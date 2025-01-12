package com.efree.gateway.route;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class RouteConfig {

    @Value("${rest.user-service.base-url}")
    private String userServiceUrl;
    @Value("${rest.category-service.base-url}")
    private String categoryServiceUrl;
    @Value("${rest.product-service.base-url}")
    private String productServiceUrl;
    @Value("${rest.order-service.base-url}")
    private String orderServiceUrl;
    @Value("${rest.payment-service.base-url}")
    private String paymentServiceUrl;
    @Value("${rest.file-service.base-url}")
    private String fileServiceUrl;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/gateway/USER/**", "/gateway/user/**")
                        .filters(f -> f.rewritePath("/gateway/USER/(?<segment>.*)", "/${segment}")
                                .rewritePath("/gateway/user/(?<segment>.*)", "/${segment}")
                                .addResponseHeader( "X-RESPONSE-TIME", LocalDateTime.now().toString()))
                        .uri(userServiceUrl))
//                        .uri("lb:user-service"))
                .route(p -> p
                        .path("/gateway/CATEGORY/**", "/gateway/category/**")
                        .filters(f -> f.rewritePath("/gateway/CATEGORY/(?<segment>.*)", "/${segment}")
                                .rewritePath("/gateway/category/(?<segment>.*)", "/${segment}")
                                .addResponseHeader( "X-RESPONSE-TIME", LocalDateTime.now().toString()))
                        .uri(categoryServiceUrl))
                .route(p -> p
                        .path("/gateway/PRODUCT/**", "/gateway/product/**")
                        .filters(f -> f.rewritePath("/gateway/PRODUCT/(?<segment>.*)", "/${segment}")
                                .rewritePath("/gateway/product/(?<segment>.*)", "/${segment}")
                                .addResponseHeader( "X-RESPONSE-TIME", LocalDateTime.now().toString()))
                        .uri(productServiceUrl))
                .route(p -> p
                        .path("/gateway/ORDER/**", "/gateway/order/**")
                        .filters(f -> f.rewritePath("/gateway/ORDER/(?<segment>.*)", "/${segment}")
                                .rewritePath("/gateway/order/(?<segment>.*)", "/${segment}")
                                .addResponseHeader( "X-RESPONSE-TIME", LocalDateTime.now().toString()))
                        .uri(orderServiceUrl))
                .route(p -> p
                        .path("/gateway/PAYMENT/**", "/gateway/payment/**")
                        .filters(f -> f.rewritePath("/gateway/PAYMENT/(?<segment>.*)", "/${segment}")
                                .rewritePath("/gateway/payment/(?<segment>.*)", "/${segment}")
                                .addResponseHeader( "X-RESPONSE-TIME", LocalDateTime.now().toString()))
                        .uri(paymentServiceUrl))
                .route(p -> p
                        .path("/gateway/RESOURCE/**", "/gateway/resource/**")
                        .filters(f -> f.rewritePath("/gateway/RESOURCE/(?<segment>.*)", "/${segment}")
                                .rewritePath("/gateway/resource/(?<segment>.*)", "/${segment}")
                                .addResponseHeader( "X-RESPONSE-TIME", LocalDateTime.now().toString()))
                        .uri(fileServiceUrl))
                .build();
    }

}
