package com.efree.fileservice.api.banner.web;

import com.efree.fileservice.api.banner.BannerService;
import com.efree.fileservice.api.file.web.FileDto;
import com.efree.fileservice.base.BaseApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public BaseApi<?> loadAllBanners() {

        List<BannerResponseDto> bannerResponseDtoList = bannerService.loadAllBanners();

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("All banners loaded successfully!")
                .timestamp(LocalDateTime.now())
                .payload(bannerResponseDtoList)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public BaseApi<?> loadBannerById(@PathVariable String id) {

        BannerResponseDto bannerResponseDto = bannerService.loadBannerById(id);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("Banner with id, %s loaded successfully!".formatted(id))
                .timestamp(LocalDateTime.now())
                .payload(bannerResponseDto)
                .build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BaseApi<?> createNewBanner(@RequestBody @Valid BannerRequestDto bannerRequestDto) {

        bannerService.createNewBanner(bannerRequestDto);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.CREATED.value())
                .message("A new banner has been created successfully!")
                .timestamp(LocalDateTime.now())
                .payload(Map.of("message", "Payload has no content!"))
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public BaseApi<?> updateBannerById(@PathVariable String id,
                                       @RequestBody @Valid BannerRequestDto bannerRequestDto) {

        bannerService.updateBannerById(id, bannerRequestDto);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("A banner has been updated successfully!")
                .timestamp(LocalDateTime.now())
                .payload(Map.of("message", "Payload has no content!"))
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public BaseApi<?> deleteBannerById(@PathVariable String id) {

        bannerService.deleteBannerById(id);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.NO_CONTENT.value())
                .message("A banner has been deleted successfully!")
                .timestamp(LocalDateTime.now())
                .payload(Map.of("message", "Payload has no content!"))
                .build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/upload/multiple/{uuid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseApi<?> uploadBannerImage(@PathVariable String uuid,
                                        @RequestPart List<MultipartFile> files) throws IOException {

        List<FileDto> fileDtoList = bannerService.uploadBannerImage(uuid, files);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.CREATED.value())
                .message("A banner's images has been uploaded successfully!")
                .timestamp(LocalDateTime.now())
                .payload(fileDtoList)
                .build();
    }

}
