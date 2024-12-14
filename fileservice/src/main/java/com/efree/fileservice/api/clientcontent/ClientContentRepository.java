package com.efree.fileservice.api.clientcontent;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientContentRepository extends JpaRepository<ClientContent, String> {

    Optional<ClientContent> findByContentType(String contentType);

}
