package bank.app.BankManagementApp.service;

import bank.app.BankManagementApp.entity.Account;
import java.util.List;

public interface AccountService {
    Account createAccount(Account account);
    Account getAccountDetailsByAccountNumber(Long accountNumber);
    List<Account> getAllAccounts();
    Account depositAmount(Long accountNumber, Double amount);
    Account withdrawAmount(Long accountNumber, Double amount);
    void closeAccount(Long accountNumber);
}
