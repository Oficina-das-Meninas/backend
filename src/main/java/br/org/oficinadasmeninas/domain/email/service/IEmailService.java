package br.org.oficinadasmeninas.domain.email.service;

import java.util.Map;

/**
 * Serviço responsável pelo envio de e-mails da aplicação.
 * <p>
 * Define operações para envio de mensagens em texto simples e em HTML
 * renderizadas a partir de templates, permitindo reutilização de layout
 * e personalização via variáveis.
 */
public interface IEmailService {

    /**
     * Envia um e-mail em texto simples (conteúdo plano).
     *
     * @param to endereço de e-mail do destinatário; não deve ser {@code null} nem vazio
     * @param subject assunto do e-mail; não deve ser {@code null} nem vazio
     * @param text corpo da mensagem em texto puro; não deve ser {@code null}
     */
    void send(String to, String subject, String text);

    /**
     * Envia um e-mail em HTML utilizando um template.
     *
     * @param to endereço de e-mail do destinatário; não deve ser {@code null} nem vazio
     * @param subject assunto do e-mail; não deve ser {@code null} nem vazio
     * @param template nome lógico do template a ser processado pelo mecanismo de templates
     *                 (ex.: {@code "default"}); não deve ser {@code null} nem vazio
     * @param variables mapa de variáveis disponibilizadas ao template (pode ser {@code null})
     */
    void sendHtml(String to, String subject, String template, Map<String, Object> variables);

    /**
     * Envia um e-mail utilizando o template padrão (nome lógico {@code "default"}).
     * <p>
     * O template padrão deve estar acessível no classpath conforme configuração do mecanismo
     * de templates. Por convenção do projeto, encontra-se em
     * {@code br/org/oficinadasmeninas/infra/email/templates/default.html}.
     *
     * @param to endereço de e-mail do destinatário; não deve ser {@code null} nem vazio
     * @param subject assunto do e-mail; não deve ser {@code null} nem vazio
     * @param greeting saudação ou título exibido no topo do e-mail; não deve ser {@code null}
     * @param contentHtml conteúdo principal do e-mail em HTML, inserido no corpo do template;
     *                    não deve ser {@code null}
     */
    void sendWithDefaultTemplate(String to, String subject, String greeting, String contentHtml);
}
