package br.org.oficinadasmeninas.domain;

import java.time.LocalDateTime;
import java.util.Optional;

import static java.time.LocalDateTime.now;

public class Response<T> {

    private final Optional<String> message;
    private final LocalDateTime date = now();
    private final Optional<T> data;

    public Response(String message, T data) {
        this.message = Optional.ofNullable(message);
        this.data = Optional.ofNullable(data);
    }
}