package com.efree.fileservice.api.clientcontent;

import com.efree.fileservice.api.clientcontent.web.ClientContentRequestDto;
import com.efree.fileservice.api.clientcontent.web.ClientContentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientContentServiceImpl implements ClientContentService {

    private final ClientContentRepository clientContentRepository;
    private final ClientContentMapper clientContentMapper;

    @Override
    public List<ClientContentResponseDto> loadAllClientContents() {
        List<ClientContent> clientContents = new ArrayList<>();

        ClientContent infoContent = clientContentRepository.findByContentType(ContentType.INFO.name())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Content of type " + ContentType.INFO.name() + " not found"));
        clientContents.add(infoContent);

        ClientContent aboutUsContent = clientContentRepository.findByContentType(ContentType.ABOUTUS.name())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Content of type " + ContentType.ABOUTUS.name() + " not found"));
        clientContents.add(aboutUsContent);

        ClientContent locationContent = clientContentRepository.findByContentType(ContentType.LOCATION.name())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Content of type " + ContentType.LOCATION.name() + " not found"));
        clientContents.add(locationContent);

        return clientContentMapper.mapFromClientContentToClientContentResponseDto(clientContents);
    }

    @Transactional
    @Override
    public ClientContentResponseDto postPageInfo(ClientContentRequestDto clientContentRequestDto) {
        ClientContent infoContent = clientContentRepository.findByContentType(ContentType.INFO.name())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Content of type " + ContentType.INFO.name() + " not found at server"));
        clientContentMapper.mapFromClientContentRequestDtoToClientContent(infoContent, clientContentRequestDto);
        clientContentRepository.save(infoContent);
        return clientContentMapper.mapFromClientContentToClientContentResponseDto(infoContent);
    }

    @Transactional
    @Override
    public ClientContentResponseDto postAboutUsInfo(ClientContentRequestDto clientContentRequestDto) {
        ClientContent infoContent = clientContentRepository.findByContentType(ContentType.ABOUTUS.name())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Content of type " + ContentType.ABOUTUS.name() + " not found at server"));
        clientContentMapper.mapFromClientContentRequestDtoToClientContent(infoContent, clientContentRequestDto);
        clientContentRepository.save(infoContent);
        return clientContentMapper.mapFromClientContentToClientContentResponseDto(infoContent);
    }

    @Transactional
    @Override
    public ClientContentResponseDto postLocationInfo(ClientContentRequestDto clientContentRequestDto) {
        ClientContent infoContent = clientContentRepository.findByContentType(ContentType.LOCATION.name())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Content of type " + ContentType.LOCATION.name() + " not found at server"));
        clientContentMapper.mapFromClientContentRequestDtoToClientContent(infoContent, clientContentRequestDto);
        clientContentRepository.save(infoContent);
        return clientContentMapper.mapFromClientContentToClientContentResponseDto(infoContent);
    }

}
