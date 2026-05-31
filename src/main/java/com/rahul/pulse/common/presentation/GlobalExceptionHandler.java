package com.rahul.pulse.common.presentation;

import com.rahul.pulse.auth.domain.exception.InvalidCredentialsException;
import com.rahul.pulse.auth.domain.exception.InvalidEmailException;
import com.rahul.pulse.auth.domain.exception.InvalidPasswordHashException;
import com.rahul.pulse.auth.domain.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex)
    {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEmailException(InvalidEmailException ex)
    {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(InvalidPasswordHashException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPasswordHashException(InvalidPasswordHashException ex)
    {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(InvalidCredentialsException ex)
    {
        return buildError(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex
    ) {
        String message =
                ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .findFirst()
                        .map(err -> err.getField() + " " + err.getDefaultMessage())
                        .orElse("Validation failed");

        return buildError(HttpStatus.BAD_REQUEST, message);
    }

    private ResponseEntity<ErrorResponse> buildError(
            HttpStatus status,
            String message
    ) {
        return ResponseEntity
                .status(status)
                .body(
                        new ErrorResponse(
                                status.value(),
                                message,
                                Instant.now()
                        )
                );
    }
}
