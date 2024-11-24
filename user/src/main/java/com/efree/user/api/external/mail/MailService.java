package com.efree.user.api.external.mail;

import jakarta.mail.MessagingException;

public interface MailService {

    void sendMail(Mail<?> mail) throws MessagingException;

}
