package com.example.expensetracker.controller.error.model;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ApiError(HttpStatus httpStatus, String errorMessage, LocalDateTime localDateTime) {
}
