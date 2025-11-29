package br.org.oficinadasmeninas.infra.shared.exception;

import br.org.oficinadasmeninas.domain.resources.Messages;

public class ObjectStorageException extends RuntimeException {
    public ObjectStorageException(Throwable cause) {
        super(Messages.OBJECT_STORAGE_ERROR, cause);
    }
}