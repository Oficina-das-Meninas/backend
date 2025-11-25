package br.org.oficinadasmeninas.presentation.handler;

import br.org.oficinadasmeninas.domain.Response;
import br.org.oficinadasmeninas.infra.shared.exception.DocumentAlreadyExistsException;
import br.org.oficinadasmeninas.infra.shared.exception.EmailAlreadyExistsException;
import br.org.oficinadasmeninas.infra.shared.exception.ObjectStorageException;
import br.org.oficinadasmeninas.infra.shared.exception.PaymentGatewayException;
import br.org.oficinadasmeninas.presentation.exceptions.NotFoundException;
import br.org.oficinadasmeninas.presentation.exceptions.UnauthorizedException;
import br.org.oficinadasmeninas.presentation.exceptions.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
    
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException ex) {

        return buildResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
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
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {

        var bindingResult = ex.getBindingResult();

        var errors = bindingResult
                .getFieldErrors()
                .stream()
                .map(fe -> toErrorMap(fe, bindingResult))
                .collect(Collectors.toList());

        return buildResponse("Mapeamento inválido: verifique os campos.", HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(DocumentAlreadyExistsException.class)
    public ResponseEntity<String> handleDocumentAlreadyExists(DocumentAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
    
    @ExceptionHandler(PaymentGatewayException.class)
    public ResponseEntity<String> handleDocumentAlreadyExists(PaymentGatewayException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    private static ResponseEntity<?> buildResponse(String message, HttpStatus status) {

        var body = new Response<Void>(message, null);
        return ResponseEntity.status(status).body(body);
    }

    private static <T> ResponseEntity<Response<T>> buildResponse(String message, HttpStatus status, T data) {
        var body = new Response<>(message, data);
        return ResponseEntity.status(status).body(body);
    }

    private static Map<String, Object> toErrorMap(FieldError fe, Errors errors) {

        return Map.of(
                "field", fe.getField(),
                "fieldType", Optional.ofNullable(errors.getFieldType(fe.getField())),
                "rejectedValue", Optional.ofNullable(fe.getRejectedValue())
        );
    }
}
