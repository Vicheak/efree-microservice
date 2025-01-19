package com.efree.order.api.external.userservice;

import com.efree.order.api.base.BaseApi;
import com.efree.order.api.external.userservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${rest.user-service.base-url}")
public interface UserServiceRestClient {

    @GetMapping("/api/v1/users/{uuid}")
    BaseApi<UserDto> loadUserByUuid(@PathVariable String uuid);

}
