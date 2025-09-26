package bank.app.BankManagementApp.integration;

import bank.app.BankManagementApp.entity.Account;
import bank.app.BankManagementApp.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Account Integration Tests")
public class AccountIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create, retrieve, update and delete account - Complete CRUD flow")
    void shouldCreateRetrieveUpdateAndDeleteAccountCompleteCrudFlow() throws Exception {
        // Step 1: Create Account
        Account accountToCreate = new Account("John Doe", 5000.0);
        
        MvcResult createResult = mockMvc.perform(post("/account/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountToCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountHolderName").value("John Doe"))
                .andExpect(jsonPath("$.accountBalance").value(5000.0))
                .andExpect(jsonPath("$.accountNumber").exists())
                .andReturn();

        // Extract account number from response
        String responseContent = createResult.getResponse().getContentAsString();
        Account createdAccount = objectMapper.readValue(responseContent, Account.class);
        Long accountNumber = createdAccount.getAccountNumber();

        // Verify account was saved in database
        assertTrue(accountRepository.findById(accountNumber).isPresent());

        // Step 2: Retrieve Account by ID
        mockMvc.perform(get("/account/{accountNumber}", accountNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value(accountNumber))
                .andExpect(jsonPath("$.accountHolderName").value("John Doe"))
                .andExpect(jsonPath("$.accountBalance").value(5000.0));

        // Step 3: Retrieve All Accounts
        mockMvc.perform(get("/account/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].accountNumber").value(accountNumber));

        // Step 4: Deposit Money
        mockMvc.perform(put("/account/deposit/{accountNumber}/{amount}", accountNumber, 1000.0))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountBalance").value(6000.0));

        // Verify balance in database
        Account updatedAccount = accountRepository.findById(accountNumber).orElseThrow();
        assertEquals(6000.0, updatedAccount.getAccountBalance());

        // Step 5: Withdraw Money
        mockMvc.perform(put("/account/withdraw/{accountNumber}/{amount}", accountNumber, 500.0))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountBalance").value(5500.0));

        // Verify balance in database
        updatedAccount = accountRepository.findById(accountNumber).orElseThrow();
        assertEquals(5500.0, updatedAccount.getAccountBalance());

        // Step 6: Delete Account
        mockMvc.perform(delete("/account/delete/{accountNumber}", accountNumber))
                .andExpect(status().isOk())
                .andExpect(content().string("Account closed successfully"));

        // Verify account was deleted from database
        assertFalse(accountRepository.findById(accountNumber).isPresent());
    }

    @Test
    @DisplayName("Should handle multiple accounts operations")
    void shouldHandleMultipleAccountsOperations() throws Exception {
        // Create multiple accounts
        Account account1 = new Account("John Doe", 5000.0);
        Account account2 = new Account("Jane Smith", 10000.0);
        Account account3 = new Account("Bob Johnson", 7500.0);

        // Create first account
        MvcResult result1 = mockMvc.perform(post("/account/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(account1)))
                .andExpect(status().isCreated())
                .andReturn();

        // Create second account
        MvcResult result2 = mockMvc.perform(post("/account/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(account2)))
                .andExpect(status().isCreated())
                .andReturn();

        // Create third account
        MvcResult result3 = mockMvc.perform(post("/account/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(account3)))
                .andExpect(status().isCreated())
                .andReturn();

        // Extract account numbers
        Account created1 = objectMapper.readValue(result1.getResponse().getContentAsString(), Account.class);
        Account created2 = objectMapper.readValue(result2.getResponse().getContentAsString(), Account.class);
        Account created3 = objectMapper.readValue(result3.getResponse().getContentAsString(), Account.class);

        // Verify all accounts exist
        assertEquals(3, accountRepository.count());

        // Retrieve all accounts
        mockMvc.perform(get("/account/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));

        // Perform operations on different accounts
        mockMvc.perform(put("/account/deposit/{accountNumber}/{amount}", created1.getAccountNumber(), 1000.0))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountBalance").value(6000.0));

        mockMvc.perform(put("/account/withdraw/{accountNumber}/{amount}", created2.getAccountNumber(), 2000.0))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountBalance").value(8000.0));

        mockMvc.perform(put("/account/deposit/{accountNumber}/{amount}", created3.getAccountNumber(), 500.0))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountBalance").value(8000.0));

        // Verify final balances in database
        assertEquals(6000.0, accountRepository.findById(created1.getAccountNumber()).get().getAccountBalance());
        assertEquals(8000.0, accountRepository.findById(created2.getAccountNumber()).get().getAccountBalance());
        assertEquals(8000.0, accountRepository.findById(created3.getAccountNumber()).get().getAccountBalance());
    }

    @Test
    @DisplayName("Should handle concurrent deposits and withdrawals")
    void shouldHandleConcurrentDepositsAndWithdrawals() throws Exception {
        // Create account
        Account account = new Account("Concurrent Test", 10000.0);
        
        MvcResult createResult = mockMvc.perform(post("/account/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isCreated())
                .andReturn();

        Account createdAccount = objectMapper.readValue(createResult.getResponse().getContentAsString(), Account.class);
        Long accountNumber = createdAccount.getAccountNumber();

        // Perform multiple operations
        mockMvc.perform(put("/account/deposit/{accountNumber}/{amount}", accountNumber, 1000.0))
                .andExpect(status().isOk());

        mockMvc.perform(put("/account/withdraw/{accountNumber}/{amount}", accountNumber, 500.0))
                .andExpect(status().isOk());

        mockMvc.perform(put("/account/deposit/{accountNumber}/{amount}", accountNumber, 2000.0))
                .andExpect(status().isOk());

        mockMvc.perform(put("/account/withdraw/{accountNumber}/{amount}", accountNumber, 1500.0))
                .andExpect(status().isOk());

        // Final balance should be: 10000 + 1000 - 500 + 2000 - 1500 = 11000
        mockMvc.perform(get("/account/{accountNumber}", accountNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountBalance").value(11000.0));
    }

    @Test
    @DisplayName("Should handle error scenarios gracefully")
    void shouldHandleErrorScenariosGracefully() throws Exception {
        // Try to get non-existent account
        mockMvc.perform(get("/account/{accountNumber}", 999L))
                .andExpect(status().isInternalServerError());

        // Try to deposit to non-existent account
        mockMvc.perform(put("/account/deposit/{accountNumber}/{amount}", 999L, 1000.0))
                .andExpect(status().isInternalServerError());

        // Try to withdraw from non-existent account
        mockMvc.perform(put("/account/withdraw/{accountNumber}/{amount}", 999L, 1000.0))
                .andExpect(status().isInternalServerError());

        // Try to delete non-existent account
        mockMvc.perform(delete("/account/delete/{accountNumber}", 999L))
                .andExpect(status().isInternalServerError());

        // Try to create account with invalid JSON
        mockMvc.perform(post("/account/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"invalid\": \"json\" }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle edge cases with amounts")
    void shouldHandleEdgeCasesWithAmounts() throws Exception {
        // Create account with zero balance
        Account account = new Account("Edge Case Test", 0.0);
        
        MvcResult createResult = mockMvc.perform(post("/account/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isCreated())
                .andReturn();

        Account createdAccount = objectMapper.readValue(createResult.getResponse().getContentAsString(), Account.class);
        Long accountNumber = createdAccount.getAccountNumber();

        // Test zero amount operations
        mockMvc.perform(put("/account/deposit/{accountNumber}/{amount}", accountNumber, 0.0))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountBalance").value(0.0));

        mockMvc.perform(put("/account/withdraw/{accountNumber}/{amount}", accountNumber, 0.0))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountBalance").value(0.0));

        // Test negative amounts
        mockMvc.perform(put("/account/deposit/{accountNumber}/{amount}", accountNumber, -1000.0))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountBalance").value(-1000.0));

        mockMvc.perform(put("/account/withdraw/{accountNumber}/{amount}", accountNumber, -500.0))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountBalance").value(-500.0));

        // Test large amounts
        mockMvc.perform(put("/account/deposit/{accountNumber}/{amount}", accountNumber, 999999.99))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountBalance").value(999499.99));
    }

    @Test
    @DisplayName("Should maintain data consistency across operations")
    void shouldMaintainDataConsistencyAcrossOperations() throws Exception {
        // Create account
        Account account = new Account("Consistency Test", 1000.0);
        
        MvcResult createResult = mockMvc.perform(post("/account/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isCreated())
                .andReturn();

        Account createdAccount = objectMapper.readValue(createResult.getResponse().getContentAsString(), Account.class);
        Long accountNumber = createdAccount.getAccountNumber();

        // Perform operations and verify consistency
        for (int i = 0; i < 10; i++) {
            // Deposit 100
            mockMvc.perform(put("/account/deposit/{accountNumber}/{amount}", accountNumber, 100.0))
                    .andExpect(status().isOk());

            // Verify balance
            Account currentAccount = accountRepository.findById(accountNumber).orElseThrow();
            assertEquals(1000.0 + (i + 1) * 100.0, currentAccount.getAccountBalance());
        }

        // Final balance should be 2000
        assertEquals(2000.0, accountRepository.findById(accountNumber).get().getAccountBalance());
    }
}
