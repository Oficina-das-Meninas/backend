package br.org.oficinadasmeninas.infra.transparency.exception;

import java.util.UUID;

public class DocumentNotFoundException extends RuntimeException {

    public DocumentNotFoundException(UUID id) {
        super("Document with id " + id + " not found.");
    }
}
