package com.example.expensetracker.repository;

import com.example.expensetracker.repository.model.UserAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserAccountRepositoryTest {

    private static final UUID ID_1 = UUID.fromString("b1495431-0753-4c46-9715-53fbd30f5668");
    private static final UUID ID_2 = UUID.fromString("e4850b1b-4934-42c8-bbe8-a1d9874938bd");
    private static final String USERNAME_1 = "user1";
    private static final String USERNAME_2 = "user2";

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Test
    void return_collection_of_user_accounts() {
        // given
        userAccountRepository.saveAll(List.of(
                new UserAccount(ID_1, USERNAME_1, 500, 1000),
                new UserAccount(ID_2, USERNAME_2, 600, 1200)
        ));

        // when
        List<UserAccount> actualUserAccounts = userAccountRepository.findAll();

        // then
        assertThat(actualUserAccounts)
                .extracting("id")
                .containsExactly(ID_1, ID_2);
    }

    @Test
    void returns_user_account_by_username() {
        // given
        userAccountRepository.saveAll(List.of(
                new UserAccount(ID_1, USERNAME_1, 500, 1000),
                new UserAccount(ID_2, USERNAME_2, 600, 1200)
        ));

        // when
        Optional<UserAccount> actualUserAccount = userAccountRepository.findByUsername(USERNAME_1);

        // then
        assertThat(actualUserAccount.orElse(null))
                .extracting("id")
                .isEqualTo(ID_1);
    }

    @Test
    void saves_user_account() {
        // given
        UserAccount savedUserAccount = userAccountRepository.save(
                new UserAccount(ID_1, USERNAME_1, 500, 1000)
        );

        // then
        assertThat(savedUserAccount)
                .extracting("id")
                .isEqualTo(ID_1);
    }

    @Test
    void deletes_user_account_by_username() {
        // given
        userAccountRepository.saveAll(List.of(
                new UserAccount(ID_1, USERNAME_1, 500, 1000),
                new UserAccount(ID_2, USERNAME_2, 600, 1200)
        ));

        // when
        userAccountRepository.deleteByUsername(USERNAME_1);
        List<UserAccount> actualUserAccounts = userAccountRepository.findAll();

        // then
        assertThat(actualUserAccounts)
                .hasSize(1)
                .extracting("id")
                .containsOnly(ID_2);
    }
}
