package br.org.oficinadasmeninas.infra.shared.exception;

public class InvalidCaptchaException extends RuntimeException {
    public InvalidCaptchaException() {
        super("Token Captcha inválido");
    }
}
