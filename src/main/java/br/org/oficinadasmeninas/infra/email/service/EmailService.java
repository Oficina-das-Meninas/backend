package br.org.oficinadasmeninas.infra.email.service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.presentation.exceptions.InternalException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import br.org.oficinadasmeninas.domain.email.service.IEmailService;
import br.org.oficinadasmeninas.infra.auth.UserDetailsCustom;
import br.org.oficinadasmeninas.infra.auth.service.JwtService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
public class EmailService implements IEmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final JwtService jwtService;

    @Value("${app.mail.from}")
    private String fromAddress;

    @Value("${app.mail.from.name:Oficina das Meninas}")
    private String fromName;
    
    @Value("${app.redirect.verify-email}")
    private String redirectVerifyEmail;
    
    @Value("${app.redirect.user.reset-password}")
    private String redirectUserResetPassword;

    @Value("${app.redirect.admin.reset-password}")
    private String redirectAdminResetPassword;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine, JwtService jwtService) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.jwtService = jwtService;
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
            throw new InternalException(Messages.ERROR_SENDING_EMAIL);
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
                addDefaultVariables(variables);
                variables.forEach(context::setVariable);
            }

            String html = templateEngine.process(template, context);

            helper.setFrom(fromAddress, fromName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new InternalException(Messages.ERROR_SENDING_EMAIL);
        }
    }

    @Override
    public void sendWithDefaultTemplate(String to, String subject, String greeting, String contentHtml) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("greeting", greeting);
        vars.put("contentHtml", contentHtml);
        addDefaultVariables(vars);
        sendHtml(to, subject, "email/default", vars);
    }
    
    public void sendConfirmUserAccountEmail(String email, String name) {
    	String to = email;
        String subject = "Verificação de e-mail";
        String greeting = "Olá, " + name;
        
        String verifyEmailToken = jwtService.generateVerifyEmailToken(
        		new UserDetailsCustom(
        				null, 
        				email, 
        				null, 
        				name, 
        				false
        			)
        		);
        
        String magicLink = redirectVerifyEmail+verifyEmailToken;
        String href = String.format("<a href='%s'>Verificar e-mail</a>", magicLink);
        
        String contentHtml = "<p>Clique no link abaixo para verificar sua conta:</p>" +
        		href;
        
        sendWithDefaultTemplate(to, subject, greeting, contentHtml);
    }
    
    public void sendResetPasswordEmail(String email, String name, boolean isAdmin) {
    	String to = email;
        String subject = "Recuperar conta";
        String greeting = "Olá, " + name;
        
        String verifyEmailToken = jwtService.generateResetPasswordToken(
        		new UserDetailsCustom(
        				null, 
        				email, 
        				null, 
        				name, 
        				isAdmin
        			)
        		);

        String magicLink = redirectUserResetPassword+verifyEmailToken;
        if (isAdmin) {
            magicLink = redirectAdminResetPassword+verifyEmailToken;
        }
        String href = String.format("<a href='%s'>Recuperar senha</a>", magicLink);
        
        String contentHtml = "<p>Clique no link abaixo para redefinir senha:</p>" +
        		href;
        
        sendWithDefaultTemplate(to, subject, greeting, contentHtml);
    }

    private void addDefaultVariables(Map<String, Object> variables) {
        variables.putIfAbsent("footerAddress", "Oficina das Meninas · R. Padre Manoel da Nobrega, 540 - Parque Alvorada");
        variables.putIfAbsent("year", java.time.Year.now().getValue());
    }
}
