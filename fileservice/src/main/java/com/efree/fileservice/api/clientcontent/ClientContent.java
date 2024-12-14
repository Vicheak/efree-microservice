package com.efree.fileservice.api.clientcontent;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "client_contents")
public class ClientContent {

    @Id
    @Column(name = "client_content_id")
    String contentId;

    @Column(name = "content_type", unique = true, nullable = false)
    String contentType;

    @Column(name = "content_description_en", columnDefinition = "TEXT", nullable = false)
    String descriptionEn;

    @Column(name = "content_description_kh", columnDefinition = "TEXT")
    String descriptionKh;

    @Column(name = "content_reference")
    String reference;

    @Column(name = "is_used")
    Boolean isUsed;

}
