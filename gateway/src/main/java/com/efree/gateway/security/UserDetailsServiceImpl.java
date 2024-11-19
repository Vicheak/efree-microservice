package com.efree.gateway.security;

import com.efree.gateway.external.userservice.UserServiceWebClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final UserServiceWebClient userServiceWebClient;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userServiceWebClient.loadAuthUserByEmail(username)
                .switchIfEmpty(Mono.defer(() -> {
                    log.error("Email has not been authenticated : {}", username);
                    return Mono.error(new UsernameNotFoundException("Email has not been authenticated!"));
                }))
                .map(authUser -> {
                    CustomUserDetails customUserDetails = new CustomUserDetails();
                    customUserDetails.setUser(authUser);
                    return customUserDetails;
                });
    }

}
