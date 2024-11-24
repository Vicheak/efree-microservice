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
                        .path("/gateway/CATEGORY/**", "/gateway/category/**")
                        .filters(f -> f.rewritePath("/gateway/CATEGORY/(?<segment>.*)", "/${segment}")
                                .rewritePath("/gateway/category/(?<segment>.*)", "/${segment}")
                                .addResponseHeader( "X-RESPONSE-TIME", LocalDateTime.now().toString()))
                        .uri("lb://category-service"))
                .route(p -> p
                        .path("/gateway/USER/**", "/gateway/user/**")
                        .filters(f -> f.rewritePath("/gateway/USER/(?<segment>.*)", "/${segment}")
                                .rewritePath("/gateway/user/(?<segment>.*)", "/${segment}")
                                .addResponseHeader( "X-RESPONSE-TIME", LocalDateTime.now().toString()))
                        .uri("lb://user-service"))
                .build();
    }

}
