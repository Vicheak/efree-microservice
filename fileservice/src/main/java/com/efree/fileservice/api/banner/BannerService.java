package com.efree.fileservice.api.banner;

import com.efree.fileservice.api.banner.web.BannerRequestDto;
import com.efree.fileservice.api.banner.web.BannerResponseDto;
import com.efree.fileservice.api.file.web.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BannerService {

    /**
     * This method is used to load all banner resources from database
     * @return List<BannerResponseDto>
     */
    List<BannerResponseDto> loadAllBanners();

    /**
     * This method is used to load specific banner resource from database
     * @param id is the path parameter from client
     * @return BannerResponseDto
     */
    BannerResponseDto loadBannerById(String id);

    /**
     * This method is used to create new banner resource
     * @param bannerRequestDto is the request from client
     */
    void createNewBanner(BannerRequestDto bannerRequestDto);

    /**
     * This method is used to update banner resource by id
     * @param id is the path parameter from client
     * @param bannerRequestDto is the request from client
     */
    void updateBannerById(String id, BannerRequestDto bannerRequestDto);

    /**
     * This method is used to delete banner resource by id
     * @param id is the path parameter from client
     */
    void deleteBannerById(String id);

    /**
     * This method is used to upload multiple image resources to server
     * @param uuid is the path parameter from client
     * @param files is the request from client
     * @return List<FileDto>
     */
    List<FileDto> uploadBannerImage(String uuid, List<MultipartFile> files) throws IOException;

}
