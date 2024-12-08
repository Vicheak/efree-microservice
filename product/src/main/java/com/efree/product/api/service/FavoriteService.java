package com.efree.product.api.service;

import com.efree.product.api.dto.request.FavoriteRequest;
import com.efree.product.api.dto.response.FavoriteResponse;
import com.efree.product.api.dto.response.ListFavResponse;

import java.util.List;

public interface FavoriteService {
    FavoriteResponse addProductToFavorite(FavoriteRequest request);
    ListFavResponse getAllFavoritesByUserId(String userId);
}
