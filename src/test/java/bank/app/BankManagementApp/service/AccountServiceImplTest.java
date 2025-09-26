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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AccountServiceImpl Tests")
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account testAccount;
    private List<Account> testAccounts;

    @BeforeEach
    void setUp() {
        testAccount = new Account("John Doe", 5000.0);
        testAccount.setAccountNumber(1L);
        
        testAccounts = Arrays.asList(
            new Account("John Doe", 5000.0),
            new Account("Jane Smith", 10000.0),
            new Account("Bob Johnson", 7500.0)
        );
    }

    @Test
    @DisplayName("Should create account successfully")
    void shouldCreateAccountSuccessfully() {
        // Given
        Account inputAccount = new Account("John Doe", 5000.0);
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        Account result = accountService.createAccount(inputAccount);

        // Then
        assertNotNull(result);
        assertEquals(testAccount.getAccountNumber(), result.getAccountNumber());
        assertEquals(testAccount.getAccountHolderName(), result.getAccountHolderName());
        assertEquals(testAccount.getAccountBalance(), result.getAccountBalance());
        verify(accountRepository, times(1)).save(inputAccount);
    }

    @Test
    @DisplayName("Should get account details by account number successfully")
    void shouldGetAccountDetailsByAccountNumberSuccessfully() {
        // Given
        Long accountNumber = 1L;
        when(accountRepository.findById(accountNumber)).thenReturn(Optional.of(testAccount));

        // When
        Account result = accountService.getAccountDetailsByAccountNumber(accountNumber);

        // Then
        assertNotNull(result);
        assertEquals(testAccount.getAccountNumber(), result.getAccountNumber());
        assertEquals(testAccount.getAccountHolderName(), result.getAccountHolderName());
        assertEquals(testAccount.getAccountBalance(), result.getAccountBalance());
        verify(accountRepository, times(1)).findById(accountNumber);
    }

    @Test
    @DisplayName("Should throw RuntimeException when account not found")
    void shouldThrowRuntimeExceptionWhenAccountNotFound() {
        // Given
        Long accountNumber = 999L;
        when(accountRepository.findById(accountNumber)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            accountService.getAccountDetailsByAccountNumber(accountNumber);
        });

        assertEquals("Account not found", exception.getMessage());
        verify(accountRepository, times(1)).findById(accountNumber);
    }

    @Test
    @DisplayName("Should get all accounts successfully")
    void shouldGetAllAccountsSuccessfully() {
        // Given
        when(accountRepository.findAll()).thenReturn(testAccounts);

        // When
        List<Account> result = accountService.getAllAccounts();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no accounts exist")
    void shouldReturnEmptyListWhenNoAccountsExist() {
        // Given
        when(accountRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Account> result = accountService.getAllAccounts();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should deposit amount successfully")
    void shouldDepositAmountSuccessfully() {
        // Given
        Long accountNumber = 1L;
        Double depositAmount = 1000.0;
        Double expectedBalance = 6000.0;
        
        when(accountRepository.findById(accountNumber)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        Account result = accountService.depositAmount(accountNumber, depositAmount);

        // Then
        assertNotNull(result);
        assertEquals(expectedBalance, result.getAccountBalance());
        verify(accountRepository, times(1)).findById(accountNumber);
        verify(accountRepository, times(1)).save(testAccount);
    }

    @Test
    @DisplayName("Should throw RuntimeException when depositing to non-existent account")
    void shouldThrowRuntimeExceptionWhenDepositingToNonExistentAccount() {
        // Given
        Long accountNumber = 999L;
        Double depositAmount = 1000.0;
        when(accountRepository.findById(accountNumber)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            accountService.depositAmount(accountNumber, depositAmount);
        });

        assertEquals("Account not found", exception.getMessage());
        verify(accountRepository, times(1)).findById(accountNumber);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    @DisplayName("Should withdraw amount successfully")
    void shouldWithdrawAmountSuccessfully() {
        // Given
        Long accountNumber = 1L;
        Double withdrawAmount = 1000.0;
        Double expectedBalance = 4000.0;
        
        when(accountRepository.findById(accountNumber)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        Account result = accountService.withdrawAmount(accountNumber, withdrawAmount);

        // Then
        assertNotNull(result);
        assertEquals(expectedBalance, result.getAccountBalance());
        verify(accountRepository, times(1)).findById(accountNumber);
        verify(accountRepository, times(1)).save(testAccount);
    }

    @Test
    @DisplayName("Should throw RuntimeException when withdrawing from non-existent account")
    void shouldThrowRuntimeExceptionWhenWithdrawingFromNonExistentAccount() {
        // Given
        Long accountNumber = 999L;
        Double withdrawAmount = 1000.0;
        when(accountRepository.findById(accountNumber)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            accountService.withdrawAmount(accountNumber, withdrawAmount);
        });

        assertEquals("Account not found", exception.getMessage());
        verify(accountRepository, times(1)).findById(accountNumber);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    @DisplayName("Should handle withdrawal resulting in negative balance")
    void shouldHandleWithdrawalResultingInNegativeBalance() {
        // Given
        Long accountNumber = 1L;
        Double withdrawAmount = 10000.0; // More than available balance
        Double expectedBalance = -5000.0;
        
        when(accountRepository.findById(accountNumber)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        Account result = accountService.withdrawAmount(accountNumber, withdrawAmount);

        // Then
        assertNotNull(result);
        assertEquals(expectedBalance, result.getAccountBalance());
        verify(accountRepository, times(1)).findById(accountNumber);
        verify(accountRepository, times(1)).save(testAccount);
    }

    @Test
    @DisplayName("Should close account successfully")
    void shouldCloseAccountSuccessfully() {
        // Given
        Long accountNumber = 1L;
        when(accountRepository.findById(accountNumber)).thenReturn(Optional.of(testAccount));
        doNothing().when(accountRepository).deleteById(accountNumber);

        // When
        accountService.closeAccount(accountNumber);

        // Then
        verify(accountRepository, times(1)).findById(accountNumber);
        verify(accountRepository, times(1)).deleteById(accountNumber);
    }

    @Test
    @DisplayName("Should throw RuntimeException when closing non-existent account")
    void shouldThrowRuntimeExceptionWhenClosingNonExistentAccount() {
        // Given
        Long accountNumber = 999L;
        when(accountRepository.findById(accountNumber)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            accountService.closeAccount(accountNumber);
        });

        assertEquals("Account not found", exception.getMessage());
        verify(accountRepository, times(1)).findById(accountNumber);
        verify(accountRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should handle deposit with zero amount")
    void shouldHandleDepositWithZeroAmount() {
        // Given
        Long accountNumber = 1L;
        Double depositAmount = 0.0;
        Double expectedBalance = 5000.0; // Should remain the same
        
        when(accountRepository.findById(accountNumber)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        Account result = accountService.depositAmount(accountNumber, depositAmount);

        // Then
        assertNotNull(result);
        assertEquals(expectedBalance, result.getAccountBalance());
        verify(accountRepository, times(1)).findById(accountNumber);
        verify(accountRepository, times(1)).save(testAccount);
    }

    @Test
    @DisplayName("Should handle withdrawal with zero amount")
    void shouldHandleWithdrawalWithZeroAmount() {
        // Given
        Long accountNumber = 1L;
        Double withdrawAmount = 0.0;
        Double expectedBalance = 5000.0; // Should remain the same
        
        when(accountRepository.findById(accountNumber)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        Account result = accountService.withdrawAmount(accountNumber, withdrawAmount);

        // Then
        assertNotNull(result);
        assertEquals(expectedBalance, result.getAccountBalance());
        verify(accountRepository, times(1)).findById(accountNumber);
        verify(accountRepository, times(1)).save(testAccount);
    }

    @Test
    @DisplayName("Should handle deposit with negative amount")
    void shouldHandleDepositWithNegativeAmount() {
        // Given
        Long accountNumber = 1L;
        Double depositAmount = -1000.0; // Negative deposit (should reduce balance)
        Double expectedBalance = 4000.0;
        
        when(accountRepository.findById(accountNumber)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        Account result = accountService.depositAmount(accountNumber, depositAmount);

        // Then
        assertNotNull(result);
        assertEquals(expectedBalance, result.getAccountBalance());
        verify(accountRepository, times(1)).findById(accountNumber);
        verify(accountRepository, times(1)).save(testAccount);
    }
}
