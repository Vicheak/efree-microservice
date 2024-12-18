package com.efree.product.api.service.impl;

import com.efree.product.api.dto.request.FavoriteRequest;
import com.efree.product.api.dto.response.FavoriteResponse;
import com.efree.product.api.dto.response.ListFavResponse;
import com.efree.product.api.dto.response.ProductResponse;
import com.efree.product.api.entity.Favorite;
import com.efree.product.api.entity.Product;
import com.efree.product.api.exception.CustomNotfoundException;
import com.efree.product.api.repository.FavoriteRepository;
import com.efree.product.api.repository.ProductRepository;
import com.efree.product.api.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;

    @Transactional
    @Override
    public FavoriteResponse addProductToFavorite(FavoriteRequest request) {
        List<Favorite> existingFavorites = favoriteRepository.findAllByUserId(request.getUserId());

        // Check if any products in the request is already in the favorites
        for (String productId : request.getProductIds()) {
            UUID productUUID = UUID.fromString(productId);
            boolean isAlreadyFavorite = existingFavorites.stream()
                    .flatMap(favorite -> favorite.getProducts().stream())
                    .anyMatch(product -> product.getId().equals(productUUID));
            if (isAlreadyFavorite) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "This product with ID, %s already added".formatted(productId));
            }
        }

        List<Product> products = new ArrayList<>();
        for (String productId : request.getProductIds()) {
            Product product = productRepository.findById(UUID.fromString(productId))
                    .orElseThrow(() -> new CustomNotfoundException("Product with ID " + productId + " not found."));
            products.add(product);
        }

        Favorite favorite = Favorite.builder()
                .userId(request.getUserId())
                .products(products)
                .build();

        return favoriteRepository.save(favorite).toResponse();
    }

    @Override
    public ListFavResponse getAllFavoritesByUserId(String userId) {
        List<Favorite> favorites = favoriteRepository.findAllByUserId(userId);
        List<ProductResponse> productResponses = favorites.stream()
                .flatMap(favorite -> favorite.getProducts().stream())
                .distinct()
                .map(Product::toResponse)
                .collect(Collectors.toList());

        return ListFavResponse.builder()
                .userId(userId)
                .products(productResponses)
                .build();
    }

}