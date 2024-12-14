package com.efree.user.api.service;

import com.efree.user.api.dto.response.AuthorityResponseDto;

import java.util.List;

public interface AuthorityService {

    List<AuthorityResponseDto> loadAllAuthorities();

}
