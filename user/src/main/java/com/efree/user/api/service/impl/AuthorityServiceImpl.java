package com.efree.user.api.service.impl;

import com.efree.user.api.dto.mapper.AuthMapper;
import com.efree.user.api.dto.mapper.AuthorityMapper;
import com.efree.user.api.dto.response.AuthorityResponseDto;
import com.efree.user.api.entity.Authority;
import com.efree.user.api.repository.AuthorityRepository;
import com.efree.user.api.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository authorityRepository;
    private final AuthorityMapper authorityMapper;

    @Override
    public List<AuthorityResponseDto> loadAllAuthorities() {
        List<Authority> authorities = authorityRepository.findAll();
        authorities = authorities.stream()
                .filter(authority -> !(authority.getName().equals("ROLE_ADMIN") ||
                        authority.getName().equals("ROLE_CUSTOMER")))
                .toList();
        return authorityMapper.fromAuthorityToAuthorityResponseDto(authorities);
    }

}
