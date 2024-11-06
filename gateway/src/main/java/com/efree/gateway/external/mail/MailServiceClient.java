package com.efree.gateway.external.mail;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceClient {

    private final MailService mailService;

    @Value("${spring.mail.username}")
    private String adminMail;

    @Async
    public void buildAndSendMailAsync(String email, String subject, String verifiedCode) throws MessagingException {
        Mail<String> verifiedMail = new Mail<>();
        verifiedMail.setSender(adminMail);
        verifiedMail.setReceiver(email);
        verifiedMail.setSubject(subject);
        verifiedMail.setTemplate("auth/verify-mail.html");
        verifiedMail.setMetaData(verifiedCode);
        mailService.sendMail(verifiedMail);
    }

}
