package br.org.oficinadasmeninas.infra.shared.exception;

public class DocumentAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public DocumentAlreadyExistsException() {
        super("Documento já cadastrado");
    }
}
