package com.example.expensetracker.controller.model;

import com.example.expensetracker.repository.model.UserAccount;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record CreateUserAccountRequest(

        @Size(min = 4, message = "Username should contain at least 4 characters.")
        @Size(max = 20, message = "Username can`t be longer than 20 characters.")
        String username,

        @Min(value = 0, message = "Budget can`t be negative.")
        double budget,

        @Min(value = 0, message = "Monthly salary can`t be negative.")
        double monthlySalary
) {

    public UserAccount toEntity() {
        return new UserAccount(username, budget, monthlySalary);
    }
}
