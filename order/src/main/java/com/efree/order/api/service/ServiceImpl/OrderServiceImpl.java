package com.efree.order.api.service.ServiceImpl;

import com.efree.order.api.dto.request.OrderRequestDTO;
import com.efree.order.api.dto.request.ProductRequestDTO;
import com.efree.order.api.dto.response.OrderDetailResponseDTO;
import com.efree.order.api.dto.response.OrderResponseDTO;
import com.efree.order.api.entity.Order;
import com.efree.order.api.entity.OrderDetail;
import com.efree.order.api.entity.Product;
import com.efree.order.api.repository.OrderDetailRepository;
import com.efree.order.api.repository.OrderRepository;
import com.efree.order.api.repository.ProductRepository;
import com.efree.order.api.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO requestDTO) {
        // Calculate total amount by streaming over products first
        BigDecimal totalAmount = requestDTO.getProducts().stream()
                .map(productRequest -> {
                    // Fetch product by ID
                    Product product = productRepository.findById(productRequest.getProductId())
                            .orElseThrow(() -> new IllegalArgumentException("Product with ID " + productRequest.getProductId() + " not found"));

                    // Calculate total price for the product in this order detail
                    BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(productRequest.getQuantity()));
                    return totalPrice;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Sum up the total price for all products

        // Ensure that totalAmount is not null
        if (totalAmount == null) {
            totalAmount = BigDecimal.ZERO;
        }

        // Create the order and set the total amount before saving
        Order order = new Order();
        order.setCustomerId(requestDTO.getCustomerId());
        order.setOrderStatus("ordered");
        order.setPaymentStatus("pending");
        order.setOrderDate(LocalDateTime.now());
        order.setCreatedAt(LocalDateTime.now());
        order.setIsPrepared(true);
        order.setBillingAddress(requestDTO.getBillingAddress());
        order.setUpdatedAt(LocalDateTime.now());
        order.setShippingAddress(requestDTO.getShippingAddress());
        order.setTotalAmount(totalAmount); // Set total amount here

        // Save the order now that totalAmount is set
        order = orderRepository.save(order);

        // Get the order ID after saving the order
        final String orderId = order.getId().toString();  // Save the order ID in a final local variable

        // List to store OrderDetailResponseDTOs
        List<OrderDetailResponseDTO> orderDetailsList = requestDTO.getProducts().stream()
                .map(productRequest -> {
                    // Fetch product by ID
                    Product product = productRepository.findById(productRequest.getProductId())
                            .orElseThrow(() -> new IllegalArgumentException("Product with ID " + productRequest.getProductId() + " not found"));

                    // Create the order detail for each product
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrderId(orderId);  // Use the final orderId variable
                    orderDetail.setProductId(product.getId().toString());
                    orderDetail.setQuantity(productRequest.getQuantity());
                    orderDetail.setUnitPrice(product.getPrice());

                    // Calculate total price for the product in this order detail
                    BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(productRequest.getQuantity()));
                    orderDetail.setTotalPrice(totalPrice);

                    // Save the order detail
                    orderDetailRepository.save(orderDetail);

                    // Return OrderDetailResponseDTO with product name, price, etc.
                    return new OrderDetailResponseDTO(
                            product.getId().toString(),
                            product.getNameEn(),
                            product.getNameKh(),
                            productRequest.getQuantity(),
                            product.getPrice(),
                            totalPrice
                    );
                }).collect(Collectors.toList());

        // Return the response DTO including order and order details
        return new OrderResponseDTO(
                order.getId().toString(),
                order.getCustomerId(),
                order.getOrderDate().toString(),
                order.getTotalAmount(),
                order.getOrderStatus(),
                order.getPaymentStatus(),
                order.getShippingAddress(),
                order.getBillingAddress(),
                orderDetailsList
        );
    }
}