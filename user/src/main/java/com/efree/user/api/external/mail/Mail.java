package com.efree.user.api.external.mail;

import lombok.Data;

@Data
public class Mail<T> {

    private String sender;
    private String receiver;
    private String subject;
    private String template;
    private T metaData;

}