package com.efree.gateway.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DashboardMenu {

    USER("user"),
    CATEGORY("category"),
    PRODUCT("product"),
    ORDER("order"),
    REPORT("report"),
    FILE("file"),
    BANNER("banner"),
    CONTENT("content"),
    DEVELOPER("developer");

    final String menuTitle;

}
