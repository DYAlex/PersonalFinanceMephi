package com.dyalex.personalfinancemephi.controller;

import com.dyalex.personalfinancemephi.model.ErrorResponseDto;
import com.dyalex.personalfinancemephi.model.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {
    @ExceptionHandler({CategoryExistsException.class, CategoryNotFoundException.class,
            WalletNotFoundException.class})
    public ResponseEntity<ErrorResponseDto> handleExceptionForNotFoundHttpStatus(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler({RegistrationException.class, TransactionIncorrectException.class, LimitIncorrectException.class,
            IllegalArgumentException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponseDto> handleExceptionForBadRequestHttpStatus(Exception ex) {
        return ResponseEntity.badRequest().body(getErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(ex.getBindingResult()
                .getAllErrors().get(0).getDefaultMessage()));
    }

    private ErrorResponseDto getErrorResponse(String message) {
        return ErrorResponseDto.builder()
                .message(message)
                .result(false)
                .build();
    }
}
