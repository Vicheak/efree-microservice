package com.efree.category.api.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "categories")
public class Category {

    @MongoId
    @Field("category_id")
    Long id;

    @Field("category_name")
    String name;

    @Field("category_description")
    String description;

    @Field("category_image_url")
    String imageUrl;

    @Field("category_is_active")
    Boolean isActive;

}
