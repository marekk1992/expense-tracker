package com.example.expensetracker.controller;

import com.example.expensetracker.controller.model.CreateUserAccountRequest;
import com.example.expensetracker.controller.model.UpdateUserAccountRequest;
import com.example.expensetracker.repository.model.UserAccount;
import com.example.expensetracker.service.UserAccountService;
import com.example.expensetracker.service.exception.UserAccountNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.ArgumentMatcher;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.blankString;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class UserAccountControllerTest {

    private static final UUID ID_1 = UUID.fromString("b1495431-0753-4c46-9715-53fbd30f5668");
    private static final UUID ID_2 = UUID.fromString("e4850b1b-4934-42c8-bbe8-a1d9874938bd");
    private static final String USERS_URL = "/v1/users";
    private static final String USER_BY_USERNAME_URL = USERS_URL + "/{username}";
    private static final String USERNAME_1 = "user1";

    @MockBean
    private UserAccountService userAccountService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void returns_collection_of_user_accounts() throws Exception {
        // given
        when(userAccountService.findAll()).thenReturn(List.of(
                new UserAccount(ID_1, USERNAME_1, 500, 1000),
                new UserAccount(ID_2, "user2", 600, 1200)
        ));

        // when
        String actualResponseBody = mockMvc.perform(get(USERS_URL))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // then
        JSONAssert.assertEquals(
                """
                           {
                              "userAccounts": [
                                  {
                                      "id": "b1495431-0753-4c46-9715-53fbd30f5668",
                                      "username": "user1",
                                      "budget": 500.0,
                                      "monthlySalary": 1000.0
                                  },
                                  {
                                      "id": "e4850b1b-4934-42c8-bbe8-a1d9874938bd",
                                      "username": "user2",
                                      "budget": 600.0,
                                      "monthlySalary": 1200.0
                                  }
                              ]
                           }
                        """,
                actualResponseBody, true);
    }

    @Test
    void returns_user_account_by_username() throws Exception {
        // given
        when(userAccountService.findByUsername(USERNAME_1))
                .thenReturn(new UserAccount(ID_1, USERNAME_1, 500, 1000));

        // when
        String actualResponseBody = mockMvc.perform(get(USER_BY_USERNAME_URL, USERNAME_1))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // then
        JSONAssert.assertEquals(
                """
                           {
                               "id": "b1495431-0753-4c46-9715-53fbd30f5668",
                               "username": "user1",
                               "budget": 500.0,
                               "monthlySalary": 1000.0
                           }
                        """,
                actualResponseBody, true);
    }

    @Test
    void returns_404_response_when_trying_to_get_non_existing_user_account() throws Exception {
        // given
        String message = "User '" + USERNAME_1 + "' couldn`t be found in database.";
        doThrow(new UserAccountNotFoundException(message)).when(userAccountService).findByUsername(any(String.class));

        // then
        mockMvc.perform(get(USER_BY_USERNAME_URL, USERNAME_1))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(message)));
    }

    @Test
    void creates_user_account() throws Exception {
        // given
        CreateUserAccountRequest createUserAccountRequest = new CreateUserAccountRequest(USERNAME_1, 500, 1000);
        UserAccount expectedUserAccount = new UserAccount(ID_1, USERNAME_1, 500, 1000);
        when(userAccountService.save(argThat(matchCreateUserAccountRequestToEntity(createUserAccountRequest))))
                .thenReturn(expectedUserAccount);

        // when
        String actualResponseBody = mockMvc.perform(post(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserAccountRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // then
        JSONAssert.assertEquals(
                """
                           {
                               "id": "b1495431-0753-4c46-9715-53fbd30f5668",
                               "username": "user1",
                               "budget": 500.0,
                               "monthlySalary": 1000.0
                           }
                        """,
                actualResponseBody, true);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/invalid_create_user_account_request_parameters.csv", numLinesToSkip = 1)
    void returns_500_response_when_user_input_validation_for_creating_user_account_is_failed(
            String username, double budget, double monthlySalary, String message
    ) throws Exception {
        // given
        String requestBody = objectMapper.writeValueAsString(new CreateUserAccountRequest(username, budget, monthlySalary));

        // then
        mockMvc.perform(post(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString(message)));
    }

    @Test
    void updates_user_account_by_username() throws Exception {
        // given
        UpdateUserAccountRequest updateUserAccountRequest = new UpdateUserAccountRequest(500, 1000);
        UserAccount expectedUserAccount = new UserAccount(ID_1, USERNAME_1, 500, 1000);
        when(userAccountService.update(argThat(matchUpdateUserAccountRequestToEntity(updateUserAccountRequest))))
                .thenReturn(expectedUserAccount);

        // when
        String actualResponseBody = mockMvc.perform(put(USER_BY_USERNAME_URL, USERNAME_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserAccountRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // then
        JSONAssert.assertEquals(
                """
                           {
                               "id": "b1495431-0753-4c46-9715-53fbd30f5668",
                               "username": "user1",
                               "budget": 500.0,
                               "monthlySalary": 1000.0
                           }
                        """,
                actualResponseBody, true);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/invalid_update_user_account_request_parameters.csv", numLinesToSkip = 1)
    void returns_500_response_when_user_input_validation_for_updating_user_account_is_failed(
            double budget, double monthlySalary, String message
    ) throws Exception {
        // given
        String requestBody = objectMapper.writeValueAsString(new UpdateUserAccountRequest(budget, monthlySalary));

        // then
        mockMvc.perform(put(USER_BY_USERNAME_URL, USERNAME_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString(message)));
    }

    @Test
    void returns_404_response_when_trying_to_update_non_existing_user_account() throws Exception {
        // given
        String message = "User '" + USERNAME_1 + "' couldn`t be found in database.";
        doThrow(new UserAccountNotFoundException(message)).when(userAccountService).update(any(UserAccount.class));

        // then
        mockMvc.perform(put(USER_BY_USERNAME_URL, USERNAME_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new UpdateUserAccountRequest(500, 1000)
                        ))
                )
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(message)));
    }

    @Test
    void deletes_user_account_by_username() throws Exception {
        // given
        doNothing().when(userAccountService).deleteByUsername(USERNAME_1);

        // then
        mockMvc.perform(delete(USER_BY_USERNAME_URL, USERNAME_1))
                .andExpect(status().isOk())
                .andExpect(content().string(blankString()));
    }

    @Test
    void returns_404_response_when_trying_to_delete_non_existing_user_account() throws Exception {
        // given
        String message = "Deletion failed. User '" + USERNAME_1 + "' couldn`t be found in database.";
        doThrow(new UserAccountNotFoundException(message)).when(userAccountService).deleteByUsername(any(String.class));

        // then
        mockMvc.perform(delete(USER_BY_USERNAME_URL, USERNAME_1))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(message)));
    }


    private ArgumentMatcher<UserAccount> matchCreateUserAccountRequestToEntity(CreateUserAccountRequest createUserAccountRequest) {
        return userAccount -> userAccount.getUsername().equals(createUserAccountRequest.username()) &&
                              userAccount.getBudget() == createUserAccountRequest.budget() &&
                              userAccount.getMonthlySalary() == createUserAccountRequest.monthlySalary();
    }

    private ArgumentMatcher<UserAccount> matchUpdateUserAccountRequestToEntity(UpdateUserAccountRequest updateUserAccountRequest) {
        return userAccount -> userAccount.getUsername().equals(USERNAME_1) &&
                              userAccount.getBudget() == updateUserAccountRequest.budget() &&
                              userAccount.getMonthlySalary() == updateUserAccountRequest.monthlySalary();
    }
}
