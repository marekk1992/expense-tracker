package com.example.expensetracker.controller.model;

import com.example.expensetracker.repository.model.UserAccount;
import jakarta.validation.constraints.Min;

public record UpdateUserAccountRequest(

        @Min(value = 0, message = "Budget can`t be negative.")
        double budget,

        @Min(value = 0, message = "Monthly salary can`t be negative.")
        double monthlySalary
) {

    public UserAccount toEntity(String username) {
        return new UserAccount(username, budget, monthlySalary);
    }
}
