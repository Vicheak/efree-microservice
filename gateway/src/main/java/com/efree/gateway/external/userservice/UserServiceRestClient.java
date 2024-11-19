package com.efree.gateway.external.userservice;

import com.efree.gateway.base.BaseApi;
import com.efree.gateway.dto.request.VerifyDto;
import com.efree.gateway.external.userservice.dto.AuthUserDto;
import com.efree.gateway.external.userservice.dto.TransactionUserDto;
import com.efree.gateway.external.userservice.dto.UpdateVerifiedCodeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service" /*url = "${rest.user-service.base-url}"*/)
public interface UserServiceRestClient {

    @PostMapping("/api/v1/users")
    BaseApi<String> createNewUser(@RequestBody TransactionUserDto transactionUserDto);

    @PostMapping("/api/v1/users/verifiedCode/{uuid}")
    Boolean updateVerifiedCodeByUuid(@PathVariable String uuid,
                                     @RequestBody UpdateVerifiedCodeDto updateVerifiedCodeDto);

    @PostMapping("/api/v1/users/verifyUser")
    Boolean verify(@RequestBody VerifyDto verifyDto);

    @GetMapping("/api/v1/users/authUser/{email}")
    AuthUserDto loadAuthUserByEmail(@PathVariable String email);

}
