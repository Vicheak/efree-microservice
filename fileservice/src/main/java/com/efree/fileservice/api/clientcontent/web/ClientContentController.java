package com.efree.fileservice.api.clientcontent.web;

import com.efree.fileservice.api.clientcontent.ClientContentService;
import com.efree.fileservice.base.BaseApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/contents")
@RequiredArgsConstructor
public class ClientContentController {

    private final ClientContentService clientContentService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public BaseApi<?> loadAllClientContents() {

        List<ClientContentResponseDto> clientContentResponseDtoList = clientContentService.loadAllClientContents();

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("All client contents loaded successfully!")
                .timestamp(LocalDateTime.now())
                .payload(clientContentResponseDtoList)
                .build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/INFO")
    public BaseApi<?> postPageInfo(@RequestBody @Valid ClientContentRequestDto clientContentRequestDto) {

        ClientContentResponseDto clientContentResponseDto = clientContentService.postPageInfo(clientContentRequestDto);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.CREATED.value())
                .message("Client content has been posted successfully!")
                .timestamp(LocalDateTime.now())
                .payload(clientContentResponseDto)
                .build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/ABOUTUS")
    public BaseApi<?> postAboutUsInfo(@RequestBody @Valid ClientContentRequestDto clientContentRequestDto) {

        ClientContentResponseDto clientContentResponseDto = clientContentService.postAboutUsInfo(clientContentRequestDto);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.CREATED.value())
                .message("Client content has been posted successfully!")
                .timestamp(LocalDateTime.now())
                .payload(clientContentResponseDto)
                .build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/LOCATION")
    public BaseApi<?> postLocationInfo(@RequestBody @Valid ClientContentRequestDto clientContentRequestDto) {

        ClientContentResponseDto clientContentResponseDto = clientContentService.postLocationInfo(clientContentRequestDto);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.CREATED.value())
                .message("Client content has been posted successfully!")
                .timestamp(LocalDateTime.now())
                .payload(clientContentResponseDto)
                .build();
    }

}
