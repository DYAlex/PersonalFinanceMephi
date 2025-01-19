package com.dyalex.personalfinancemephi.model.exception;

public class CategoryExistsException extends RuntimeException {
    public CategoryExistsException(String message) {
        super(message);
    }
}
