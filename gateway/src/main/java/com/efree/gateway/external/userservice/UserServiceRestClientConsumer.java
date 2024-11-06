package com.efree.gateway.external.userservice;
import com.efree.gateway.base.BaseApi;
import com.efree.gateway.dto.request.VerifyDto;
import com.efree.gateway.external.userservice.dto.TransactionUserDto;
import com.efree.gateway.external.userservice.dto.UpdateVerifiedCodeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceRestClientConsumer {

    private final UserServiceRestClient userServiceRestClient;

    public BaseApi<String> createNewUser(TransactionUserDto transactionUserDto){
        //validate role in service user
        //@TODO

        //set default role to CUSTOMER
        transactionUserDto.setRoleIds(Set.of(3));
        return userServiceRestClient.createNewUser(transactionUserDto);
    }

    public Boolean updateVerifiedCodeUser(String uuid, UpdateVerifiedCodeDto updateVerifiedCodeDto){
        return userServiceRestClient.updateVerifiedCodeByUuid(uuid, updateVerifiedCodeDto);
    }

    public Boolean verify(VerifyDto verifyDto){
        return userServiceRestClient.verify(verifyDto);
    }

}
