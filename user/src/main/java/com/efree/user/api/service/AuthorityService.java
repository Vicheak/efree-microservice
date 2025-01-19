package com.efree.user.api.service;

import com.efree.user.api.dto.response.AuthorityResponseDto;

import java.util.List;

public interface AuthorityService {

    /**
     * This method is used to load all authorities for client in order to set or remove permission of user
     * @return List<AuthorityResponseDto>
     */
    List<AuthorityResponseDto> loadAllAuthorities();

}
