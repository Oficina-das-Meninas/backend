package br.org.oficinadasmeninas.infra.email.service;

import br.org.oficinadasmeninas.domain.email.service.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementação do serviço de envio de e-mails.
 * <p>
 * Fornece métodos para envio em texto simples e envio de HTML a partir de
 * templates processados pelo Thymeleaf. Também expõe um método utilitário
 * para enviar o template padrão do projeto.
 */
@Service
public class EmailService implements IEmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.mail.from}")
    private String fromAddress;

    @Value("${app.mail.from.name:Oficina das Meninas}")
    private String fromName;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void send(String to, String subject, String text) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, StandardCharsets.UTF_8.name());
            helper.setFrom(fromAddress, fromName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, false);
            mailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Falha ao enviar e-mail", e);
        }
    }


    @Override
    public void sendHtml(String to, String subject, String template, Map<String, Object> variables) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            Context context = new Context();
            if (variables != null) {
                // Preenche valores padrão caso não fornecidos
                variables.putIfAbsent("footerAddress", "Oficina das Meninas · R. Padre Manoel da Nobrega, 552 - Parque Alvorada");
                variables.putIfAbsent("year", java.time.Year.now().getValue());
                variables.forEach(context::setVariable);
            }

            String html = templateEngine.process(template, context);

            helper.setFrom(fromAddress, fromName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Falha ao enviar e-mail", e);
        }
    }

    @Override
    public void sendWithDefaultTemplate(String to, String subject, String greeting, String contentHtml) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("greeting", greeting);
        vars.put("contentHtml", contentHtml);
        vars.putIfAbsent("footerAddress", "Oficina das Meninas · R. Padre Manoel da Nobrega, 552 - Parque Alvorada");
        vars.putIfAbsent("year", java.time.Year.now().getValue());
        sendHtml(to, subject, "email/default", vars);
    }
}
