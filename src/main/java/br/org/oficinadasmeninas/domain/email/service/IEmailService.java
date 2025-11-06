package br.org.oficinadasmeninas.domain.email.service;

import java.util.Map;

public interface IEmailService {
    void send(String to, String subject, String text);
    void sendHtml(String to, String subject, String template, Map<String, Object> variables);
    // Template padrão agora em: classpath:/br/org/oficinadasmeninas/infra/email/templates/default.html
    void sendWithDefaultTemplate(String to, String subject, String greeting, String contentHtml);
}
