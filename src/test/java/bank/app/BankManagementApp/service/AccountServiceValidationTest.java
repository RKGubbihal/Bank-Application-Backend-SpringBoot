package bank.app.BankManagementApp.service;

import bank.app.BankManagementApp.entity.Account;
import bank.app.BankManagementApp.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AccountService Validation Tests")
public class AccountServiceValidationTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account testAccount;

    @BeforeEach
    void setUp() {
        testAccount = new Account("John Doe", 5000.0);
        testAccount.setAccountNumber(1L);
    }

    @Test
    @DisplayName("Should handle null account holder name")
    void shouldHandleNullAccountHolderName() {
        // Given
        Account accountWithNullName = new Account(null, 1000.0);
        when(accountRepository.save(any(Account.class))).thenReturn(accountWithNullName);

        // When
        Account result = accountService.createAccount(accountWithNullName);

        // Then
        assertNotNull(result);
        assertNull(result.getAccountHolderName());
        verify(accountRepository, times(1)).save(accountWithNullName);
    }

    @Test
    @DisplayName("Should handle empty account holder name")
    void shouldHandleEmptyAccountHolderName() {
        // Given
        Account accountWithEmptyName = new Account("", 1000.0);
        when(accountRepository.save(any(Account.class))).thenReturn(accountWithEmptyName);

        // When
        Account result = accountService.createAccount(accountWithEmptyName);

        // Then
        assertNotNull(result);
        assertEquals("", result.getAccountHolderName());
        verify(accountRepository, times(1)).save(accountWithEmptyName);
    }

    @Test
    @DisplayName("Should handle null account balance")
    void shouldHandleNullAccountBalance() {
        // Given
        Account accountWithNullBalance = new Account("John Doe", null);
        when(accountRepository.save(any(Account.class))).thenReturn(accountWithNullBalance);

        // When
        Account result = accountService.createAccount(accountWithNullBalance);

        // Then
        assertNotNull(result);
        assertNull(result.getAccountBalance());
        verify(accountRepository, times(1)).save(accountWithNullBalance);
    }

    @Test
    @DisplayName("Should handle very large deposit amounts")
    void shouldHandleVeryLargeDepositAmounts() {
        // Given
        Long accountNumber = 1L;
        Double largeAmount = Double.MAX_VALUE;
        when(accountRepository.findById(accountNumber)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        Account result = accountService.depositAmount(accountNumber, largeAmount);

        // Then
        assertNotNull(result);
        verify(accountRepository, times(1)).findById(accountNumber);
        verify(accountRepository, times(1)).save(testAccount);
    }

    @Test
    @DisplayName("Should handle very large withdrawal amounts")
    void shouldHandleVeryLargeWithdrawalAmounts() {
        // Given
        Long accountNumber = 1L;
        Double largeAmount = Double.MAX_VALUE;
        when(accountRepository.findById(accountNumber)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        Account result = accountService.withdrawAmount(accountNumber, largeAmount);

        // Then
        assertNotNull(result);
        verify(accountRepository, times(1)).findById(accountNumber);
        verify(accountRepository, times(1)).save(testAccount);
    }

    @Test
    @DisplayName("Should handle precision in decimal amounts")
    void shouldHandlePrecisionInDecimalAmounts() {
        // Given
        Long accountNumber = 1L;
        Double preciseAmount = 0.01;
        when(accountRepository.findById(accountNumber)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        Account result = accountService.depositAmount(accountNumber, preciseAmount);

        // Then
        assertNotNull(result);
        verify(accountRepository, times(1)).findById(accountNumber);
        verify(accountRepository, times(1)).save(testAccount);
    }

    @Test
    @DisplayName("Should handle very small amounts")
    void shouldHandleVerySmallAmounts() {
        // Given
        Long accountNumber = 1L;
        Double smallAmount = Double.MIN_VALUE;
        when(accountRepository.findById(accountNumber)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        Account result = accountService.depositAmount(accountNumber, smallAmount);

        // Then
        assertNotNull(result);
        verify(accountRepository, times(1)).findById(accountNumber);
        verify(accountRepository, times(1)).save(testAccount);
    }

    @Test
    @DisplayName("Should handle account with special characters in name")
    void shouldHandleAccountWithSpecialCharactersInName() {
        // Given
        String nameWithSpecialChars = "John O'Connor-Smith Jr. & Co.";
        Account accountWithSpecialName = new Account(nameWithSpecialChars, 1000.0);
        when(accountRepository.save(any(Account.class))).thenReturn(accountWithSpecialName);

        // When
        Account result = accountService.createAccount(accountWithSpecialName);

        // Then
        assertNotNull(result);
        assertEquals(nameWithSpecialChars, result.getAccountHolderName());
        verify(accountRepository, times(1)).save(accountWithSpecialName);
    }

    @Test
    @DisplayName("Should handle account with unicode characters in name")
    void shouldHandleAccountWithUnicodeCharactersInName() {
        // Given
        String nameWithUnicode = "José María García-López";
        Account accountWithUnicodeName = new Account(nameWithUnicode, 1000.0);
        when(accountRepository.save(any(Account.class))).thenReturn(accountWithUnicodeName);

        // When
        Account result = accountService.createAccount(accountWithUnicodeName);

        // Then
        assertNotNull(result);
        assertEquals(nameWithUnicode, result.getAccountHolderName());
        verify(accountRepository, times(1)).save(accountWithUnicodeName);
    }

    @Test
    @DisplayName("Should handle account with very long name")
    void shouldHandleAccountWithVeryLongName() {
        // Given
        String longName = "A".repeat(1000); // Very long name
        Account accountWithLongName = new Account(longName, 1000.0);
        when(accountRepository.save(any(Account.class))).thenReturn(accountWithLongName);

        // When
        Account result = accountService.createAccount(accountWithLongName);

        // Then
        assertNotNull(result);
        assertEquals(longName, result.getAccountHolderName());
        verify(accountRepository, times(1)).save(accountWithLongName);
    }

    @Test
    @DisplayName("Should handle multiple operations on same account")
    void shouldHandleMultipleOperationsOnSameAccount() {
        // Given
        Long accountNumber = 1L;
        when(accountRepository.findById(accountNumber)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When - Perform multiple operations
        accountService.depositAmount(accountNumber, 100.0);
        accountService.withdrawAmount(accountNumber, 50.0);
        accountService.depositAmount(accountNumber, 200.0);
        accountService.withdrawAmount(accountNumber, 25.0);

        // Then
        verify(accountRepository, times(4)).findById(accountNumber);
        verify(accountRepository, times(4)).save(testAccount);
    }

    @Test
    @DisplayName("Should handle edge case with Double.NaN amounts")
    void shouldHandleEdgeCaseWithDoubleNaNAmounts() {
        // Given
        Long accountNumber = 1L;
        Double nanAmount = Double.NaN;
        when(accountRepository.findById(accountNumber)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        Account result = accountService.depositAmount(accountNumber, nanAmount);

        // Then
        assertNotNull(result);
        verify(accountRepository, times(1)).findById(accountNumber);
        verify(accountRepository, times(1)).save(testAccount);
    }

    @Test
    @DisplayName("Should handle edge case with Double.POSITIVE_INFINITY amounts")
    void shouldHandleEdgeCaseWithDoublePositiveInfinityAmounts() {
        // Given
        Long accountNumber = 1L;
        Double infinityAmount = Double.POSITIVE_INFINITY;
        when(accountRepository.findById(accountNumber)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        Account result = accountService.depositAmount(accountNumber, infinityAmount);

        // Then
        assertNotNull(result);
        verify(accountRepository, times(1)).findById(accountNumber);
        verify(accountRepository, times(1)).save(testAccount);
    }

    @Test
    @DisplayName("Should handle edge case with Double.NEGATIVE_INFINITY amounts")
    void shouldHandleEdgeCaseWithDoubleNegativeInfinityAmounts() {
        // Given
        Long accountNumber = 1L;
        Double negativeInfinityAmount = Double.NEGATIVE_INFINITY;
        when(accountRepository.findById(accountNumber)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        Account result = accountService.depositAmount(accountNumber, negativeInfinityAmount);

        // Then
        assertNotNull(result);
        verify(accountRepository, times(1)).findById(accountNumber);
        verify(accountRepository, times(1)).save(testAccount);
    }
}
