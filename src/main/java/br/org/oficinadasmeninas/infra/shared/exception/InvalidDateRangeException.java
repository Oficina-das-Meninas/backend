package br.org.oficinadasmeninas.infra.shared.exception;

public class InvalidDateRangeException extends RuntimeException {
    public InvalidDateRangeException() {
        super("Data inicial deve ser anterior ou igual à data final");
    }
}
