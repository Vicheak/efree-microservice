package com.efree.category.api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

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

    @Column(name = "category_name_en", unique = true, nullable = false)
    String nameEn;

    @Column(name = "category_name_kh", unique = true)
    String nameKh;

    @Column(name = "category_description_en")
    String descriptionEn;

    @Column(name = "category_description_kh")
    String descriptionKh;

    @Column(name = "category_image_url")
    String imageUrl;

    @Column(name = "category_is_active", nullable = false)
    Boolean isActive;

    @OneToMany(mappedBy = "category")
    List<Product> products;

}
