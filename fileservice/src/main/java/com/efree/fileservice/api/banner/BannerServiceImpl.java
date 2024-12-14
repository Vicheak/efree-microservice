package com.efree.fileservice.api.banner;

import com.efree.fileservice.api.banner.web.BannerRequestDto;
import com.efree.fileservice.api.banner.web.BannerResponseDto;
import com.efree.fileservice.api.file.FileService;
import com.efree.fileservice.api.file.web.FileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.efree.fileservice.api.banner.BannerConstant.*;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;
    private final FileService fileService;

    @Value("${file.supported-extension}")
    private String supportedFileExtension;

    @Override
    public List<BannerResponseDto> loadAllBanners() {
        return bannerMapper.mapFromBannerToBannerResponseDto(bannerRepository.findAll());
    }

    @Override
    public BannerResponseDto loadBannerById(String id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Banner with id, %s has not been found in the system!"
                                        .formatted(id))
                );

        return bannerMapper.mapFromBannerToBannerResponseDto(banner);
    }

    @Transactional
    @Override
    public void createNewBanner(BannerRequestDto bannerRequestDto) {
        Banner newBanner = bannerMapper.mapFromBannerRequestDtoToBanner(bannerRequestDto);
        newBanner.setBannerId(UUID.randomUUID().toString());
        newBanner.setIsUsed(Objects.nonNull(bannerRequestDto.isUsed()) ? bannerRequestDto.isUsed() : true);
        bannerRepository.save(newBanner);
    }

    @Transactional
    @Override
    public void updateBannerById(String id, BannerRequestDto bannerRequestDto) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Banner with id, %s has not been found in the system!"
                                        .formatted(id))
                );

        bannerMapper.mapFromBannerRequestDtoToBanner(banner, bannerRequestDto);
        banner.setIsUsed(Objects.nonNull(bannerRequestDto.isUsed()) ? bannerRequestDto.isUsed() : true);
        bannerRepository.save(banner);
    }

    @Transactional
    @Override
    public void deleteBannerById(String id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Banner with id, %s has not been found in the system!"
                                        .formatted(id))
                );

        bannerRepository.delete(banner);
    }

    @Transactional
    @Override
    public List<FileDto> uploadBannerImage(String uuid, List<MultipartFile> files) throws IOException {
        //check base image and mobile image
        if (files.size() != 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Two images are required: base image and mobile image.");
        }

        //check extension file
        String[] supportedExtensions = supportedFileExtension.split(",");
        files.forEach(fileRequest -> {
            String extension = BannerUtil.getBannerFileExtension(Objects.requireNonNull(fileRequest.getOriginalFilename()));
            if (!Arrays.stream(supportedExtensions).toList().contains(extension)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Unsupported file extensions, supported extensions : " + Arrays.toString(supportedExtensions));
            }
        });

        //check size of both images
        MultipartFile baseImageFile = files.get(0);
        MultipartFile mobileImageFile = files.get(1);

        BannerUtil.validateImageDimension(baseImageFile, BASE_IMAGE_WIDTH, BASE_IMAGE_HEIGHT, "Base image");
        BannerUtil.validateImageDimension(mobileImageFile, MOBILE_IMAGE_WIDTH, MOBILE_IMAGE_HEIGHT, "Mobile image");

        Banner banner = bannerRepository.findById(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Banner with id, %s has not been found in the system!"
                                        .formatted(uuid))
                );

        List<FileDto> fileDtoList = fileService.multipleUpload(files);
        FileDto fileBaseImageDto = fileDtoList.getFirst();
        FileDto fileMobileImageDto = fileDtoList.getLast();
        banner.setBaseImageName(fileBaseImageDto.name());
        banner.setImageNameMobile(fileMobileImageDto.name());
        banner.setBaseDimension(BASE_IMAGE_WIDTH + "x" + BASE_IMAGE_HEIGHT);
        banner.setMobileDimension(MOBILE_IMAGE_WIDTH + "x" + MOBILE_IMAGE_HEIGHT);
        bannerRepository.save(banner);

        return fileDtoList;
    }

}
