package br.org.oficinadasmeninas.infra.shared.exception;

public class EntityNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public EntityNotFoundException(String s) {
        super(s);
    }
}
