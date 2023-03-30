package com.example.expensetracker.repository;

import com.example.expensetracker.repository.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {

    Optional<UserAccount> findByUsername(String username);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM user_account WHERE username = :username", nativeQuery = true)
    void deleteByUsername(String username);
}
