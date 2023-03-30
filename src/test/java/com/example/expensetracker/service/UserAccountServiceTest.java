package com.example.expensetracker.service;

import com.example.expensetracker.repository.UserAccountRepository;
import com.example.expensetracker.repository.model.UserAccount;
import com.example.expensetracker.service.exception.UserAccountNotFoundException;
import com.example.expensetracker.service.exception.UsernameIsTakenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserAccountServiceTest {

    private static final UUID ID_1 = UUID.fromString("b1495431-0753-4c46-9715-53fbd30f5668");
    private static final UUID ID_2 = UUID.fromString("e4850b1b-4934-42c8-bbe8-a1d9874938bd");
    private static final String USERNAME_1 = "user1";

    @Mock
    private UserAccountRepository userAccountRepository;

    @InjectMocks
    private UserAccountService userAccountService;

    @Test
    void returns_collection_of_user_accounts() {
        // given
        List<UserAccount> expectedUserAccounts = List.of(
                new UserAccount(ID_1, USERNAME_1, 500, 1000),
                new UserAccount(ID_2, "user2", 600, 1200)
        );
        when(userAccountRepository.findAll()).thenReturn(expectedUserAccounts);

        // when
        List<UserAccount> actualUserAccounts = userAccountRepository.findAll();

        // then
        assertThat(actualUserAccounts)
                .isEqualTo(expectedUserAccounts);
    }

    @Test
    void returns_user_account_by_username() {
        // given
        UserAccount expectedUserAccount = new UserAccount(ID_1, USERNAME_1, 500, 1000);
        when(userAccountRepository.findByUsername(USERNAME_1)).thenReturn(Optional.of(expectedUserAccount));

        // when
        UserAccount actualUserAccount = userAccountService.findByUsername(USERNAME_1);

        // then
        assertThat(actualUserAccount)
                .isEqualTo(expectedUserAccount);
    }

    @Test
    void throws_exception_when_trying_to_retrieve_non_existing_user_account() {
        // given
        when(userAccountRepository.findByUsername(USERNAME_1)).thenReturn(Optional.empty());

        // then
        assertThatExceptionOfType(UserAccountNotFoundException.class)
                .isThrownBy(() -> userAccountService.findByUsername(USERNAME_1))
                .withMessage("User '" + USERNAME_1 + "' couldn`t be found in database.");
    }

    @Test
    void deletes_user_account_by_username() {
        // given
        UserAccount userAccount = new UserAccount(ID_1, USERNAME_1, 500, 1000);
        when(userAccountRepository.findByUsername(USERNAME_1)).thenReturn(Optional.of(userAccount));
        doNothing().when(userAccountRepository).deleteByUsername(USERNAME_1);

        // when
        userAccountService.deleteByUsername(USERNAME_1);

        // then
        verify(userAccountRepository, times(1)).deleteByUsername(USERNAME_1);
        verifyNoMoreInteractions(userAccountRepository);
    }

    @Test
    void throws_exception_when_trying_to_delete_non_existing_user_account() {
        // given
        when(userAccountRepository.findByUsername(USERNAME_1)).thenReturn(Optional.empty());

        // then
        assertThatExceptionOfType(UserAccountNotFoundException.class)
                .isThrownBy(() -> userAccountService.deleteByUsername(USERNAME_1))
                .withMessage("Deletion failed. User '" + USERNAME_1 + "' couldn`t be found in database.");
    }

    @Test
    void saves_user_account() {
        // given
        UserAccount expectedUserAccount = new UserAccount(ID_1, USERNAME_1, 500, 1000);
        when(userAccountRepository.findByUsername(USERNAME_1)).thenReturn(Optional.empty());
        when(userAccountRepository.save(expectedUserAccount)).thenReturn(expectedUserAccount);

        // when
        UserAccount actualUserAccount = userAccountService.save(expectedUserAccount);

        // then
        assertThat(actualUserAccount)
                .isEqualTo(expectedUserAccount);
    }

    @Test
    void throws_exception_when_trying_to_save_user_account_with_already_taken_username() {
        // given
        UserAccount existingUserAccount = new UserAccount(ID_1, USERNAME_1, 500, 1000);
        when(userAccountRepository.findByUsername(USERNAME_1)).thenReturn(Optional.of(existingUserAccount));

        // then
        assertThatExceptionOfType(UsernameIsTakenException.class)
                .isThrownBy(() -> userAccountService.save(existingUserAccount))
                .withMessage("Addition failed. Username '" + USERNAME_1 + "' is already taken.");
    }

    @Test
    void updates_user_account_by_username_with_provided_data() {
        // given
        UserAccount expectedUserAccount = new UserAccount(ID_2, USERNAME_1, 1000, 2000);
        when(userAccountRepository.findByUsername(USERNAME_1)).thenReturn(Optional.of(
                new UserAccount(ID_1, USERNAME_1, 500, 1000)
        ));
        when(userAccountRepository.save(expectedUserAccount)).thenReturn(expectedUserAccount);

        // when
        UserAccount actualUserAccount = userAccountService.update(expectedUserAccount);

        // then
        assertThat(actualUserAccount)
                .isEqualTo(expectedUserAccount);
    }

    @Test
    void throws_exception_when_trying_to_update_non_existing_user_account() {
        // given
        UserAccount userAccount = new UserAccount(ID_1, USERNAME_1, 500, 1000);
        when(userAccountRepository.findByUsername(USERNAME_1)).thenReturn(Optional.empty());

        // then
        assertThatExceptionOfType(UserAccountNotFoundException.class)
                .isThrownBy(() -> userAccountService.update(userAccount))
                .withMessage("Update failed. User '" + USERNAME_1 + "' couldn`t be found in database.");
    }
}
