package com.example.expensetracker.service;

import com.example.expensetracker.repository.UserAccountRepository;
import com.example.expensetracker.repository.model.UserAccount;
import com.example.expensetracker.service.exception.UsernameIsTakenException;
import com.example.expensetracker.service.exception.UserAccountNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    public UserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public List<UserAccount> findAll() {
        return userAccountRepository.findAll();
    }

    public UserAccount findByUsername(String username){
        Optional<UserAccount> userAccount = userAccountRepository.findByUsername(username);
        return userAccount.orElseThrow(() -> new UserAccountNotFoundException("User '" + username + "' couldn`t be found in database."));
    }

    public void deleteByUsername(String username) {
        if (userAccountRepository.findByUsername(username).isEmpty()) {
            throw new UserAccountNotFoundException("Deletion failed. User '" + username + "' couldn`t be found in database.");
        }
        userAccountRepository.deleteByUsername(username);
    }

    public UserAccount save(UserAccount userAccount) {
        String username = userAccount.getUsername();
        if (userAccountRepository.findByUsername(username).isPresent()) {
            throw new UsernameIsTakenException("Addition failed. Username '" + username + "' is already taken.");
        }

        return userAccountRepository.save(userAccount);
    }

    public UserAccount update(UserAccount userAccount) {
        String username = userAccount.getUsername();
        Optional<UserAccount> tempUserAccount = userAccountRepository.findByUsername(username);
        if (tempUserAccount.isEmpty()) {
            throw new UserAccountNotFoundException("Update failed. User '" + username + "' couldn`t be found in database.");
        }
        UUID userId = tempUserAccount.get().getId();
        userAccount.setId(userId);

        return userAccountRepository.save(userAccount);
    }
}
