package bank.app.BankManagementApp.repository;

import bank.app.BankManagementApp.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("AccountRepository Tests")
public class AccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    private Account account1;
    private Account account2;

    @BeforeEach
    void setUp() {
        // Create test accounts
        account1 = new Account("John Doe", 5000.0);
        account2 = new Account("Jane Smith", 10000.0);
    }

    @Test
    @DisplayName("Should save account successfully")
    void shouldSaveAccountSuccessfully() {
        // When
        Account savedAccount = accountRepository.save(account1);

        // Then
        assertNotNull(savedAccount);
        assertNotNull(savedAccount.getAccountNumber());
        assertEquals(account1.getAccountHolderName(), savedAccount.getAccountHolderName());
        assertEquals(account1.getAccountBalance(), savedAccount.getAccountBalance());
    }

    @Test
    @DisplayName("Should find account by ID")
    void shouldFindAccountById() {
        // Given
        Account savedAccount = entityManager.persistAndFlush(account1);

        // When
        Optional<Account> foundAccount = accountRepository.findById(savedAccount.getAccountNumber());

        // Then
        assertTrue(foundAccount.isPresent());
        assertEquals(savedAccount.getAccountNumber(), foundAccount.get().getAccountNumber());
        assertEquals(savedAccount.getAccountHolderName(), foundAccount.get().getAccountHolderName());
        assertEquals(savedAccount.getAccountBalance(), foundAccount.get().getAccountBalance());
    }

    @Test
    @DisplayName("Should return empty optional when account not found")
    void shouldReturnEmptyOptionalWhenAccountNotFound() {
        // Given
        Long nonExistentId = 999L;

        // When
        Optional<Account> foundAccount = accountRepository.findById(nonExistentId);

        // Then
        assertFalse(foundAccount.isPresent());
    }

    @Test
    @DisplayName("Should find all accounts")
    void shouldFindAllAccounts() {
        // Given
        entityManager.persistAndFlush(account1);
        entityManager.persistAndFlush(account2);

        // When
        List<Account> allAccounts = accountRepository.findAll();

        // Then
        assertNotNull(allAccounts);
        assertEquals(2, allAccounts.size());
    }

    @Test
    @DisplayName("Should return empty list when no accounts exist")
    void shouldReturnEmptyListWhenNoAccountsExist() {
        // When
        List<Account> allAccounts = accountRepository.findAll();

        // Then
        assertNotNull(allAccounts);
        assertTrue(allAccounts.isEmpty());
    }

    @Test
    @DisplayName("Should update existing account")
    void shouldUpdateExistingAccount() {
        // Given
        Account savedAccount = entityManager.persistAndFlush(account1);
        savedAccount.setAccountBalance(7500.0);
        savedAccount.setAccountHolderName("John Updated");

        // When
        Account updatedAccount = accountRepository.save(savedAccount);

        // Then
        assertNotNull(updatedAccount);
        assertEquals(savedAccount.getAccountNumber(), updatedAccount.getAccountNumber());
        assertEquals("John Updated", updatedAccount.getAccountHolderName());
        assertEquals(7500.0, updatedAccount.getAccountBalance());
    }

    @Test
    @DisplayName("Should delete account by ID")
    void shouldDeleteAccountById() {
        // Given
        Account savedAccount = entityManager.persistAndFlush(account1);
        Long accountId = savedAccount.getAccountNumber();

        // When
        accountRepository.deleteById(accountId);

        // Then
        Optional<Account> deletedAccount = accountRepository.findById(accountId);
        assertFalse(deletedAccount.isPresent());
    }

    @Test
    @DisplayName("Should count total accounts")
    void shouldCountTotalAccounts() {
        // Given
        entityManager.persistAndFlush(account1);
        entityManager.persistAndFlush(account2);

        // When
        long count = accountRepository.count();

        // Then
        assertEquals(2, count);
    }

    @Test
    @DisplayName("Should return zero count when no accounts exist")
    void shouldReturnZeroCountWhenNoAccountsExist() {
        // When
        long count = accountRepository.count();

        // Then
        assertEquals(0, count);
    }

    @Test
    @DisplayName("Should check if account exists by ID")
    void shouldCheckIfAccountExistsById() {
        // Given
        Account savedAccount = entityManager.persistAndFlush(account1);

        // When
        boolean exists = accountRepository.existsById(savedAccount.getAccountNumber());

        // Then
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false when account does not exist")
    void shouldReturnFalseWhenAccountDoesNotExist() {
        // Given
        Long nonExistentId = 999L;

        // When
        boolean exists = accountRepository.existsById(nonExistentId);

        // Then
        assertFalse(exists);
    }

    @Test
    @DisplayName("Should handle account with null balance")
    void shouldHandleAccountWithNullBalance() {
        // Given
        Account accountWithNullBalance = new Account("Test User", null);

        // When
        Account savedAccount = accountRepository.save(accountWithNullBalance);

        // Then
        assertNotNull(savedAccount);
        assertNull(savedAccount.getAccountBalance());
    }

    @Test
    @DisplayName("Should handle account with very long holder name")
    void shouldHandleAccountWithVeryLongHolderName() {
        // Given
        String longName = "A".repeat(255); // Assuming 255 is max length
        Account accountWithLongName = new Account(longName, 1000.0);

        // When
        Account savedAccount = accountRepository.save(accountWithLongName);

        // Then
        assertNotNull(savedAccount);
        assertEquals(longName, savedAccount.getAccountHolderName());
    }
}
