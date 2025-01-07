package com.efree.order.api.external.userservice;

import com.efree.order.api.base.BaseApi;
import com.efree.order.api.external.userservice.dto.UserDto;
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
