package com.example.expensetracker.controller;

import com.example.expensetracker.controller.model.CreateUserAccountRequest;
import com.example.expensetracker.controller.model.UpdateUserAccountRequest;
import com.example.expensetracker.controller.model.UserAccountsCollectionResponse;
import com.example.expensetracker.controller.model.UserAccountResponse;
import com.example.expensetracker.service.UserAccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class UserAccountController {

    private final UserAccountService userAccountService;

    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @GetMapping
    public UserAccountsCollectionResponse findAll() {
        return UserAccountsCollectionResponse.fromEntity(userAccountService.findAll());
    }

    @GetMapping("/{username}")
    public UserAccountResponse findByUsername(@PathVariable String username) {
        return UserAccountResponse.fromEntity(userAccountService.findByUsername(username));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserAccountResponse addUser(@Valid @RequestBody CreateUserAccountRequest createUserAccountRequest) {
        return UserAccountResponse.fromEntity(userAccountService.save(createUserAccountRequest.toEntity()));
    }

    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username) {
        userAccountService.deleteByUsername(username);
    }

    @PutMapping("/{username}")
    public UserAccountResponse updateUser(
            @Valid @RequestBody UpdateUserAccountRequest updateUserAccountRequest, @PathVariable String username
    ) {
        return UserAccountResponse.fromEntity(userAccountService.update(updateUserAccountRequest.toEntity(username)));
    }
}
