package com.efree.category.api.entity;

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
@Table(name = "categories")
public class Category {

    @Id
    @Column(name = "category_id")
    String id;

    @Column(name = "category_name_en")
    String nameEn;

    @Column(name = "category_name_kh")
    String nameKh;

    @Column(name = "category_description_en")
    String descriptionEn;

    @Column(name = "category_description_kh")
    String descriptionKh;

    @Column(name = "category_image_url")
    String imageUrl;

    @Column(name = "category_is_active")
    Boolean isActive;

}
