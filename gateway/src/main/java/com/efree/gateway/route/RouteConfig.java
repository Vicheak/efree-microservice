package com.efree.gateway.route;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/gateway/USER/**", "/gateway/user/**")
                        .filters(f -> f.rewritePath("/gateway/USER/(?<segment>.*)", "/${segment}")
                                .rewritePath("/gateway/user/(?<segment>.*)", "/${segment}")
                                .addResponseHeader( "X-RESPONSE-TIME", LocalDateTime.now().toString()))
                        .uri("lb://user-service"))
                .route(p -> p
                        .path("/gateway/CATEGORY/**", "/gateway/category/**")
                        .filters(f -> f.rewritePath("/gateway/CATEGORY/(?<segment>.*)", "/${segment}")
                                .rewritePath("/gateway/category/(?<segment>.*)", "/${segment}")
                                .addResponseHeader( "X-RESPONSE-TIME", LocalDateTime.now().toString()))
                        .uri("lb://category-service"))
                .route(p -> p
                        .path("/gateway/PRODUCT/**", "/gateway/product/**")
                        .filters(f -> f.rewritePath("/gateway/PRODUCT/(?<segment>.*)", "/${segment}")
                                .rewritePath("/gateway/product/(?<segment>.*)", "/${segment}")
                                .addResponseHeader( "X-RESPONSE-TIME", LocalDateTime.now().toString()))
                        .uri("lb://product-service"))
                .route(p -> p
                        .path("/gateway/ORDER/**", "/gateway/order/**")
                        .filters(f -> f.rewritePath("/gateway/ORDER/(?<segment>.*)", "/${segment}")
                                .rewritePath("/gateway/order/(?<segment>.*)", "/${segment}")
                                .addResponseHeader( "X-RESPONSE-TIME", LocalDateTime.now().toString()))
                        .uri("lb://order-service"))
                .route(p -> p
                        .path("/gateway/PAYMENT/**", "/gateway/payment/**")
                        .filters(f -> f.rewritePath("/gateway/PAYMENT/(?<segment>.*)", "/${segment}")
                                .rewritePath("/gateway/payment/(?<segment>.*)", "/${segment}")
                                .addResponseHeader( "X-RESPONSE-TIME", LocalDateTime.now().toString()))
                        .uri("lb://payment-service"))
                .route(p -> p
                        .path("/gateway/RESOURCE/**", "/gateway/resource/**")
                        .filters(f -> f.rewritePath("/gateway/RESOURCE/(?<segment>.*)", "/${segment}")
                                .rewritePath("/gateway/resource/(?<segment>.*)", "/${segment}")
                                .addResponseHeader( "X-RESPONSE-TIME", LocalDateTime.now().toString()))
                        .uri("lb://file-service"))
                .build();
    }

}
