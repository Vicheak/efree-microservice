package com.efree.gateway.dto.response;

import java.util.List;

public record DashboardMenuDto(String key,
                               String title,
                               String path,
                               List<DashboardMenuChildDto> children) {
}
