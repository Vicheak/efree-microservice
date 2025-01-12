package com.efree.gateway.external.userservice;

import com.efree.gateway.external.userservice.dto.AuthProfileUserDto;
import com.efree.gateway.external.userservice.dto.AuthUserDto;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceWebClient {

    private final WebClient webClient;

    @Value("${rest.user-service.base-url}")
    private String restServiceUrl;
    @Value("${rest.user-service.connection-timeout}")
    private String connectionTimeout;
    @Value("${rest.user-service.response-timeout}")
    private String responseTimeout;
    @Value("${rest.user-service.read-timeout}")
    private String readTimeout;
    @Value("${rest.user-service.write-timeout}")
    private String writeTimeout;

    public UserServiceWebClient(WebClient.Builder webClientBuilder) {
        // Configure Netty HttpClient with custom DNS resolver
        HttpClient httpClient = HttpClient.create()
                // aligns with Kubernetes DNS
                .resolver(DefaultAddressResolverGroup.INSTANCE) // Use Netty's default address resolver
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) // Connection timeout
                .responseTimeout(Duration.ofSeconds(5))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS)) // Read timeout
                        .addHandlerLast(new WriteTimeoutHandler(10, TimeUnit.SECONDS)) // Write timeout
                );

        // Create WebClient with the customized HttpClient
        this.webClient = webClientBuilder
//                .baseUrl(restServiceUrl + "/api/v1/users")
                .baseUrl("http://localhost:8100/api/v1/users")
//                .baseUrl("http://user-service-svc.microservice.svc.prod:8100/api/v1/users")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
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
