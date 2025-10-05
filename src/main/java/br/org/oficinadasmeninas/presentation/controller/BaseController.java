package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.function.Supplier;

public abstract class BaseController {

    protected <T> ResponseEntity<?> handle(Supplier<T> action, String successMessage, HttpStatus successStatus) {
        var actionResponse = action.get();

        var body = new Response<>(successMessage, actionResponse);

        return ResponseEntity.status(successStatus).body(body);
    }

    protected <T> ResponseEntity<?> handle(Supplier<T> action, String successMessage) {
        return handle(action, successMessage, HttpStatus.OK);
    }

    protected <T> ResponseEntity<?> handle(Supplier<T> action) {
        return handle(action, null, HttpStatus.OK);
    }
}