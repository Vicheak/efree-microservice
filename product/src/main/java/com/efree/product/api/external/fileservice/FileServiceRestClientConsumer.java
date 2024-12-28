package com.efree.product.api.external.fileservice;

import com.efree.product.api.external.fileservice.dto.FileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FileServiceRestClientConsumer {

    private final FileServiceRestClient fileServiceRestClient;

    @Value("${file.supported-extension}")
    private String supportedFileExtension;
    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileRequestSize;

    public FileDto singleUpload(MultipartFile fileRequest) {
        validateFileRequest(fileRequest);
        return fileServiceRestClient.singleUpload(fileRequest);
    }

    public List<FileDto> multipleUpload(List<MultipartFile> fileRequests) {
        fileRequests.forEach(this::validateFileRequest);
        return fileServiceRestClient.multipleUpload(fileRequests);
    }

    private void validateFileRequest(MultipartFile fileRequest) {
        //check if file is empty
        if (fileRequest.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File request is empty! try again");

        //check extension file
        String extension = this.getExtension(Objects.requireNonNull(fileRequest.getOriginalFilename()));
        String[] supportedExtensions = supportedFileExtension.split(",");
        if (!Arrays.stream(supportedExtensions).toList().contains(extension)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unsupported file extensions, supported extensions : " + Arrays.toString(supportedExtensions));
        }

        //check image size
        double sizeInBytes = fileRequest.getSize();
        double sizeInMB = sizeInBytes / (1024.0 * 1024.0);
        double maxFileRequestSizeInMB = Double.parseDouble(maxFileRequestSize.replace("MB", "").trim());
        if (sizeInMB > maxFileRequestSizeInMB) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File request is exceeded the max file request size! try again");
        }
    }

    private String getExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastDotIndex + 1);
    }

}
