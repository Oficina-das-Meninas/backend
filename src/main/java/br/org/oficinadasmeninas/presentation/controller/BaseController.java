package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.function.Supplier;

public abstract class BaseController {

    protected <T> ResponseEntity<?> handle(Supplier<Response<T>> action, HttpStatus successStatus){

        try{
            var body = action.get();

            return ResponseEntity
                    .status(successStatus)
                    .body(body);

        }catch (Exception e){

            return mapException(e);
        }
    }

    private ResponseEntity<?> mapException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}