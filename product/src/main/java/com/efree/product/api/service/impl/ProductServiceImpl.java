package com.efree.product.api.service.impl;

import com.efree.product.api.dto.request.ImportProductRequest;
import com.efree.product.api.dto.request.PostStockRequest;
import com.efree.product.api.dto.request.ProductRequest;
import com.efree.product.api.dto.response.ImportProductResponse;
import com.efree.product.api.dto.response.ProductResponse;
import com.efree.product.api.entity.Product;
import com.efree.product.api.entity.ProductImage;
import com.efree.product.api.external.categoryservice.CategoryServiceRestClientConsumer;
import com.efree.product.api.external.categoryservice.dto.CategoryResponseDto;
import com.efree.product.api.repository.ProductRepository;
import com.efree.product.api.service.ProductService;
import com.efree.product.api.util.ValueInjectUtil;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryServiceRestClientConsumer categoryServiceRestClientConsumer;
    private final ValueInjectUtil valueInjectUtil;

    @Transactional
    @Override
    public ProductResponse createProduct(ProductRequest request) {
        //validate request
        validateProductRequest(request, true);
        Product product = productRepository.save(mapToProduct(request));
        ProductResponse productResponse = product.toResponse();
        productResponse.setCategoryResponseDto(request.getCategoryResponseDto());
        return productResponse;
    }

    @Override
    public Page<ProductResponse> getAllProductsByPagination(int page, int size, String sortBy, String direction) {
        PageRequest pageRequest = buildPageRequest(page, size, sortBy, direction);
        Page<Product> productsPage = productRepository.findAll(pageRequest);
        return productsPage.map(product -> {
            //set up category resource
            CategoryResponseDto categoryResponseDto =
                    categoryServiceRestClientConsumer.loadCategoryById(product.getCategoryId());
            ProductResponse productResponse = product.toResponse();
            productResponse.setCategoryResponseDto(categoryResponseDto);
            //set up product image
            List<ProductImage> productImages = product.getProductImages();
            //check based product image
            Optional<ProductImage> optionalProductImage = productImages.stream()
                    .filter(ProductImage::getIsBased)
                    .findFirst();
            if(optionalProductImage.isPresent()){
                ProductImage basedProductImage = optionalProductImage.get();
                productResponse.setBasedImageUrl(valueInjectUtil.getImageUri(basedProductImage.getImageUrl()));
                productResponse.setBasedImageDownloadUrl(valueInjectUtil.getDownloadUri(basedProductImage.getImageUrl()));
            }
            return productResponse;
        });
    }

    @Override
    public ProductResponse getProductById(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Product with id, %s has not been found in the system!"
                                        .formatted(productId.toString()))
                );
        CategoryResponseDto categoryResponseDto =
                categoryServiceRestClientConsumer.loadCategoryById(product.getCategoryId());
        ProductResponse productResponse = product.toResponse();
        productResponse.setCategoryResponseDto(categoryResponseDto);
        return productResponse;
    }

    @Override
    public Page<ProductResponse> getAllBestSellingProducts(int page, int size, String sortBy, String direction) {
        PageRequest pageRequest = buildPageRequest(page, size, sortBy, direction);
        Specification<Product> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isBestSeller"), true); //only best selling
        Page<Product> productsPage = productRepository.findAll(spec, pageRequest);
        return productsPage.map(Product::toResponse);
    }

    @Override
    public List<ProductResponse> getProductsByCategory(String categoryId) {
        categoryServiceRestClientConsumer.loadCategoryById(categoryId);
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return products.stream().map(Product::toResponse).toList();
    }

    @Override
    public Page<ProductResponse> searchProducts(String keyword, String field, int page, int size, String sortBy, String direction) {
        PageRequest pageRequest = buildPageRequest(page, size, sortBy, direction);
        Specification<Product> spec = (root, query, criteriaBuilder) -> {
            if (field != null && keyword != null) {
                return criteriaBuilder.like(criteriaBuilder.lower(root.get(field)),
                        "%" + keyword.toLowerCase() + "%");
            }
            return null;
        };
        Page<Product> productsPage = productRepository.findAll(spec, pageRequest);
        return productsPage.map(Product::toResponse);
    }

    @Override
    public List<ProductResponse> filterProductsByPrice(BigDecimal from, BigDecimal to) {
        // Validate price range
        if (from.compareTo(to) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid price range");
        }

        // Fetch filtered products
        List<Product> products = productRepository.findByPriceBetween(from, to);

        // Convert to ProductResponse
        return products.stream()
                .map(Product::toResponse)
                .toList();
    }

    @Transactional
    @Override
    public ProductResponse updateProductById(UUID productId, ProductRequest request) {
        validateProductRequest(request, false);

        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Product with id, %s has not been found in the system!"
                                        .formatted(productId.toString()))
                );

        // Check for duplicate product name
        if (Objects.nonNull(request.getNameEn()) && !request.getNameEn().isEmpty())
            if (!request.getNameEn().equalsIgnoreCase(product.getNameEn()) &&
                    productRepository.existsByNameEnIsIgnoreCase(request.getNameEn()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Product's name conflicts resource in the system!");

        if (Objects.nonNull(request.getNameKh()) && !request.getNameKh().isEmpty())
            if (!request.getNameKh().equalsIgnoreCase(product.getNameKh()) &&
                    productRepository.existsByNameKhIsIgnoreCase(request.getNameKh()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Product's name conflicts resource in the system!");

        Product updatedProduct = mapToProduct(request);
        updatedProduct.setId(product.getId()); // Retain the same ID for the update
        productRepository.save(updatedProduct);
        ProductResponse productResponse = updatedProduct.toResponse();
        productResponse.setCategoryResponseDto(request.getCategoryResponseDto());
        return productResponse;
    }

    @Transactional
    @Override
    public void deleteProductById(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Product with id, %s has not been found in the system!"
                                        .formatted(productId.toString()))
                );

        //check resources in product
        if (!product.getProductImages().isEmpty() || !product.getPromotions().isEmpty() ||
                !product.getFavorites().isEmpty() || !product.getRates().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Product resource cannot be removed, it is used for another resource");
        }

        productRepository.delete(product);
    }

    @Transactional
    @Override
    public List<ImportProductResponse> importProduct(MultipartFile requestedFile) {
        //define map for unexpected error
        Map<Integer, String> map = new HashMap<>();
        List<ImportProductRequest> importProductRequests = new ArrayList<>();

        try {
            Workbook workbook = new XSSFWorkbook(requestedFile.getInputStream());
            Sheet sheet = workbook.getSheet("import");
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();
            int rowNumber = 1;
            while (rowIterator.hasNext()) {
                try {
                    Row row = rowIterator.next();
                    int cellIndex = 0;

                    Cell cellNo = row.getCell(cellIndex);
                    String productUuid = cellNo.getStringCellValue();

                    //skip product note or description
                    cellIndex += 2;

                    cellNo = row.getCell(cellIndex++);
                    double productPrice = cellNo.getNumericCellValue();

                    cellNo = row.getCell(cellIndex++);
                    long productStockAmt = (long) cellNo.getNumericCellValue();

                    //build import product request
                    ImportProductRequest importProductRequest = new ImportProductRequest();
                    importProductRequest.setProductUuid(productUuid);
                    importProductRequest.setPrice(BigDecimal.valueOf(productPrice));
                    importProductRequest.setStockAmt(productStockAmt);
                    importProductRequests.add(importProductRequest);

                    rowNumber++;
                } catch (Exception ex) {
                    map.put(rowNumber, ex.getMessage());
                    rowNumber++;
                }
            }
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        if (!map.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, map.toString());
        }

        //validate imported product
        Map<String, Product> requestImportProduct = new HashMap<>();
        importProductRequests.forEach(importProductRequest -> {
            Product product = productRepository.findById(UUID.fromString(importProductRequest.getProductUuid()))
                    .orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Product with id, %s has not been found in the system!"
                                            .formatted(importProductRequest.getProductUuid()))
                    );

            if (importProductRequest.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Product price must not be less than ZERO or exact ZERO!, " + importProductRequest.getProductUuid());
            }

            if (importProductRequest.getStockAmt() < 0) {
                if (product.getStockQty() - (Math.abs(importProductRequest.getStockAmt())) < 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Product stock is insufficient for deduction of stock amount!, " + importProductRequest.getProductUuid());
                }
            }

            requestImportProduct.put(importProductRequest.getProductUuid(), product);
        });

        List<ImportProductResponse> importProductResponses = new ArrayList<>();
        importProductRequests.forEach(importProductRequest -> {
            Product product = requestImportProduct.get(importProductRequest.getProductUuid());
            product.setPrice(importProductRequest.getPrice());
            product.setStockQty(product.getStockQty() + importProductRequest.getStockAmt());
            productRepository.save(product);

            //build response
            ImportProductResponse importProductResponse = new ImportProductResponse();
            importProductResponse.setProductId(product.getId().toString());
            importProductResponse.setNameEn(product.getNameEn());
            importProductResponse.setNameKh(product.getNameKh());
            importProductResponse.setPrice(product.getPrice());
            importProductResponse.setStockQty(product.getStockQty());
            importProductResponse.setStatus(product.getStatus());
            importProductResponse.setCreatedAt(product.getCreatedAt());
            importProductResponse.setUpdatedAt(product.getUpdatedAt());
            importProductResponses.add(importProductResponse);
        });

        return importProductResponses;
    }

    @Transactional
    @Override
    public void postStock(PostStockRequest request) {
        if (request.getQuantity() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Post quantity must not be ZERO");
        }

        Product product = productRepository.findById(UUID.fromString(request.getProductId()))
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Product with id, %s has not been found in the system!"
                                        .formatted(request.getProductId()))
                );

        if (request.getQuantity() > product.getStockQty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Quantity exceeds stock limit");
        }
        product.setStockQty(product.getStockQty() - request.getQuantity());
        productRepository.save(product);
    }

    private void validateProductRequest(ProductRequest request, Boolean isForNew) {
        // Check for category
        CategoryResponseDto categoryResponseDto =
                categoryServiceRestClientConsumer.loadCategoryById(request.getCategoryId());
        request.setCategoryResponseDto(categoryResponseDto);
    }

    private PageRequest buildPageRequest(int page, int size, String sortBy, String direction) {
        if (page <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Page number must be greater than 0");
        }
        Sort.Direction sortDirection = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(page - 1, size, Sort.by(sortDirection, sortBy));
    }

    private Product mapToProduct(ProductRequest request) {
        return Product.builder()
                .categoryId(request.getCategoryId())
                .nameEn(request.getNameEn())
                .nameKh(request.getNameKh())
                .descriptionEn(request.getDescriptionEn())
                .descriptionKh(request.getDescriptionKh())
                .price(request.getPrice())
                .stockQty(request.getStockQty())
                .status(request.getStatus())
                .weightType(request.getWeightType())
                .weight(request.getWeight())
                .dimension(request.getDimension())
                .brand(request.getBrand())
                .warrantyPeriod(request.getWarrantyPeriod())
                .isFeatured(request.getIsFeatured())
                .isNewArrival(request.getIsNewArrival())
                .isBestSeller(request.getIsBestSeller())
                .shippingClass(request.getShippingClass())
                .returnPolicy(request.getReturnPolicy())
                .metaTitle(request.getMetaTitle())
                .metaDescription(request.getMetaDescription())
                .isSecondHand(request.getIsSecondHand())
                .secondHandDescription(request.getSecondHandDescription())
                .build();
    }

}