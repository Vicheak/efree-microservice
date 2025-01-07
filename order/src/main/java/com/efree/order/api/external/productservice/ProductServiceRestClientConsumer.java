package com.efree.order.api.external.productservice;

import com.efree.order.api.base.BaseApi;
import com.efree.order.api.external.productservice.dto.PostStockRequest;
import com.efree.order.api.external.productservice.dto.PromotionResponse;
import com.efree.order.api.external.productservice.dto.PromotionUsageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceRestClientConsumer {

    private final ProductServiceRestClient productServiceRestClient;

    public void postStock(String productId, Long quantity){
        PostStockRequest postStockRequest = new PostStockRequest();
        postStockRequest.setProductId(productId);
        postStockRequest.setQuantity(quantity.intValue());
        productServiceRestClient.postStock(postStockRequest);
    }

    public List<PromotionResponse> getPromotionsByProductId(String productId) {
        BaseApi<List<PromotionResponse>> promotionResponseListBaseApi =
                productServiceRestClient.getPromotionsByProductId(productId);
        return promotionResponseListBaseApi.payload();
    }

    public void updatePromotionUsage(String productId, String promotionId){
        PromotionUsageRequest promotionUsageRequest = new PromotionUsageRequest();
        promotionUsageRequest.setProductId(productId);
        promotionUsageRequest.setPromotionId(promotionId);
        productServiceRestClient.updatePromotionUsage(promotionUsageRequest);
    }

}
