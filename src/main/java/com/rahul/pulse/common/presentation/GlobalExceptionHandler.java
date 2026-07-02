package com.rahul.pulse.common.presentation;

import com.rahul.pulse.auth.domain.exception.*;
import com.rahul.pulse.common.exception.ResourceNotFoundException;
import com.rahul.pulse.posts.domain.exception.InvalidPostException;
import com.rahul.pulse.posts.domain.exception.LikeDoesNotExistsException;
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

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException e)
    {
        return buildError(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(RefreshTokenReuseDetectedException.class)
    public ResponseEntity<ErrorResponse> handleRefreshTokenReuseDetectedException(RefreshTokenReuseDetectedException e)
    {
        return buildError(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e)
    {
        return buildError(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(LikeDoesNotExistsException.class)
    public ResponseEntity<ErrorResponse> handleLikeDoesNotExistsException(LikeDoesNotExistsException e)
    {
        return buildError(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(InvalidPostException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPostException(InvalidPostException e)
    {
        return buildError(HttpStatus.UNAUTHORIZED, e.getMessage());
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
