package com.efree.gateway.util;

import com.efree.gateway.dto.response.DashboardMenuChildDto;
import com.efree.gateway.dto.response.DashboardMenuDto;

import java.util.List;
import java.util.Map;

import static com.efree.gateway.util.Authority.*;
import static com.efree.gateway.util.DashboardMenu.*;

public class DashboardUtil {

    //FULL AUTHORITY FOR EACH MENU
    public static final Map<String, DashboardMenuDto> MENU_MAP = Map.of(
            USER.getMenuTitle(), new DashboardMenuDto("M001", USER.getMenuTitle(), "/gateway/USER/**", List.of(
                    new DashboardMenuChildDto("S001", USER_READ.getAuthority(), "/gateway/USER/**"),
                    new DashboardMenuChildDto("S002", USER_WRITE.getAuthority(), "/gateway/USER/**"),
                    new DashboardMenuChildDto("S003", USER_UPDATE.getAuthority(), "/gateway/USER/**"),
                    new DashboardMenuChildDto("S004", USER_DELETE.getAuthority(), "/gateway/USER/**")
            )),
            CATEGORY.getMenuTitle(), new DashboardMenuDto("M002", CATEGORY.getMenuTitle(), "/gateway/CATEGORY/**", List.of(
                    new DashboardMenuChildDto("S001", CATEGORY_READ.getAuthority(), "/gateway/CATEGORY/**"),
                    new DashboardMenuChildDto("S002", CATEGORY_WRITE.getAuthority(), "/gateway/CATEGORY/**"),
                    new DashboardMenuChildDto("S003", CATEGORY_UPDATE.getAuthority(), "/gateway/CATEGORY/**"),
                    new DashboardMenuChildDto("S004", CATEGORY_DELETE.getAuthority(), "/gateway/CATEGORY/**")
            )),
            PRODUCT.getMenuTitle(), new DashboardMenuDto("M003", PRODUCT.getMenuTitle(), "/gateway/PRODUCT/**", List.of(
                    new DashboardMenuChildDto("S001", PRODUCT_READ.getAuthority(), "/gateway/PRODUCT/**"),
                    new DashboardMenuChildDto("S002", PRODUCT_WRITE.getAuthority(), "/gateway/PRODUCT/**"),
                    new DashboardMenuChildDto("S003", PRODUCT_UPDATE.getAuthority(), "/gateway/PRODUCT/**"),
                    new DashboardMenuChildDto("S004", PRODUCT_DELETE.getAuthority(), "/gateway/PRODUCT/**")
            )),
            ORDER.getMenuTitle(), new DashboardMenuDto("M004", ORDER.getMenuTitle(), "/gateway/ORDER/**", List.of(
                    new DashboardMenuChildDto("S001", ORDER_VIEW.getAuthority(), "/gateway/ORDER/**"),
                    new DashboardMenuChildDto("S002", ORDER_STATUS.getAuthority(), "/gateway/ORDER/**")
            )),
            REPORT.getMenuTitle(), new DashboardMenuDto("M005", REPORT.getMenuTitle(), "/gateway/ORDER/**", List.of(
                    new DashboardMenuChildDto("S001", REPORT_PRODUCT.getAuthority(), "/gateway/ORDER/**"),
                    new DashboardMenuChildDto("S002", REPORT_PRODUCTDOWNLOAD.getAuthority(), "/gateway/ORDER/**")
            )),
            FILE.getMenuTitle(), new DashboardMenuDto("M006", FILE.getMenuTitle(), "/gateway/RESOURCE/**", List.of(
                    new DashboardMenuChildDto("S001", FILE_READ.getAuthority(), "/gateway/RESOURCE/**"),
                    new DashboardMenuChildDto("S002", FILE_UPLOAD.getAuthority(), "/gateway/RESOURCE/**"),
                    new DashboardMenuChildDto("S003", FILE_DELETE.getAuthority(), "/gateway/RESOURCE/**")
            )),
            BANNER.getMenuTitle(), new DashboardMenuDto("M007", BANNER.getMenuTitle(), "/gateway/RESOURCE/**", List.of(
                    new DashboardMenuChildDto("S001", BANNER_READ.getAuthority(), "/gateway/RESOURCE/**"),
                    new DashboardMenuChildDto("S002", BANNER_WRITE.getAuthority(), "/gateway/RESOURCE/**"),
                    new DashboardMenuChildDto("S003", BANNER_UPDATE.getAuthority(), "/gateway/RESOURCE/**"),
                    new DashboardMenuChildDto("S004", BANNER_DELETE.getAuthority(), "/gateway/RESOURCE/**")
            )),
            CONTENT.getMenuTitle(), new DashboardMenuDto("M008", CONTENT.getMenuTitle(), "/gateway/RESOURCE/**", List.of(
                    new DashboardMenuChildDto("S001", CONTENT_READ.getAuthority(), "/gateway/RESOURCE/**"),
                    new DashboardMenuChildDto("S002", CONTENT_POST.getAuthority(), "/gateway/RESOURCE/**")
            )),
            DEVELOPER.getMenuTitle(), new DashboardMenuDto("M009", DEVELOPER.getMenuTitle(), "/gateway/**", List.of(
                    new DashboardMenuChildDto("S001", ACTUATOR_READ.getAuthority(), "/gateway/**"),
                    new DashboardMenuChildDto("S002", ACTUATOR_POST.getAuthority(), "/gateway/**")
            ))
    );

}
