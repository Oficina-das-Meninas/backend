package br.org.oficinadasmeninas.infra.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmailAlreadyExistsException extends RuntimeException {
	public EmailAlreadyExistsException() {
        super("Email já cadastrado");
    }
}