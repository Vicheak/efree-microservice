package com.efree.product.api.service;

import com.efree.product.api.dto.request.FavoriteRequest;
import com.efree.product.api.dto.response.FavoriteResponse;
import com.efree.product.api.dto.response.ListFavResponse;

public interface FavoriteService {

    /**
     * This method is used to add a product to the favorite list
     * @param request is the request from client
     * @return FavoriteResponse
     */
    FavoriteResponse addProductToFavorite(FavoriteRequest request);

    /**
     * This method is used to get a list of product favorite
     * @param userId is the request parameter from client
     * @return ListFavResponse
     */
    ListFavResponse getAllFavoritesByUserId(String userId);

    /**
     * This method is used to remove list of products of the favorite list
     * @param request is the request from client
     */
    void removeProductFromFavorite(FavoriteRequest request);

}