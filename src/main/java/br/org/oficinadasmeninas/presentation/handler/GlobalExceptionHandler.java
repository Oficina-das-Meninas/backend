package br.org.oficinadasmeninas.presentation.handler;

import br.org.oficinadasmeninas.domain.Response;
import br.org.oficinadasmeninas.infra.exceptions.ObjectStorageException;
import br.org.oficinadasmeninas.infra.shared.exception.DocumentAlreadyExistsException;
import br.org.oficinadasmeninas.infra.shared.exception.EmailAlreadyExistsException;
import br.org.oficinadasmeninas.presentation.exceptions.NotFoundException;
import br.org.oficinadasmeninas.presentation.exceptions.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex) {

       return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException ex) {

        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectStorageException.class)
    public ResponseEntity<?> handleObjectStorageException(ObjectStorageException ex) {

        return buildResponse(ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {

        var type = ex.getRequiredType();

        var message = UUID.class.equals(type)
                ? "O parâmetro fornecido não é um UUID válido."
                : "Parâmetro inválido: " + type;

        return buildResponse(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }
    
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
    	return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
    
    @ExceptionHandler(DocumentAlreadyExistsException.class)
    public ResponseEntity<String> handleDocumentAlreadyExists(DocumentAlreadyExistsException ex) {
    	return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    private static ResponseEntity<?> buildResponse(String message, HttpStatus status) {

        var body = new Response<Void>(message, null);
        return ResponseEntity.status(status).body(body);
    }
}
