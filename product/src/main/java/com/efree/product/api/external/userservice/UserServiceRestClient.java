package com.efree.product.api.external.userservice;

import com.efree.product.api.base.BaseApi;
import com.efree.product.api.external.userservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserServiceRestClient {

    @GetMapping("/api/v1/users/{uuid}")
    BaseApi<UserDto> loadUserByUuid(@PathVariable String uuid);

}
