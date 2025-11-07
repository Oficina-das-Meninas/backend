package br.org.oficinadasmeninas.infra.shared.exception;

public class EmailSendException extends RuntimeException {
    public EmailSendException(String message) {
        super(message);
    }
}
