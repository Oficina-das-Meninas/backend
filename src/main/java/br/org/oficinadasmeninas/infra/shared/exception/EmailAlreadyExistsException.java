package br.org.oficinadasmeninas.infra.shared.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public EmailAlreadyExistsException() {
        super("Email já cadastrado");
    }
}