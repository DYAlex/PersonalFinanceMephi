package com.dyalex.personalfinancemephi.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseDto {
    private String message;
    private boolean result;
}
