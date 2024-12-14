package com.efree.gateway.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Authority {

    USER_READ("SCOPE_user:read"),
    USER_WRITE("SCOPE_user:write"),
    USER_UPDATE("SCOPE_user:update"),
    USER_DELETE("SCOPE_user:delete"),
    USER_PROFILE("SCOPE_user:profile"),
    CATEGORY_READ("SCOPE_category:read"),
    CATEGORY_WRITE("SCOPE_category:write"),
    CATEGORY_UPDATE("SCOPE_category:update"),
    CATEGORY_DELETE("SCOPE_category:delete"),
    PRODUCT_READ("SCOPE_product:read"),
    PRODUCT_WRITE("SCOPE_product:write"),
    PRODUCT_UPDATE("SCOPE_product:update"),
    PRODUCT_DELETE("SCOPE_product:delete"),
    ORDER_ADDTOCART("SCOPE_order:toaddtocart"),
    ORDER_SAVE("SCOPE_order:tosave"),
    ORDER_MAKE("SCOPE_order:tomake"),
    ORDER_PAYMENT("SCOPE_order:topayment"),
    ORDER_INVOICEDOWNLOAD("SCOPE_order:invoicedownload"),
    ORDER_DETAIL("SCOPE_order:detail"),
    ORDER_DETAILDOWNLOAD("SCOPE_order:detaildownload"),
    ORDER_GETFROMHISTORY("SCOPE_order:togetfromhistory"),
    ORDER_VIEW("SCOPE_order:view"),
    ORDER_STATUS("SCOPE_order:status"),
    REPORT_PRODUCT("SCOPE_report:product"),
    REPORT_PRODUCTDOWNLOAD("SCOPE_report:productdownload"),
    PAYMENT_PAY("SCOPE_payments:topay"),
    FILE_READ("SCOPE_file:read"),
    FILE_UPLOAD("SCOPE_file:upload"),
    FILE_DELETE("SCOPE_file:delete"),
    FILE_RESOURCE("SCOPE_file:resource"),
    FILE_DOWNLOAD("SCOPE_file:download"),
    BANNER_READ("SCOPE_banner:read"),
    BANNER_WRITE("SCOPE_banner:write"),
    BANNER_UPDATE("SCOPE_banner:update"),
    BANNER_DELETE("SCOPE_banner:delete"),
    CONTENT_READ("SCOPE_content:read"),
    CONTENT_POST("SCOPE_content:post"),
    ACTUATOR_READ("SCOPE_actuator:read"),
    ACTUATOR_POST("SCOPE_actuator:post"),

    //FOR BASE ROLE
    ROLE_ADMIN("SCOPE_ROLE_ADMIN"),
    ROLE_CUSTOMER("SCOPE_ROLE_CUSTOMER");

    final String authority;

}
