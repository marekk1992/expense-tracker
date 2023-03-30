package com.example.expensetracker.controller.model;

import com.example.expensetracker.repository.model.UserAccount;

import java.util.UUID;

public record UserAccountResponse(UUID id, String username, double budget, double monthlySalary) {

    public static UserAccountResponse fromEntity(UserAccount userAccount) {
        return new UserAccountResponse(
                userAccount.getId(),
                userAccount.getUsername(),
                userAccount.getBudget(),
                userAccount.getMonthlySalary()
        );
    }
}
