package com.efree.fileservice.api.clientcontent.web;

public record ClientContentResponseDto(String contentId,
                                       String contentType,
                                       String descriptionEn,
                                       String descriptionKh,
                                       String reference,
                                       Boolean isUsed) {
}
