package com.efree.gateway.external.userservice;

import com.efree.gateway.dto.response.AuthProfileUserDto;
import com.efree.gateway.external.userservice.dto.AuthUserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserServiceWebClient {

    private final WebClient webClient;

    @Value("${rest.user-service.base-url}")
    private String restServiceUrl;

    public UserServiceWebClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://localhost:8100/api/v1/users")
                .build();
    }

    public Mono<AuthProfileUserDto> loadAuthUserProfile(String email) {
        return webClient.get()
                .uri("/me/{email}", email)
                .retrieve()
                .bodyToMono(AuthProfileUserDto.class);
    }

    public Mono<AuthUserDto> loadAuthUserByEmail(String email) {
        return webClient.get()
                .uri("/authUser/{email}", email)
                .retrieve()
                .bodyToMono(AuthUserDto.class);
    }

}
