package bank.app.BankManagementApp.service;

import bank.app.BankManagementApp.entity.Account;
import bank.app.BankManagementApp.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account getAccountDetailsByAccountNumber(Long accountNumber) {
        return accountRepository.findById(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account depositAmount(Long accountNumber, Double amount) {
        Account account = getAccountDetailsByAccountNumber(accountNumber);
        account.setAccountBalance(account.getAccountBalance() + amount);
        return accountRepository.save(account);
    }

    @Override
    public Account withdrawAmount(Long accountNumber, Double amount) {
        Account account = getAccountDetailsByAccountNumber(accountNumber);
        account.setAccountBalance(account.getAccountBalance() - amount);
        return accountRepository.save(account);
    }

    @Override
    public void closeAccount(Long accountNumber) {
        getAccountDetailsByAccountNumber(accountNumber);
        accountRepository.deleteById(accountNumber);
    }
}
