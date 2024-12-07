package com.bnpp.kata.onlinebookstore.exception;

import com.bnpp.kata.onlinebookstore.models.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException .class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException  ex) {

        ErrorResponse errorResponse	= ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {

        ErrorResponse errorResponse	= ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .build();

        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
