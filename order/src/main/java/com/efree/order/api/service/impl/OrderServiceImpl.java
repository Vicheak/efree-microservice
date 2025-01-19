package com.efree.order.api.service.impl;

import com.efree.order.api.dto.mapper.OrderMapper;
import com.efree.order.api.dto.mapper.OrderUnauthMapper;
import com.efree.order.api.dto.request.*;
import com.efree.order.api.dto.response.*;
import com.efree.order.api.entity.*;
import com.efree.order.api.external.paymentservice.PaymentServiceRestClientConsumer;
import com.efree.order.api.external.paymentservice.dto.PaymentResponse;
import com.efree.order.api.external.productservice.ProductServiceRestClientConsumer;
import com.efree.order.api.external.productservice.dto.PromotionResponse;
import com.efree.order.api.external.userservice.UserServiceRestClientConsumer;
import com.efree.order.api.external.userservice.dto.UserDto;
import com.efree.order.api.repository.*;
import com.efree.order.api.service.OrderService;
import com.efree.order.api.util.EOrderStatus;
import com.efree.order.api.util.EPaymentStatus;
import com.efree.order.api.util.OrderUtil;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderUnauthRepository orderUnauthRepository;
    private final OrderUnauthDetailRepository orderUnauthDetailRepository;
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final UserServiceRestClientConsumer userServiceRestClientConsumer;
    private final ProductServiceRestClientConsumer productServiceRestClientConsumer;
    private final PaymentServiceRestClientConsumer paymentServiceRestClientConsumer;
    private final OrderUnauthMapper orderUnauthMapper;

    @Override
    public ProceedAddToCartResponse proceedAddToCart(ProceedAddToCartRequest proceedAddToCartRequest) {
        ProceedAddToCartResponse proceedAddToCartResponse = new ProceedAddToCartResponse();

        if (proceedAddToCartRequest.shoppingCart().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Your shopping cart is empty!");
        }

        validateDuplicateItem(proceedAddToCartRequest.shoppingCart());

        //check if product list exists or not and check requested stock availability
        Map<String, BigDecimal> productPriceMap = proceedAddToCartRequest.shoppingCart().stream()
                .map(addToCartRequest -> validateProductResource(addToCartRequest.productId(), addToCartRequest.quantity()))
                .collect(Collectors.toMap(product -> product.getId().toString(), Product::getPrice));

        //build cart list
        List<AddToCartResponse> cartList = new ArrayList<>();
        for (AddToCartRequest addToCartRequest : proceedAddToCartRequest.shoppingCart()) {
            AddToCartResponse productCart = new AddToCartResponse();
            productCart.setProductId(addToCartRequest.productId());
            productCart.setQuantity(addToCartRequest.quantity());
            productCart.setUnitPrice(productPriceMap.get(addToCartRequest.productId()));
            productCart.setIsApplyPromotion(false);
            productCart.setPromotionId("");
            productCart.setPromoCode("");

            //check promotion of product by requested PROMO CODE
            double subTotalPrice = productCart.getUnitPrice().doubleValue() * productCart.getQuantity();
            double discount;
            double totalDiscount;
            double finalSubTotalPrice;
            if (!addToCartRequest.promoCode().isEmpty()) {
                List<PromotionResponse> promotionResponseList =
                        productServiceRestClientConsumer.getPromotionsByProductId(addToCartRequest.productId());
                Optional<PromotionResponse> promotionResponseOptional = promotionResponseList.stream()
                        .filter(promotionResponse ->
                                promotionResponse.getPromoCode().equals(addToCartRequest.promoCode()))
                        .findFirst();

                if (promotionResponseOptional.isPresent()) {
                    PromotionResponse promotionResponse = promotionResponseOptional.get();

                    //check valid requested promotion
                    if (!promotionResponse.getStatus()) {
                        throw new ResponseStatusException(HttpStatus.OK,
                                "Sorry, this promotion " + promotionResponse.getPromoCode() + " is inactive!");
                    }
                    if (promotionResponse.getUsageLimit() < promotionResponse.getUsageCount() + 1) {
                        throw new ResponseStatusException(HttpStatus.OK,
                                "Sorry, this promotion " + promotionResponse.getPromoCode() + " exceeds the usage limit!");
                    }
                    if (promotionResponse.getEndDate().isBefore(LocalDateTime.now())) {
                        throw new ResponseStatusException(HttpStatus.OK,
                                "Sorry, this promotion " + promotionResponse.getPromoCode() + " is expired!");
                    }

                    productCart.setIsApplyPromotion(true);
                    productCart.setPromotionId(promotionResponse.getPromotionId());
                    productCart.setPromoCode(addToCartRequest.promoCode());

                    discount = promotionResponse.getDiscount(); //discount as %
                    totalDiscount = (subTotalPrice * (discount / 100));
                    finalSubTotalPrice = subTotalPrice - totalDiscount;
                } else {
                    discount = 0;
                    totalDiscount = 0;
                    finalSubTotalPrice = subTotalPrice;
                }
            } else {
                discount = 0;
                totalDiscount = 0;
                finalSubTotalPrice = subTotalPrice;
            }

            productCart.setDiscount(discount + "%");
            productCart.setSubTotalPrice(BigDecimal.valueOf(subTotalPrice));
            productCart.setTotalDiscount(BigDecimal.valueOf(totalDiscount));
            productCart.setFinalSubTotalPrice(BigDecimal.valueOf(finalSubTotalPrice));

            cartList.add(productCart);
        }

        processCartList(cartList, proceedAddToCartResponse);

        return proceedAddToCartResponse;
    }

    @Transactional
    @Override
    public AuthorizeOrderResponse authorizeOrder(String authUserUuid, AuthorizeOrderRequest authorizeOrderRequest) {
        AuthorizeOrderResponse authorizeOrderResponse = new AuthorizeOrderResponse();

        if (authorizeOrderRequest.cartList().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Your shopping cart is empty! cannot place order and proceed payment!");
        }

        validateDuplicateItemAuthorize(authorizeOrderRequest.cartList());

        UserDto userDto = validateUserRequest(authUserUuid);

        authorizeOrderRequest.cartList().forEach(authorizeCartRequest ->
                validateProductResource(authorizeCartRequest.productId(), authorizeCartRequest.quantity()));

        //post stock of product
        authorizeOrderRequest.cartList().forEach(authorizeCartRequest ->
                productServiceRestClientConsumer.postStock(authorizeCartRequest.productId(), authorizeCartRequest.quantity()));

        //check promotion to post usage count
        authorizeOrderRequest.cartList().forEach(authorizeCartRequest -> {
            if (!authorizeCartRequest.promotionId().isEmpty()) {
                productServiceRestClientConsumer.updatePromotionUsage(authorizeCartRequest.productId(), authorizeCartRequest.promotionId());
            }
        });

        //build order
        Order newOrder = new Order();
        newOrder.setOrderId(UUID.randomUUID().toString());
        newOrder.setUserId(userDto.uuid());
        newOrder.setOrderDate(LocalDateTime.now());
        newOrder.setTotalAmount(authorizeOrderRequest.totalAmount());
        newOrder.setPaymentStatus(EPaymentStatus.UNPAID.name());
        newOrder.setOrderStatus(EOrderStatus.SUCCESS.name());
        newOrder.setShippingAddress(authorizeOrderRequest.shippingAddress());
        newOrder.setBillingAddress(authorizeOrderRequest.billingAddress());
        newOrder.setIsPrepared(false);
        newOrder.setOrderContact(userDto.phoneNumber());
        orderRepository.save(newOrder);

        //build order details
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (AuthorizeCartRequest authorizeCartRequest : authorizeOrderRequest.cartList()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(newOrder.getOrderId());
            orderDetail.setProductId(UUID.fromString(authorizeCartRequest.productId()));
            orderDetail.setQuantity(authorizeCartRequest.quantity());
            orderDetail.setUnitPrice(authorizeCartRequest.unitPrice());
            orderDetail.setTotalPrice(authorizeCartRequest.totalPrice());
            orderDetails.add(orderDetail);
        }
        newOrder.setOrderDetails(orderDetails);
        orderDetailRepository.saveAll(orderDetails);

        //build payment for order
        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setAmount(newOrder.getTotalAmount());
        payment.setPaymentDate(newOrder.getOrderDate());
        payment.setPaymentMethod("API");
        payment.setPlatform("Stripe");
        payment.setReference("EFREEODR" + newOrder.getOrderId());
        payment.setPaymentStatus(EPaymentStatus.UNPAID.name());
        payment.setPaymentToken(OrderUtil.generatePaymentToken());
        payment.setOrder(newOrder);
        paymentRepository.save(payment);

        //proceed payment intent
        PaymentResponse paymentResponse =
                paymentServiceRestClientConsumer.proceedPayment(newOrder, payment);

        //build response for client
        authorizeOrderResponse.setOrderId(newOrder.getOrderId());
        authorizeOrderResponse.setOrderDate(newOrder.getOrderDate());
        authorizeOrderResponse.setTotalAmount(newOrder.getTotalAmount());
        authorizeOrderResponse.setOrderStatus(newOrder.getOrderStatus());
        authorizeOrderResponse.setIsPrepared(newOrder.getIsPrepared());
        authorizeOrderResponse.setOrderContact(newOrder.getOrderContact());

        List<AuthorizeCartResponse> authorizeCartResponseList = new ArrayList<>();
        authorizeOrderRequest.cartList().forEach(authorizeCartRequest -> {
            AuthorizeCartResponse authorizeCartResponse = new AuthorizeCartResponse();
            authorizeCartResponse.setProductId(authorizeCartRequest.productId());
            authorizeCartResponse.setQuantity(authorizeCartRequest.quantity());
            authorizeCartResponse.setUnitPrice(authorizeCartRequest.unitPrice());
            authorizeCartResponse.setTotalPrice(authorizeCartRequest.totalPrice());
            authorizeCartResponseList.add(authorizeCartResponse);
        });

        authorizeOrderResponse.setOrderDetails(authorizeCartResponseList);
        authorizeOrderResponse.setPaymentIntent(paymentResponse);

        return authorizeOrderResponse;
    }

    @Transactional
    @Override
    public SaveOrderUnauthResponse saveOrderUnauth(String authUserUuid, SaveOrderUnauthRequest saveOrderUnauthRequest) {
        SaveOrderUnauthResponse saveOrderUnauthResponse = new SaveOrderUnauthResponse();

        if (saveOrderUnauthRequest.cartList().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Your shopping cart is empty! cannot save order for proceed later!");
        }

        validateDuplicateItemUnauth(saveOrderUnauthRequest.cartList());

        UserDto userDto = validateUserRequest(authUserUuid);

        //check product list exists or not
        saveOrderUnauthRequest.cartList().forEach(saveOrderUnauthCartRequest ->
                validateProductResource(saveOrderUnauthCartRequest.productId(), saveOrderUnauthCartRequest.quantity()));

        //build order unauth
        OrderUnauth orderUnauth = new OrderUnauth();
        orderUnauth.setOrderUnauthId(UUID.randomUUID().toString());
        orderUnauth.setUserId(userDto.uuid());
        orderUnauth.setOrderDate(LocalDateTime.now());
        orderUnauth.setTotalAmount(saveOrderUnauthRequest.totalAmount());
        orderUnauth.setOrderContact(userDto.phoneNumber());
        orderUnauthRepository.save(orderUnauth);

        //build order unauth details
        List<OrderUnauthDetail> orderUnauthDetails = new ArrayList<>();
        for (SaveOrderUnauthCartRequest saveOrderUnauthCartRequest : saveOrderUnauthRequest.cartList()) {
            OrderUnauthDetail orderUnauthDetail = new OrderUnauthDetail();
            orderUnauthDetail.setOrderUnauthId(orderUnauth.getOrderUnauthId());
            orderUnauthDetail.setProductId(UUID.fromString(saveOrderUnauthCartRequest.productId()));
            orderUnauthDetail.setQuantity(saveOrderUnauthCartRequest.quantity());
            orderUnauthDetail.setUnitPrice(saveOrderUnauthCartRequest.unitPrice());
            orderUnauthDetail.setTotalPrice(saveOrderUnauthCartRequest.totalPrice());
            orderUnauthDetails.add(orderUnauthDetail);
        }
        orderUnauth.setOrderUnauthDetails(orderUnauthDetails);
        orderUnauthDetailRepository.saveAll(orderUnauthDetails);

        //build response for client
        saveOrderUnauthResponse.setOrderUnauthId(orderUnauth.getOrderUnauthId());
        saveOrderUnauthResponse.setOrderDate(orderUnauth.getOrderDate());
        saveOrderUnauthResponse.setTotalAmount(orderUnauth.getTotalAmount());
        saveOrderUnauthResponse.setOrderContact(orderUnauth.getOrderContact());

        List<SaveOrderUnauthCartResponse> saveOrderUnauthCartResponseList = new ArrayList<>();
        saveOrderUnauthRequest.cartList().forEach(saveOrderUnauthCartRequest -> {
            SaveOrderUnauthCartResponse saveOrderUnauthCartResponse = new SaveOrderUnauthCartResponse();
            saveOrderUnauthCartResponse.setProductId(saveOrderUnauthCartRequest.productId());
            saveOrderUnauthCartResponse.setQuantity(saveOrderUnauthCartRequest.quantity());
            saveOrderUnauthCartResponse.setUnitPrice(saveOrderUnauthCartRequest.totalPrice());
            saveOrderUnauthCartResponse.setTotalPrice(saveOrderUnauthCartRequest.totalPrice());
            saveOrderUnauthCartResponseList.add(saveOrderUnauthCartResponse);
        });

        saveOrderUnauthResponse.setOrderUnauthDetails(saveOrderUnauthCartResponseList);

        return saveOrderUnauthResponse;
    }

    @Override
    public OrderResponse loadOrderByOrderId(String authUserUuid, String orderId) {
        UserDto userDto = validateUserRequest(authUserUuid);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Order with id, %s has not been found in the system!"
                                        .formatted(orderId))
                );

        //validate request user for order resource
        if (!userDto.authorities().contains("ROLE_ADMIN") && !userDto.uuid().equals(order.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Permission denied to view order resource");
        }

        return orderMapper.mapFromOrderToOrderResponse(order);
    }

    @Override
    public Page<OrderResponse> loadAuthOrderHistory(String authUserUuid, int byLastDay, int page, int size, String sortBy, String direction) {
        //validate by last day param
        if (byLastDay < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "By last day cannot be less than ZERO, must start with today or the previous day!");
        }

        UserDto userDto = validateUserRequest(authUserUuid);

        PageRequest pageRequest = buildPageRequest(page, size, sortBy, direction);

        Specification<Order> spec = (root, query, criteriaBuilder) -> {
            //Calculate start of the target day
            LocalDateTime startFromDay = LocalDateTime.now().minusDays(byLastDay).toLocalDate().atStartOfDay();
            //Filter by the order date starting from the target day
            Predicate datePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("orderDate"), startFromDay);
            //Filter by user ID if provided and not an ADMIN
            if(!userDto.authorities().contains("ROLE_ADMIN")){
                Predicate userPredicate = criteriaBuilder.equal(root.get("userId"), userDto.uuid());
                return criteriaBuilder.and(datePredicate, userPredicate);
            }
            return criteriaBuilder.and(datePredicate);
        };

        Page<Order> ordersPage = orderRepository.findAll(spec, pageRequest);

        return ordersPage.map(orderMapper::mapFromOrderToOrderResponse);
    }

    @Transactional
    @Override
    public OrderStatusResponse setPrepareOrder(String orderId, OrderStatusRequest orderStatusRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Order with id, %s has not been found in the system!"
                                        .formatted(orderId))
                );

        //validate order status
        if (!EOrderStatus.isValidOrderStatus(orderStatusRequest.orderStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Order status is invalid! possible status, " + Arrays.stream(EOrderStatus.values()).toList());
        }

        order.setIsPrepared(orderStatusRequest.isPrepared());
        order.setOrderStatus(orderStatusRequest.orderStatus().toUpperCase());
        orderRepository.save(order);

        OrderStatusResponse orderStatusResponse = new OrderStatusResponse();
        orderStatusResponse.setOrderId(order.getOrderId());
        orderStatusResponse.setIsPrepared(order.getIsPrepared());
        orderStatusResponse.setOrderStatus(order.getOrderStatus());

        return orderStatusResponse;
    }

    @Override
    public OrderUnauthResponse loadOrderUnauthByOrderId(String authUserUuid, String orderId) {
        UserDto userDto = validateUserRequest(authUserUuid);

        OrderUnauth orderUnauth = orderUnauthRepository.findById(orderId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Order unauth with id, %s has not been found in the system!"
                                        .formatted(orderId))
                );

        //validate request user for order resource
        if (!userDto.authorities().contains("ROLE_ADMIN") && !userDto.uuid().equals(orderUnauth.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Permission denied to view order resource");
        }

        return orderUnauthMapper.mapFromOrderUnauthToOrderUnauthResponse(orderUnauth);
    }

    @Override
    public Page<OrderUnauthResponse> loadAuthOrderUnauthHistory(String authUserUuid, int byLastDay, int page, int size, String sortBy, String direction) {
        //validate by last day param
        if (byLastDay < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "By last day cannot be less than ZERO, must start with today or the previous day!");
        }

        UserDto userDto = validateUserRequest(authUserUuid);

        PageRequest pageRequest = buildPageRequest(page, size, sortBy, direction);

        Specification<OrderUnauth> spec = (root, query, criteriaBuilder) -> {
            //Calculate start of the target day
            LocalDateTime startFromDay = LocalDateTime.now().minusDays(byLastDay).toLocalDate().atStartOfDay();
            //Filter by the order date starting from the target day
            Predicate datePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("orderDate"), startFromDay);
            //Filter by user ID if provided and not an ADMIN
            if(!userDto.authorities().contains("ROLE_ADMIN")){
                Predicate userPredicate = criteriaBuilder.equal(root.get("userId"), userDto.uuid());
                return criteriaBuilder.and(datePredicate, userPredicate);
            }
            return criteriaBuilder.and(datePredicate);
        };

        Page<OrderUnauth> ordersPage = orderUnauthRepository.findAll(spec, pageRequest);

        return ordersPage.map(orderUnauthMapper::mapFromOrderUnauthToOrderUnauthResponse);
    }

    @Transactional
    @Override
    public void deleteOrderUnauthByOrderId(String authUserUuid, String orderId) {
        UserDto userDto = validateUserRequest(authUserUuid);

        OrderUnauth orderUnauth = orderUnauthRepository.findById(orderId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Order unauth with id, %s has not been found in the system!"
                                        .formatted(orderId))
                );

        //validate request user for order resource
        if (!userDto.authorities().contains("ROLE_ADMIN") && !userDto.uuid().equals(orderUnauth.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Permission denied to remove order unauth resource");
        }

        orderUnauthDetailRepository.deleteAll(orderUnauth.getOrderUnauthDetails());
        orderUnauthRepository.delete(orderUnauth);
    }

    private void processCartList(List<AddToCartResponse> cartList, ProceedAddToCartResponse proceedAddToCartResponse) {
        // Calculate totalAmount
        BigDecimal totalAmount = cartList.stream()
                .map(AddToCartResponse::getFinalSubTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Check if any item has a discount applied
        boolean isDiscount = cartList.stream()
                .anyMatch(AddToCartResponse::getIsApplyPromotion);

        // Calculate totalDiscount by summing all totalDiscount
        BigDecimal totalDiscount = cartList.stream()
                .map(AddToCartResponse::getTotalDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Populate ProceedAddToCartResponse
        proceedAddToCartResponse.setOrderDate(LocalDateTime.now());
        proceedAddToCartResponse.setTotalAmount(totalAmount);
        proceedAddToCartResponse.setIsDiscount(isDiscount);
        proceedAddToCartResponse.setTotalDiscount(totalDiscount);
        proceedAddToCartResponse.setCartList(cartList);
    }

    private void validateDuplicateItem(List<AddToCartRequest> shoppingCart) {
        Set<String> uniqueProductIds = new HashSet<>();
        for (AddToCartRequest item : shoppingCart) {
            if (!uniqueProductIds.add(item.productId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Shopping cart cannot contain duplicate item : " + item.productId());
            }
        }
    }

    private void validateDuplicateItemAuthorize(List<AuthorizeCartRequest> shoppingCart) {
        Set<String> uniqueProductIds = new HashSet<>();
        for (AuthorizeCartRequest item : shoppingCart) {
            if (!uniqueProductIds.add(item.productId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Authorized order cannot contain duplicate item : " + item.productId());
            }
        }
    }

    private void validateDuplicateItemUnauth(List<SaveOrderUnauthCartRequest> shoppingCart) {
        Set<String> uniqueProductIds = new HashSet<>();
        for (SaveOrderUnauthCartRequest item : shoppingCart) {
            if (!uniqueProductIds.add(item.productId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Unauthorized order cannot contain duplicate item : " + item.productId());
            }
        }
    }

    private Product validateProductResource(String productId, Long quantity) {
        // check product exists or not
        Product product = productRepository.findById(UUID.fromString(productId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        String.format("Product with ID %s does not exist!", productId)
                ));

        // validate product stock
        if (product.getStockQty() < quantity) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Insufficient stock for product ID %s. Available: %d, Requested: %d",
                            productId, product.getStockQty(), quantity)
            );
        }

        return product;
    }

    private UserDto validateUserRequest(String authUserUuid) {
        if (Objects.isNull(authUserUuid) || authUserUuid.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Resource denied request from auth user!");
        }

        //check user exists or not
        authUserUuid = decodeAuthUuid(authUserUuid);
        return userServiceRestClientConsumer.loadUserByUuid(authUserUuid);
    }

    private String decodeAuthUuid(String authUserUuid) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(authUserUuid);
            authUserUuid = new String(decodedBytes);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Error decoding request auth!");
        }
        return authUserUuid;
    }

    private PageRequest buildPageRequest(int page, int size, String sortBy, String direction) {
        if (page <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Page number must be greater than 0");
        }
        Sort.Direction sortDirection = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(page - 1, size, Sort.by(sortDirection, sortBy));
    }

}
