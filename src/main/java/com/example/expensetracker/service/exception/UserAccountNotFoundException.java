package com.example.expensetracker.service.exception;

public class UserAccountNotFoundException extends RuntimeException {

    public UserAccountNotFoundException(String message) {
        super(message);
    }
}
