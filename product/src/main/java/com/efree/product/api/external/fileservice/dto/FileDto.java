package com.efree.product.api.external.fileservice.dto;

import lombok.Builder;

@Builder
public record FileDto(String name,
                      String fileBaseUri,
                      String downloadUri,
                      Long size,
                      String extension) {
}
