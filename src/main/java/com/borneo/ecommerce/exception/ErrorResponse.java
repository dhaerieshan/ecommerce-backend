// src/main/java/com/borneo/ecommerce/exception/ErrorResponse.java

package com.borneo.ecommerce.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;
}