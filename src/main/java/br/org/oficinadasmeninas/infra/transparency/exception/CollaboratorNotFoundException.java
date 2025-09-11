package br.org.oficinadasmeninas.infra.transparency.exception;

import java.util.UUID;

public class CollaboratorNotFoundException extends RuntimeException {

    public CollaboratorNotFoundException(UUID id) {
        super("Collaborator with id " + id + " not found.");
    }
}
