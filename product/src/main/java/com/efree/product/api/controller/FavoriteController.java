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
import java.util.List;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;
    @PostMapping
    @Operation(summary = "add to favorite")
    public ResponseEntity<FavoriteResponse> addProductToFavorite(@RequestBody FavoriteRequest request){
        FavoriteResponse favoriteResponse = favoriteService.addProductToFavorite(request);
        BaseApi api = BaseApi.builder()
                .code(201)
                .isSuccess(true)
                .message("Add product to Favorite successful")
                .payload(favoriteResponse)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity(api, HttpStatus.CREATED);
    }

    @GetMapping("/user-favorites")
    @Operation(summary = "get all fav of user")
    public ResponseEntity<List<FavoriteResponse>> findAllByUser(@RequestParam String userId){
        ListFavResponse favoriteResponses = favoriteService.getAllFavoritesByUserId(userId);
        BaseApi api = BaseApi.builder()
                .code(200)
                .isSuccess(true)
                .message("Get all favorites of user successful")
                .payload(favoriteResponses)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity(api, HttpStatus.OK);
    }

}
