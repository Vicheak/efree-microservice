package com.efree.gateway.service.impl;

import com.efree.gateway.base.BaseApi;
import com.efree.gateway.constant.AppGlobalConstant;
import com.efree.gateway.dto.mapper.AuthMapper;
import com.efree.gateway.dto.request.*;
import com.efree.gateway.dto.response.AuthDto;
import com.efree.gateway.external.mail.MailServiceClient;
import com.efree.gateway.external.userservice.UserServiceRestClientConsumer;
import com.efree.gateway.external.userservice.dto.TransactionUserDto;
import com.efree.gateway.external.userservice.dto.UpdateVerifiedCodeDto;
import com.efree.gateway.service.AuthService;
import com.efree.gateway.util.RandomUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MailServiceClient mailServiceClient;
    private final UserServiceRestClientConsumer userServiceRestClientConsumer;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
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

    @Value("${mail.subject}")
    private String mailSubject;

    @Override
    public Mono<AuthDto> login(LoginDto loginDto) {
        //authenticate with email and passwords
        Authentication auth = new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password());

        return authenticationManager.authenticate(auth)  //authenticate reactively
                .flatMap(authenticatedAuth -> {
                    String scope = authenticatedAuth.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(" "));

                    return Mono.just(AuthDto.builder()
                            .type(AppGlobalConstant.AUTH_TYPE)
                            .accessToken(generateAccessToken(GenerateTokenDto.builder()
                                    .auth(authenticatedAuth.getName())
                                    .scope(scope)
                                    .expiration(Instant.now().plus(1, ChronoUnit.HOURS))
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
        //Authenticate the refresh token using bearer token authentication
        Authentication auth = new BearerTokenAuthenticationToken(refreshTokenDto.refreshToken());

        //Authenticate reactively without blocking
        return jwtReactiveAuthenticationManager.authenticate(auth)
                .flatMap(authenticated -> {
                    Jwt jwt = (Jwt) authenticated.getPrincipal();

                    String accessToken = generateAccessToken(GenerateTokenDto.builder()
                            .auth(jwt.getId())
                            .scope(jwt.getClaimAsString(AppGlobalConstant.JWT_CLAIM_AUTH))
                            .expiration(Instant.now().plus(1, ChronoUnit.HOURS))
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

    @Override
    public void register(RegisterDto registerDto) throws MessagingException {
        //map from registerDto to transactionUserDto
        TransactionUserDto transactionUserDto =
                authMapper.fromRegisterDtoToTransactionUserDto(registerDto);
        //encrypt the raw password
        transactionUserDto.setPassword(passwordEncoder.encode(transactionUserDto.getPassword()));

        BaseApi<String> baseUserServiceResponse =
                userServiceRestClientConsumer.createNewUser(transactionUserDto);
        if (baseUserServiceResponse.isSuccess()) {
            //update verification code
            String userUuid = baseUserServiceResponse.message();
            //generate six random digit for verification code
            String verifiedCode = RandomUtil.getRandomNumber();
            UpdateVerifiedCodeDto updateVerifiedCodeDto = new UpdateVerifiedCodeDto(transactionUserDto.getEmail(), verifiedCode);
            boolean isStatusSuccess =
                    userServiceRestClientConsumer.updateVerifiedCodeUser(userUuid, updateVerifiedCodeDto);

            //send verified code to user's email asynchronously
            if (isStatusSuccess) {
                mailServiceClient.buildAndSendMailAsync(transactionUserDto.getEmail(), mailSubject, verifiedCode);
            }
        }
    }

    @Override
    public void verify(VerifyDto verifyDto) {
        boolean isVerifiedSuccess = userServiceRestClientConsumer.verify(verifyDto);
        if (!isVerifiedSuccess) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Email verification has been failed!");
        }
    }

}
