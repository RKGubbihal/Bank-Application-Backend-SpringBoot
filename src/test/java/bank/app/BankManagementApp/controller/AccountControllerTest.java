package bank.app.BankManagementApp.controller;

import bank.app.BankManagementApp.entity.Account;
import bank.app.BankManagementApp.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
@DisplayName("AccountController Tests")
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    private Account testAccount;
    private List<Account> testAccounts;

    @BeforeEach
    void setUp() {
        testAccount = new Account("John Doe", 5000.0);
        testAccount.setAccountNumber(1L);
        
        testAccounts = Arrays.asList(
            new Account("John Doe", 5000.0),
            new Account("Jane Smith", 10000.0)
        );
    }

    @Test
    @DisplayName("Should create account successfully")
    void shouldCreateAccountSuccessfully() throws Exception {
        // Given
        Account inputAccount = new Account("John Doe", 5000.0);
        when(accountService.createAccount(any(Account.class))).thenReturn(testAccount);

        // When & Then
        mockMvc.perform(post("/account/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputAccount)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountNumber").value(1))
                .andExpect(jsonPath("$.accountHolderName").value("John Doe"))
                .andExpect(jsonPath("$.accountBalance").value(5000.0));

        verify(accountService, times(1)).createAccount(any(Account.class));
    }

    @Test
    @DisplayName("Should return bad request when creating account with invalid data")
    void shouldReturnBadRequestWhenCreatingAccountWithInvalidData() throws Exception {
        // Given
        String invalidJson = "{ \"invalid\": \"data\" }";

        // When & Then
        mockMvc.perform(post("/account/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should get account by ID successfully")
    void shouldGetAccountByIdSuccessfully() throws Exception {
        // Given
        Long accountNumber = 1L;
        when(accountService.getAccountDetailsByAccountNumber(accountNumber)).thenReturn(testAccount);

        // When & Then
        mockMvc.perform(get("/account/{accountNumber}", accountNumber)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountNumber").value(1))
                .andExpect(jsonPath("$.accountHolderName").value("John Doe"))
                .andExpect(jsonPath("$.accountBalance").value(5000.0));

        verify(accountService, times(1)).getAccountDetailsByAccountNumber(accountNumber);
    }

    @Test
    @DisplayName("Should return internal server error when account not found")
    void shouldReturnInternalServerErrorWhenAccountNotFound() throws Exception {
        // Given
        Long accountNumber = 999L;
        when(accountService.getAccountDetailsByAccountNumber(accountNumber))
                .thenThrow(new RuntimeException("Account not found"));

        // When & Then
        mockMvc.perform(get("/account/{accountNumber}", accountNumber)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(accountService, times(1)).getAccountDetailsByAccountNumber(accountNumber);
    }

    @Test
    @DisplayName("Should get all accounts successfully")
    void shouldGetAllAccountsSuccessfully() throws Exception {
        // Given
        when(accountService.getAllAccounts()).thenReturn(testAccounts);

        // When & Then
        mockMvc.perform(get("/account/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].accountHolderName").value("John Doe"))
                .andExpect(jsonPath("$[1].accountHolderName").value("Jane Smith"));

        verify(accountService, times(1)).getAllAccounts();
    }

    @Test
    @DisplayName("Should return empty array when no accounts exist")
    void shouldReturnEmptyArrayWhenNoAccountsExist() throws Exception {
        // Given
        when(accountService.getAllAccounts()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/account/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(accountService, times(1)).getAllAccounts();
    }

    @Test
    @DisplayName("Should deposit amount successfully")
    void shouldDepositAmountSuccessfully() throws Exception {
        // Given
        Long accountNumber = 1L;
        Double depositAmount = 1000.0;
        Account updatedAccount = new Account("John Doe", 6000.0);
        updatedAccount.setAccountNumber(1L);
        
        when(accountService.depositAmount(accountNumber, depositAmount)).thenReturn(updatedAccount);

        // When & Then
        mockMvc.perform(put("/account/deposit/{accountNumber}/{amount}", accountNumber, depositAmount)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountNumber").value(1))
                .andExpect(jsonPath("$.accountBalance").value(6000.0));

        verify(accountService, times(1)).depositAmount(accountNumber, depositAmount);
    }

    @Test
    @DisplayName("Should return internal server error when depositing to non-existent account")
    void shouldReturnInternalServerErrorWhenDepositingToNonExistentAccount() throws Exception {
        // Given
        Long accountNumber = 999L;
        Double depositAmount = 1000.0;
        when(accountService.depositAmount(accountNumber, depositAmount))
                .thenThrow(new RuntimeException("Account not found"));

        // When & Then
        mockMvc.perform(put("/account/deposit/{accountNumber}/{amount}", accountNumber, depositAmount)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(accountService, times(1)).depositAmount(accountNumber, depositAmount);
    }

    @Test
    @DisplayName("Should withdraw amount successfully")
    void shouldWithdrawAmountSuccessfully() throws Exception {
        // Given
        Long accountNumber = 1L;
        Double withdrawAmount = 1000.0;
        Account updatedAccount = new Account("John Doe", 4000.0);
        updatedAccount.setAccountNumber(1L);
        
        when(accountService.withdrawAmount(accountNumber, withdrawAmount)).thenReturn(updatedAccount);

        // When & Then
        mockMvc.perform(put("/account/withdraw/{accountNumber}/{amount}", accountNumber, withdrawAmount)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountNumber").value(1))
                .andExpect(jsonPath("$.accountBalance").value(4000.0));

        verify(accountService, times(1)).withdrawAmount(accountNumber, withdrawAmount);
    }

    @Test
    @DisplayName("Should return internal server error when withdrawing from non-existent account")
    void shouldReturnInternalServerErrorWhenWithdrawingFromNonExistentAccount() throws Exception {
        // Given
        Long accountNumber = 999L;
        Double withdrawAmount = 1000.0;
        when(accountService.withdrawAmount(accountNumber, withdrawAmount))
                .thenThrow(new RuntimeException("Account not found"));

        // When & Then
        mockMvc.perform(put("/account/withdraw/{accountNumber}/{amount}", accountNumber, withdrawAmount)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(accountService, times(1)).withdrawAmount(accountNumber, withdrawAmount);
    }

    @Test
    @DisplayName("Should delete account successfully")
    void shouldDeleteAccountSuccessfully() throws Exception {
        // Given
        Long accountNumber = 1L;
        doNothing().when(accountService).closeAccount(accountNumber);

        // When & Then
        mockMvc.perform(delete("/account/delete/{accountNumber}", accountNumber)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Account closed successfully"));

        verify(accountService, times(1)).closeAccount(accountNumber);
    }

    @Test
    @DisplayName("Should return internal server error when deleting non-existent account")
    void shouldReturnInternalServerErrorWhenDeletingNonExistentAccount() throws Exception {
        // Given
        Long accountNumber = 999L;
        doThrow(new RuntimeException("Account not found")).when(accountService).closeAccount(accountNumber);

        // When & Then
        mockMvc.perform(delete("/account/delete/{accountNumber}", accountNumber)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(accountService, times(1)).closeAccount(accountNumber);
    }

    @Test
    @DisplayName("Should handle deposit with zero amount")
    void shouldHandleDepositWithZeroAmount() throws Exception {
        // Given
        Long accountNumber = 1L;
        Double depositAmount = 0.0;
        when(accountService.depositAmount(accountNumber, depositAmount)).thenReturn(testAccount);

        // When & Then
        mockMvc.perform(put("/account/deposit/{accountNumber}/{amount}", accountNumber, depositAmount)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountBalance").value(5000.0));

        verify(accountService, times(1)).depositAmount(accountNumber, depositAmount);
    }

    @Test
    @DisplayName("Should handle withdrawal with zero amount")
    void shouldHandleWithdrawalWithZeroAmount() throws Exception {
        // Given
        Long accountNumber = 1L;
        Double withdrawAmount = 0.0;
        when(accountService.withdrawAmount(accountNumber, withdrawAmount)).thenReturn(testAccount);

        // When & Then
        mockMvc.perform(put("/account/withdraw/{accountNumber}/{amount}", accountNumber, withdrawAmount)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountBalance").value(5000.0));

        verify(accountService, times(1)).withdrawAmount(accountNumber, withdrawAmount);
    }

    @Test
    @DisplayName("Should handle invalid path variables")
    void shouldHandleInvalidPathVariables() throws Exception {
        // Given
        String invalidAccountNumber = "invalid";
        Double amount = 1000.0;

        // When & Then
        mockMvc.perform(put("/account/deposit/{accountNumber}/{amount}", invalidAccountNumber, amount)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle negative amounts in deposit")
    void shouldHandleNegativeAmountsInDeposit() throws Exception {
        // Given
        Long accountNumber = 1L;
        Double negativeAmount = -1000.0;
        Account updatedAccount = new Account("John Doe", 4000.0); // 5000 + (-1000) = 4000
        updatedAccount.setAccountNumber(1L);
        
        when(accountService.depositAmount(accountNumber, negativeAmount)).thenReturn(updatedAccount);

        // When & Then
        mockMvc.perform(put("/account/deposit/{accountNumber}/{amount}", accountNumber, negativeAmount)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountBalance").value(4000.0));

        verify(accountService, times(1)).depositAmount(accountNumber, negativeAmount);
    }
}
