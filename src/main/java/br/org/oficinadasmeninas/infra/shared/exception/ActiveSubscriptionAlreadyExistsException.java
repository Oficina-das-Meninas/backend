package br.org.oficinadasmeninas.infra.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ActiveSubscriptionAlreadyExistsException extends RuntimeException {
    public ActiveSubscriptionAlreadyExistsException() {
        super("Usuário já possui assinatura ativa");
    }
}
