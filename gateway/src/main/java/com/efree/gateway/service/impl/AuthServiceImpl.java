package com.efree.gateway.service.impl;

import com.efree.gateway.constant.AppGlobalConstant;
import com.efree.gateway.dto.request.GenerateTokenDto;
import com.efree.gateway.dto.request.LoginDto;
import com.efree.gateway.dto.request.RefreshTokenDto;
import com.efree.gateway.dto.response.AuthDto;
import com.efree.gateway.dto.response.DashboardMenuChildDto;
import com.efree.gateway.dto.response.DashboardMenuDto;
import com.efree.gateway.external.userservice.dto.AuthProfileUserDto;
import com.efree.gateway.external.userservice.UserServiceWebClient;
import com.efree.gateway.service.AuthService;
import com.efree.gateway.util.DashboardUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserServiceWebClient userServiceWebClient;
    private ReactiveAuthenticationManager authenticationManager;
    private ReactiveAuthenticationManager jwtReactiveAuthenticationManager;
    private final JwtEncoder jwtEncoder;
    private JwtEncoder jwtRefreshTokenEncoder;

    @Autowired
    public void setAuthenticationManager(@Qualifier("reactiveAuthenticationManager") ReactiveAuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setReactiveAuthenticationManager(@Qualifier("jwtReactiveAuthenticationManager") ReactiveAuthenticationManager jwtReactiveAuthenticationManager) {
        this.jwtReactiveAuthenticationManager = jwtReactiveAuthenticationManager;
    }

    @Autowired
    public void setJwtRefreshTokenEncoder(@Qualifier("jwtRefreshTokenEncoder") JwtEncoder jwtRefreshTokenEncoder) {
        this.jwtRefreshTokenEncoder = jwtRefreshTokenEncoder;
    }

    @Override
    public Mono<AuthDto> login(LoginDto loginDto) {
        //authenticate with email and passwords
        Authentication auth = new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password());

        return authenticationManager.authenticate(auth) //authenticate reactively
                .flatMap(authenticatedAuth -> {
                    String scope = authenticatedAuth.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(" "));

                    return Mono.just(AuthDto.builder()
                            .type(AppGlobalConstant.AUTH_TYPE)
                            .accessToken(generateAccessToken(GenerateTokenDto.builder()
                                    .auth(authenticatedAuth.getName())
                                    .scope(scope)
                                    .expiration(Instant.now().plus(24, ChronoUnit.HOURS))
                                    .build()))
                            .refreshToken(generateRefreshToken(GenerateTokenDto.builder()
                                    .auth(authenticatedAuth.getName())
                                    .scope(scope)
                                    .expiration(Instant.now().plus(30, ChronoUnit.DAYS))
                                    .build()))
                            .build());
                });
    }

    @Override
    public Mono<AuthDto> refreshToken(RefreshTokenDto refreshTokenDto) {
        //authenticate the refresh token using bearer token authentication
        Authentication auth = new BearerTokenAuthenticationToken(refreshTokenDto.refreshToken());

        //authenticate reactively without blocking
        return jwtReactiveAuthenticationManager.authenticate(auth)
                .flatMap(authenticated -> {
                    Jwt jwt = (Jwt) authenticated.getPrincipal();

                    String accessToken = generateAccessToken(GenerateTokenDto.builder()
                            .auth(jwt.getId())
                            .scope(jwt.getClaimAsString(AppGlobalConstant.JWT_CLAIM_AUTH))
                            .expiration(Instant.now().plus(24, ChronoUnit.HOURS))
                            .build());

                    String refreshToken = generateRefreshTokenCheckDuration(GenerateTokenDto.builder()
                            .auth(jwt.getId())
                            .scope(jwt.getClaimAsString(AppGlobalConstant.JWT_CLAIM_AUTH))
                            .previousToken(refreshTokenDto.refreshToken())
                            .expiration(Instant.now().plus(30, ChronoUnit.DAYS))
                            .duration(Duration.between(Instant.now(), jwt.getExpiresAt()))
                            .checkDurationNumber(7)
                            .build());

                    return Mono.just(AuthDto.builder()
                            .type(AppGlobalConstant.AUTH_TYPE)
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build());
                });
    }

    @Override
    public Mono<List<DashboardMenuDto>> loadDashboardMenu() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .cast(JwtAuthenticationToken.class)
                .map(JwtAuthenticationToken::getAuthorities)
                .map(authorities -> authorities.stream()
                        .map(GrantedAuthority::getAuthority) // Extract authority string
                        .collect(Collectors.toSet()))
                .flatMap(this::filterDashboardMenuByAuthorities)
                .map(this::sortDashboardMenuByKey)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "No authentication found in context")));
    }

    @Override
    public Mono<AuthProfileUserDto> loadUserProfile() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .cast(JwtAuthenticationToken.class)
                .map(JwtAuthenticationToken::getToken) //extract JWT object
                .flatMap(jwt -> userServiceWebClient.loadAuthUserProfile(jwt.getId()))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "No authentication found in context")));
    }

    private Mono<List<DashboardMenuDto>> filterDashboardMenuByAuthorities(Set<String> authorities) {
        // Filter the menus based on the user's authorities
        List<DashboardMenuDto> filteredMenus = DashboardUtil.MENU_MAP.values().stream()
                .map(menu -> {
                    // Filter children based on authorities
                    List<DashboardMenuChildDto> filteredChildren = menu.children().stream()
                            .filter(child -> authorities.contains(child.title()))
                            .collect(Collectors.toList());

                    // Include the menu only if it has any matching children
                    if (!filteredChildren.isEmpty()) {
                        return new DashboardMenuDto(menu.key(), menu.title(), menu.path(), filteredChildren);
                    }
                    return null;
                })
                .filter(Objects::nonNull) // Exclude null menus
                .collect(Collectors.toList());

        return Mono.just(filteredMenus);
    }

    private List<DashboardMenuDto> sortDashboardMenuByKey(List<DashboardMenuDto> menus) {
        return menus.stream()
                .sorted(Comparator.comparing(DashboardMenuDto::key)) // Sort by key
                .collect(Collectors.toList());
    }

    private String generateAccessToken(GenerateTokenDto generateTokenDto) {
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .id(generateTokenDto.auth())
                .issuer("public")
                .issuedAt(Instant.now())
                .expiresAt(generateTokenDto.expiration())
                .subject("Access Token")
                .audience(List.of("Public Client"))
                .claim(AppGlobalConstant.JWT_CLAIM_AUTH, generateTokenDto.scope())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }

    private String generateRefreshToken(GenerateTokenDto generateTokenDto) {
        JwtClaimsSet jwtRefreshTokenClaimsSet = JwtClaimsSet.builder()
                .id(generateTokenDto.auth())
                .issuer("public")
                .issuedAt(Instant.now())
                .expiresAt(generateTokenDto.expiration())
                .subject("Refresh Token")
                .audience(List.of("Public Client"))
                .claim(AppGlobalConstant.JWT_CLAIM_AUTH, generateTokenDto.scope())
                .build();
        return jwtRefreshTokenEncoder.encode(JwtEncoderParameters.from(jwtRefreshTokenClaimsSet)).getTokenValue();
    }

    private String generateRefreshTokenCheckDuration(GenerateTokenDto generateTokenDto) {
        if (generateTokenDto.duration().toDays() < generateTokenDto.checkDurationNumber()) {
            JwtClaimsSet jwtRefreshTokenClaimsSet = JwtClaimsSet.builder()
                    .id(generateTokenDto.auth())
                    .issuer("public")
                    .issuedAt(Instant.now())
                    .expiresAt(generateTokenDto.expiration())
                    .subject("Refresh Token")
                    .audience(List.of("Public Client"))
                    .claim(AppGlobalConstant.JWT_CLAIM_AUTH, generateTokenDto.scope())
                    .build();
            return jwtRefreshTokenEncoder.encode(JwtEncoderParameters.from(jwtRefreshTokenClaimsSet)).getTokenValue();
        }
        return generateTokenDto.previousToken();
    }

}
