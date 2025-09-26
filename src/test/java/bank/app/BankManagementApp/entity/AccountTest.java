package bank.app.BankManagementApp.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Account Entity Tests")
public class AccountTest {

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
    }

    @Test
    @DisplayName("Should create account with default constructor")
    void shouldCreateAccountWithDefaultConstructor() {
        assertNotNull(account);
        assertNull(account.getAccountNumber());
        assertNull(account.getAccountHolderName());
        assertNull(account.getAccountBalance());
    }

    @Test
    @DisplayName("Should create account with parameterized constructor")
    void shouldCreateAccountWithParameterizedConstructor() {
        // Given
        String holderName = "John Doe";
        Double balance = 5000.0;

        // When
        Account newAccount = new Account(holderName, balance);

        // Then
        assertNotNull(newAccount);
        assertEquals(holderName, newAccount.getAccountHolderName());
        assertEquals(balance, newAccount.getAccountBalance());
        assertNull(newAccount.getAccountNumber()); // ID should be null initially
    }

    @Test
    @DisplayName("Should set and get account number")
    void shouldSetAndGetAccountNumber() {
        // Given
        Long accountNumber = 12345L;

        // When
        account.setAccountNumber(accountNumber);

        // Then
        assertEquals(accountNumber, account.getAccountNumber());
    }

    @Test
    @DisplayName("Should set and get account holder name")
    void shouldSetAndGetAccountHolderName() {
        // Given
        String holderName = "Jane Smith";

        // When
        account.setAccountHolderName(holderName);

        // Then
        assertEquals(holderName, account.getAccountHolderName());
    }

    @Test
    @DisplayName("Should set and get account balance")
    void shouldSetAndGetAccountBalance() {
        // Given
        Double balance = 10000.0;

        // When
        account.setAccountBalance(balance);

        // Then
        assertEquals(balance, account.getAccountBalance());
    }

    @Test
    @DisplayName("Should handle null values")
    void shouldHandleNullValues() {
        // When
        account.setAccountNumber(null);
        account.setAccountHolderName(null);
        account.setAccountBalance(null);

        // Then
        assertNull(account.getAccountNumber());
        assertNull(account.getAccountHolderName());
        assertNull(account.getAccountBalance());
    }

    @Test
    @DisplayName("Should return correct toString representation")
    void shouldReturnCorrectToStringRepresentation() {
        // Given
        Long accountNumber = 12345L;
        String holderName = "John Doe";
        Double balance = 5000.0;

        account.setAccountNumber(accountNumber);
        account.setAccountHolderName(holderName);
        account.setAccountBalance(balance);

        // When
        String toStringResult = account.toString();

        // Then
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("accountNumber=" + accountNumber));
        assertTrue(toStringResult.contains("accountHolderName='" + holderName + "'"));
        assertTrue(toStringResult.contains("accountBalance=" + balance));
    }

    @Test
    @DisplayName("Should handle zero balance")
    void shouldHandleZeroBalance() {
        // Given
        Double zeroBalance = 0.0;

        // When
        account.setAccountBalance(zeroBalance);

        // Then
        assertEquals(zeroBalance, account.getAccountBalance());
    }

    @Test
    @DisplayName("Should handle negative balance")
    void shouldHandleNegativeBalance() {
        // Given
        Double negativeBalance = -100.0;

        // When
        account.setAccountBalance(negativeBalance);

        // Then
        assertEquals(negativeBalance, account.getAccountBalance());
    }

    @Test
    @DisplayName("Should handle large balance values")
    void shouldHandleLargeBalanceValues() {
        // Given
        Double largeBalance = 999999999.99;

        // When
        account.setAccountBalance(largeBalance);

        // Then
        assertEquals(largeBalance, account.getAccountBalance());
    }

    @Test
    @DisplayName("Should handle empty account holder name")
    void shouldHandleEmptyAccountHolderName() {
        // Given
        String emptyName = "";

        // When
        account.setAccountHolderName(emptyName);

        // Then
        assertEquals(emptyName, account.getAccountHolderName());
    }
}
