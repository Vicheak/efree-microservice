package com.efree.fileservice.api.file;

import com.efree.fileservice.api.file.web.FileDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {

    /**
     * This method is used to download file resource from file server
     * @param name is the path requested from client
     * @return Resource
     */
    Resource download(String name);

    /**
     * This method is used to upload single file resource to server
     * @param file is the multipart request from client
     * @return FileDto
     */
    FileDto singleUpload(MultipartFile file);

    /**
     * This method is used to upload multiple file resources to server
     * @param files is the multipart requests from client
     * @return List<FileDto>
     */
    List<FileDto> multipleUpload(List<MultipartFile> files);

    /**
     * This method is used to load file resource from server
     * @param name is the path requested from client
     * @return FileDto
     * @throws IOException exception from server
     */
    FileDto findByName(String name) throws IOException;

    /**
     * This method is used to load all file resources from server
     * @return List<FileDto>
     */
    List<FileDto> findAll();

    /**
     * This method is used to remove file resource from server by specific path name
     * @param name is the path requested from client
     */
    void deleteByName(String name);

    /**
     * This method is used to clear all file resources from server
     */
    void deleteAll();

    /**
     * This method is used to update file resource in server by specific path name and requested file
     * @param name is the path requested from client
     * @param file is the multipart request from client
     */
    void updateByName(String name, MultipartFile file);

}
