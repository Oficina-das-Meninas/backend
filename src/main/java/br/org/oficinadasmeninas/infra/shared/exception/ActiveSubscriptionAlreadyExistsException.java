package br.org.oficinadasmeninas.infra.shared.exception;

public class ActiveSubscriptionAlreadyExistsException extends RuntimeException {
    public ActiveSubscriptionAlreadyExistsException(String message) {
        super("Usuário já possui assinatura ativa");
    }
}
