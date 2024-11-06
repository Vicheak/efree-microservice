package com.efree.gateway.external.mail;

import jakarta.mail.MessagingException;

public interface MailService {

    void sendMail(Mail<?> mail) throws MessagingException;

}
