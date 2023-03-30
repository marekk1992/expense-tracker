package com.example.expensetracker.controller.model;

import com.example.expensetracker.repository.model.UserAccount;

import java.util.List;

public record UserAccountsCollectionResponse(List<UserAccountResponse> userAccounts) {

    public static UserAccountsCollectionResponse fromEntity(List<UserAccount> userAccounts) {
        List<UserAccountResponse> response = userAccounts.stream()
                .map(UserAccountResponse::fromEntity)
                .toList();

        return new UserAccountsCollectionResponse(response);
    }
}
