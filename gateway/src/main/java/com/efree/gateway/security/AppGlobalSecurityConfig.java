package com.efree.gateway.security;

import com.efree.gateway.util.KeyUtil;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import java.util.UUID;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class AppGlobalSecurityConfig {

    private final ReactiveUserDetailsService userDetailsServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final KeyUtil keyUtil;

    @Primary
    @Bean("reactiveAuthenticationManager")
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager authManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsServiceImpl);
        authManager.setPasswordEncoder(passwordEncoder);
        return authManager;
    }

    @Bean("jwtReactiveAuthenticationManager")
    public ReactiveAuthenticationManager jwtReactiveAuthenticationManager() {
        return new JwtReactiveAuthenticationManager(jwtRefreshTokenDecoder());
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChainConfig(ServerHttpSecurity http) {
        http.authorizeExchange(exchange -> {
            exchange.pathMatchers(
                    // For external resources
                    "/gateway/api/v1/auth/login",
                    "/gateway/api/v1/auth/refreshToken",
                    "/gateway/api/v1/auth/ping").permitAll();

            exchange.pathMatchers(HttpMethod.POST,
                    "/gateway/USER/api/v1/auth/**",
                    "/gateway/user/api/v1/auth/**",
                    "/user-service/api/v1/auth/**").permitAll();

            //ACTUATOR
            exchange.pathMatchers("/actuator/**").hasAnyAuthority(
                    "SCOPE_ROLE_ADMIN", "SCOPE_actuator:read", "SCOPE_actuator:post");

            //USER SERVICE
            exchange.pathMatchers("/gateway/api/v1/auth/profile/me").hasAuthority("SCOPE_user:profile");
            exchange.pathMatchers(HttpMethod.POST, "/gateway/USER/api/v1/users/upload/profile/**", "/gateway/user/api/v1/users/upload/profile/**", "/user-service/api/v1/users/upload/profile/**").hasAuthority("SCOPE_user:profile");
            exchange.pathMatchers(HttpMethod.GET, "/gateway/USER/**", "/gateway/user/**", "/user-service/**").hasAuthority("SCOPE_user:read");
            exchange.pathMatchers(HttpMethod.POST, "/gateway/USER/**", "/gateway/user/**", "/user-service/**").hasAuthority("SCOPE_user:write");
            exchange.pathMatchers(HttpMethod.PUT, "/gateway/USER/**", "/gateway/user/**", "/user-service/**").hasAuthority("SCOPE_user:update");
            exchange.pathMatchers(HttpMethod.PATCH, "/gateway/USER/**", "/gateway/user/**", "/user-service/**").hasAuthority("SCOPE_user:update");
            exchange.pathMatchers(HttpMethod.DELETE, "/gateway/USER/**", "/gateway/user/**", "/user-service/**").hasAuthority("SCOPE_user:delete");

            //CATEGORY SERVICE
            exchange.pathMatchers(HttpMethod.GET, "/gateway/CATEGORY/**", "/gateway/category/**", "/category-service/**").permitAll();
            exchange.pathMatchers(HttpMethod.POST, "/gateway/CATEGORY/**", "/gateway/category/**", "/category-service/**").hasAuthority("SCOPE_category:write");
            exchange.pathMatchers(HttpMethod.PATCH, "/gateway/CATEGORY/**", "/gateway/category/**", "/category-service/**").hasAuthority("SCOPE_category:update");
            exchange.pathMatchers(HttpMethod.DELETE, "/gateway/CATEGORY/**", "/gateway/category/**", "/category-service/**").hasAuthority("SCOPE_category:delete");

            //PRODUCT SERVICE
            exchange.pathMatchers(HttpMethod.GET, "/gateway/PRODUCT/**", "/gateway/product/**", "/product-service/**").hasAuthority("SCOPE_product:read");
            exchange.pathMatchers(HttpMethod.POST,"/gateway/PRODUCT/**", "/gateway/product/**", "/product-service/**").hasAuthority("SCOPE_product:write");
            exchange.pathMatchers(HttpMethod.PUT, "/gateway/PRODUCT/**", "/gateway/product/**", "/product-service/**").hasAuthority("SCOPE_product:update");
            exchange.pathMatchers(HttpMethod.PATCH, "/gateway/PRODUCT/**", "/gateway/product/**", "/product-service/**").hasAuthority("SCOPE_product:update");
            exchange.pathMatchers(HttpMethod.DELETE, "/gateway/PRODUCT/**", "/gateway/product/**", "/product-service/**").hasAuthority("SCOPE_product:delete");

            //ORDER SERVICE

            //PAYMENT SERVICE

            //FILE SERVICE
            exchange.pathMatchers(HttpMethod.GET, "/gateway/RESOURCE/file/**", "/gateway/resource/file/**", "/file-service/file/**").permitAll();
            exchange.pathMatchers(HttpMethod.GET, "/gateway/RESOURCE/api/v1/files/download/**", "/gateway/resource/api/v1/files/download/**", "/file-service/api/v1/files/download/**").permitAll();
            exchange.pathMatchers(HttpMethod.GET, "/gateway/RESOURCE/**", "/gateway/resource/**", "/file-service/**").hasAuthority("SCOPE_file:read");
            exchange.pathMatchers(HttpMethod.POST, "/gateway/RESOURCE/**", "/gateway/resource/**", "/file-service/**").hasAuthority("SCOPE_file:upload");
            exchange.pathMatchers(HttpMethod.DELETE, "/gateway/RESOURCE/**", "/gateway/resource/**", "/file-service/**").hasAuthority("SCOPE_file:delete");

            exchange.anyExchange().authenticated();
        });

        http.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(Customizer.withDefaults()));

        http.csrf(ServerHttpSecurity.CsrfSpec::disable);

        http.securityContextRepository(NoOpServerSecurityContextRepository.getInstance());

        return http.build();
    }

    //access token beans
    @Bean
    @Primary
    public JWKSource<SecurityContext> jwkSource() {
        JWK jwk = new RSAKey.Builder(keyUtil.getAccessTokenPublicKey())
                .privateKey(keyUtil.getAccessTokenPrivateKey())
                .keyID(UUID.randomUUID().toString())
                .build();

        var jwkSet = new JWKSet(jwk);
        return (jwkSelector, context) -> jwkSelector.select(jwkSet);
    }

    @Bean
    @Primary
    public ReactiveJwtDecoder jwtDecoder() {
        return NimbusReactiveJwtDecoder.withPublicKey(keyUtil.getAccessTokenPublicKey()).build();
    }

    @Bean
    @Primary
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    //refresh token beans
    @Bean("refreshTokenJwkSource")
    public JWKSource<SecurityContext> refreshTokenJwkSource() {
        JWK jwk = new RSAKey.Builder(keyUtil.getRefreshTokenPublicKey())
                .privateKey(keyUtil.getRefreshTokenPrivateKey())
                .keyID(UUID.randomUUID().toString())
                .build();

        var jwkSet = new JWKSet(jwk);
        return (jwkSelector, context) -> jwkSelector.select(jwkSet);
    }

    @Bean("jwtRefreshTokenDecoder")
    public ReactiveJwtDecoder jwtRefreshTokenDecoder() {
        return NimbusReactiveJwtDecoder.withPublicKey(keyUtil.getRefreshTokenPublicKey()).build();
    }

    @Bean("jwtRefreshTokenEncoder")
    public JwtEncoder jwtRefreshTokenEncoder(@Qualifier("refreshTokenJwkSource") JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

}
