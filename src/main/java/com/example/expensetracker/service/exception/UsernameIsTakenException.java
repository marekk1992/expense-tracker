package com.example.expensetracker.service.exception;

public class UsernameIsTakenException extends RuntimeException {

    public UsernameIsTakenException(String message) {
        super(message);
    }
}
