package com.weyland.bishop.exception;

import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice(basePackages = "com.weyland.bishop")
public class GlobalExceptionHandler {

    // Новый обработчик для ошибок десериализации
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<WeylandErrorResponse> handleJsonErrors(HttpMessageNotReadableException ex, WebRequest request) {
        String errorMessage = "Invalid request format";

        // Специальная обработка для ошибок enum
        if (ex.getMessage() != null && ex.getMessage().contains("Priority")) {
            errorMessage = "Invalid priority value. Allowed values: COMMON, CRITICAL";
        } else if (ex.getMessage() != null && ex.getMessage().contains("time")) {
            errorMessage = "Invalid time format. Use ISO8601 (e.g. 2023-01-01T12:00:00Z)";
        }

        WeylandErrorResponse response = new WeylandErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                errorMessage,
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Остальные обработчики остаются без изменений
    @ExceptionHandler(QueueFullException.class)
    public ResponseEntity<WeylandErrorResponse> handleQueueFull(QueueFullException ex, WebRequest request) {
        WeylandErrorResponse response = new WeylandErrorResponse(
                HttpStatus.TOO_MANY_REQUESTS.value(),
                "Queue overflow",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(response, HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<WeylandErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        WeylandErrorResponse response = new WeylandErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                String.join("; ", errors),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<WeylandErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
        WeylandErrorResponse response = new WeylandErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal error",
                "Android malfunction detected: " + ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}