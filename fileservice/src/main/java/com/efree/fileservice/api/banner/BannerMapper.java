package com.efree.fileservice.api.banner;

import com.efree.fileservice.api.banner.web.BannerRequestDto;
import com.efree.fileservice.api.banner.web.BannerResponseDto;
import com.efree.fileservice.util.ValueInjectUtil;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class BannerMapper {

    protected ValueInjectUtil valueInjectUtil;

    @Autowired
    public void setValueInjectUtil(ValueInjectUtil valueInjectUtil) {
        this.valueInjectUtil = valueInjectUtil;
    }

    public abstract Banner mapFromBannerRequestDtoToBanner(BannerRequestDto bannerRequestDto);

    @Mapping(target = "baseImageUrl", expression = "java(valueInjectUtil.getImageUri(banner.getBaseImageName()))")
    @Mapping(target = "baseImageDownloadUrl", expression = "java(valueInjectUtil.getDownloadUri(banner.getBaseImageName()))")
    @Mapping(target = "mobileImageUrl", expression = "java(valueInjectUtil.getImageUri(banner.getImageNameMobile()))")
    @Mapping(target = "mobileImageDownloadUrl", expression = "java(valueInjectUtil.getDownloadUri(banner.getImageNameMobile()))")
    public abstract BannerResponseDto mapFromBannerToBannerResponseDto(Banner banner);

    public abstract List<BannerResponseDto> mapFromBannerToBannerResponseDto(List<Banner> banners);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void mapFromBannerRequestDtoToBanner(@MappingTarget Banner banner, BannerRequestDto bannerRequestDto);

}
