package com.efree.product.api.external.userservice;

import com.efree.product.api.base.BaseApi;
import com.efree.product.api.external.userservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceRestClientConsumer {

    private final UserServiceRestClient userServiceRestClient;

    public UserDto loadUserByUuid(String userId) {
        BaseApi<UserDto> userDtoBaseApi = userServiceRestClient.loadUserByUuid(userId);
        return userDtoBaseApi.payload();
    }

}
