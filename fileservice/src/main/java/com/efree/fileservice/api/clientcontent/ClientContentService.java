package com.efree.fileservice.api.clientcontent;

import com.efree.fileservice.api.clientcontent.web.ClientContentRequestDto;
import com.efree.fileservice.api.clientcontent.web.ClientContentResponseDto;

import java.util.List;

public interface ClientContentService {

    /**
     * This method is used to load all client contents posting from admin dashboard
     * @return List<ClientContentResponseDto>
     */
    List<ClientContentResponseDto> loadAllClientContents();

    /**
     * This method is used to post page content info
     * @param clientContentRequestDto is the request from client
     * @return ClientContentResponseDto
     */
    ClientContentResponseDto postPageInfo(ClientContentRequestDto clientContentRequestDto);

    /**
     * This method is used to post page content info
     * @param clientContentRequestDto is the request from client
     * @return ClientContentResponseDto
     */
    ClientContentResponseDto postAboutUsInfo(ClientContentRequestDto clientContentRequestDto);

    /**
     * This method is used to post page content info
     * @param clientContentRequestDto is the request from client
     * @return ClientContentResponseDto
     */
    ClientContentResponseDto postLocationInfo(ClientContentRequestDto clientContentRequestDto);

}
