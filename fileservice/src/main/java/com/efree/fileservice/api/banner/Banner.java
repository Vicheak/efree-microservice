package com.efree.fileservice.api.banner;

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
@Table(name = "banners")
public class Banner {

    @Id
    @Column(name = "banner_id")
    String bannerId;

    @Column(name = "image_name_base")
    String baseImageName;

    @Column(name = "image_name_mobile")
    String imageNameMobile;

    @Column(name = "banner_description_en")
    String descriptionEn;

    @Column(name = "banner_description_kh")
    String descriptionKh;

    @Column(name = "base_dimension")
    String baseDimension;

    @Column(name = "mobile_dimension")
    String mobileDimension;

    @Column(name = "reference_url")
    String referenceUrl;

    @Column(name = "is_used")
    Boolean isUsed;

}
