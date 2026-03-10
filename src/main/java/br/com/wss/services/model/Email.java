package br.com.wss.services.model;

import java.util.Map;

import lombok.Data;

@Data
public class Email {
    private String to;
    private String subject;
    private String body;
    private String template;
    private Map<String, Object> variables;
}
