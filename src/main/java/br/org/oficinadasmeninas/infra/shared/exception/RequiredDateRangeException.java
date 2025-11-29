package br.org.oficinadasmeninas.infra.shared.exception;

public class RequiredDateRangeException extends RuntimeException {
    public RequiredDateRangeException() {
        super("Data inicial e data final são obrigatórias");
    }
}
