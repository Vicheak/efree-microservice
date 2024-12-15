package com.efree.product.api.controller;

import com.efree.product.api.base.BaseApi;
import com.efree.product.api.dto.request.FavoriteRequest;
import com.efree.product.api.dto.response.FavoriteResponse;
import com.efree.product.api.dto.response.ListFavResponse;
import com.efree.product.api.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    @Operation(summary = "add to favorite")
    public ResponseEntity<BaseApi<Object>> addProductToFavorite(@RequestBody FavoriteRequest request) {
        FavoriteResponse favoriteResponse = favoriteService.addProductToFavorite(request);
        BaseApi<Object> api = BaseApi.builder()
                .code(HttpStatus.CREATED.value())
                .isSuccess(true)
                .message("Add product to favorite successfully")
                .payload(favoriteResponse)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.CREATED);
    }

    @GetMapping("/user-favorites")
    @Operation(summary = "get all fav of user")
    public ResponseEntity<BaseApi<Object>> findAllByUser(@RequestParam String userId) {
        ListFavResponse favoriteResponses = favoriteService.getAllFavoritesByUserId(userId);
        BaseApi<Object> api = BaseApi.builder()
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .message("Get all favorite products of user successfully")
                .payload(favoriteResponses)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

}