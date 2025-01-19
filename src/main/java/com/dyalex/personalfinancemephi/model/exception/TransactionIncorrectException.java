package com.dyalex.personalfinancemephi.model.exception;

public class TransactionIncorrectException extends RuntimeException {
    public TransactionIncorrectException(String message) {
        super(message);
    }
}
