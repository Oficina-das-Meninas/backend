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
     * @param to      endereço de e-mail do destinatário; não deve ser {@code null} nem vazio
     * @param subject assunto do e-mail; não deve ser {@code null} nem vazio
     * @param text    corpo da mensagem em texto puro; não deve ser {@code null}
     */
    void send(String to, String subject, String text);

    /**
     * Envia um e-mail em HTML utilizando um template.
     *
     * @param to        endereço de e-mail do destinatário; não deve ser {@code null} nem vazio
     * @param subject   assunto do e-mail; não deve ser {@code null} nem vazio
     * @param template  nome lógico do template a ser processado pelo mecanismo de templates
     *                  (ex.: {@code "default"} ou {@code "email/default"}); não deve ser {@code null} nem vazio
     * @param variables mapa de variáveis disponibilizadas ao template (pode ser {@code null})
     */
    void sendHtml(String to, String subject, String template, Map<String, Object> variables);

    /**
     * Envia um e-mail utilizando o template padrão do projeto.
     * <p>
     * Comportamento:
     * - Processa o template lógico {@code "email/default"} localizado em
     *   {@code src/main/resources/templates/email/default.html} (padrão do projeto) e envia o HTML
     *   resultante ao destinatário.
     * - Monta um mapa de variáveis simples usado pelo template. As variáveis mais comuns que o
     *   template utiliza são:
     *     <ul>
     *       <li>{@code title}  - Título principal exibido no topo (string).</li>
     *       <li>{@code greeting} - Texto alternativo para saudação/título (string).</li>
     *       <li>{@code description} - Parágrafo descritivo (string).</li>
     *       <li>{@code contentHtml} - Conteúdo HTML livre que pode ser inserido no corpo (string, usado com th:utext).</li>
     *       <li>{@code heroImageUrl} - URL da imagem de destaque (string, opcional).</li>
     *       <li>{@code footerAddress} - Texto do rodapé com endereço/contato (string).</li>
     *       <li>{@code year} - Ano exibido no rodapé (número).</li>
     *     </ul>
     * <p>
     * Observações:
     * - Se {@code title} não for informado, {@code greeting} será usado como fallback.
     * - O método delega para {@link #sendHtml(String, String, String, Map)} e pode lançar
     *   exceções de email caso o envio falhe.
     *
     * @param to endereço de e-mail do destinatário; não deve ser {@code null} nem vazio
     * @param subject assunto do e-mail; não deve ser {@code null} nem vazio
     * @param greeting saudação ou título exibido no topo do e-mail; não deve ser {@code null}
     * @param contentHtml conteúdo principal do e-mail em HTML, inserido no corpo do template;
     *                    não deve ser {@code null}
     */
    void sendWithDefaultTemplate(String to, String subject, String greeting, String contentHtml);
}