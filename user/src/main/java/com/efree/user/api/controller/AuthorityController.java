package com.efree.user.api.controller;

import com.efree.user.api.base.BaseApi;
import com.efree.user.api.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/authorities")
@RequiredArgsConstructor
public class AuthorityController {

    private final AuthorityService authorityService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public BaseApi<?> loadAllAuthorities(){
        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.NO_CONTENT.value())
                .message("User permission updated successfully!")
                .timestamp(LocalDateTime.now())
                .payload(authorityService.loadAllAuthorities())
                .build();
    }

}
