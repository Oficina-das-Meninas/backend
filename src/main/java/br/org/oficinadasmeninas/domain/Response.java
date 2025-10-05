package br.org.oficinadasmeninas.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

    private final String message;

    private final LocalDateTime date = now();
    private final T data;

    public Response(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public T getData() {
        return data;
    }
}