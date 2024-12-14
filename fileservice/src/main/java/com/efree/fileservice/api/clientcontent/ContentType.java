package com.efree.fileservice.api.clientcontent;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ContentType {

    INFO("The client page information"),
    ABOUTUS("The client about us information"),
    LOCATION("The client location section information");

    final String description;

}
