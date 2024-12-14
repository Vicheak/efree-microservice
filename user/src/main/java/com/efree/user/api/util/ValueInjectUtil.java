package com.efree.user.api.util;

import com.efree.user.api.entity.UserAuthority;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ValueInjectUtil {

    @Value("${file.base-uri}")
    private String baseUri;
    @Value("${file.download-uri}")
    private String downloadUri;

    //check if the image is already set and then pass the baseUri + name
    public String getImageUri(String name) {
        return Objects.nonNull(name) ? baseUri + name : null;
    }

    public String getDownloadUri(String name) {
        return Objects.nonNull(name) ? downloadUri + name : null;
    }

    public List<String> buildAuthorityResponse(List<UserAuthority> userAuthorities){
        List<String> authorityResponse = new ArrayList<>();
        userAuthorities.forEach(userAuthority -> authorityResponse.add(userAuthority.getAuthority().getName()));
        return authorityResponse;
    }

}
