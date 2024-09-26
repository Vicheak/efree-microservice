package com.efree.fileservice.api.file.web;

import lombok.Builder;

@Builder
public record FileDto(String name,
                      String fileBaseUri,
                      String downloadUri,
                      Long size,
                      String extension) {
}
