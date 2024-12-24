package com.efree.product.api.external.fileservice;


import com.efree.product.api.external.fileservice.dto.FileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "file-service") // LB will route to service
public interface FileServiceRestClient {

    @PostMapping(value = "/api/v1/files/single", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    FileDto singleUpload(@RequestPart MultipartFile file);

}
