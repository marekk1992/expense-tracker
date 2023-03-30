package com.example.expensetracker.controller.error;

import com.example.expensetracker.controller.error.model.ApiError;
import com.example.expensetracker.service.exception.UserAccountNotFoundException;
import com.example.expensetracker.service.exception.UsernameIsTakenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class UserAccountExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApiError> handleException(Exception exc) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, exc.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleException(UserAccountNotFoundException exc) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, exc.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleException(UsernameIsTakenException exc) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, exc.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
}
