package com.efree.product.api.service.impl;

import com.efree.product.api.dto.request.FavoriteRequest;
import com.efree.product.api.dto.response.FavoriteResponse;
import com.efree.product.api.dto.response.ListFavResponse;
import com.efree.product.api.dto.response.ProductResponse;
import com.efree.product.api.entity.Favorite;
import com.efree.product.api.entity.Product;
import com.efree.product.api.external.userservice.UserServiceRestClientConsumer;
import com.efree.product.api.repository.FavoriteRepository;
import com.efree.product.api.repository.ProductRepository;
import com.efree.product.api.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;
    private final UserServiceRestClientConsumer userServiceRestClientConsumer;

    @Transactional
    @Override
    public FavoriteResponse addProductToFavorite(FavoriteRequest request) {
        checkUserExist(request.getUserId());

        // Check validate products
        Set<Product> products = new HashSet<>();
        for (String productId : request.getProductIds()) {
            Product product = productRepository.findById(UUID.fromString(productId))
                    .orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Product with id, %s has not been found in the system!"
                                            .formatted(productId))
                    );
            products.add(product);
        }

        // Check if any products in the request is already in the favorites
        List<Favorite> existingFavorites = favoriteRepository.findAllByUserId(request.getUserId());
        for (String productId : request.getProductIds()) {
            UUID productUUID = UUID.fromString(productId);
            boolean isAlreadyFavorite = existingFavorites.stream()
                    .flatMap(favorite -> favorite.getProducts().stream())
                    .anyMatch(product -> product.getId().equals(productUUID));
            if (isAlreadyFavorite) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "This product with ID, %s already added" .formatted(productId));
            }
        }

        Favorite favorite = Favorite.builder()
                .userId(request.getUserId())
                .products(products)
                .build();
        return favoriteRepository.save(favorite).toResponse();
    }

    @Override
    public ListFavResponse getAllFavoritesByUserId(String userId) {
        checkUserExist(userId);

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

    @Transactional
    @Override
    public void removeProductFromFavorite(FavoriteRequest request) {
        checkUserExist(request.getUserId());

        // Check validate products
        for (String productId : request.getProductIds()) {
            productRepository.findById(UUID.fromString(productId))
                    .orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Product with id, %s has not been found in the system!"
                                            .formatted(productId))
                    );
        }

        List<Favorite> favorites = favoriteRepository.findAllByUserId(request.getUserId());
        favorites.forEach(favorite -> {
            favorite.getProducts().forEach(product -> {
                if(request.getProductIds().contains(product.getId().toString())){
                    favoriteRepository.deleteByFavoriteIdAndProductId(favorite.getId(), product.getId());
                    favoriteRepository.deleteById(favorite.getId());
                }
            });
        });
    }

    private void checkUserExist(String userId) {
        // Check if user does not exist
        userServiceRestClientConsumer.loadUserByUuid(userId);
    }

}