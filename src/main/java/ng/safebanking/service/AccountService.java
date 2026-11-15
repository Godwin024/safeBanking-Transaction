package ng.safebanking.service;


import ng.safebanking.data.models.Account;
import ng.safebanking.data.repositories.AccountRepository;
import ng.safebanking.dtos.request.CreateAccountRequest;
import ng.safebanking.dtos.response.AccountResponse;
import ng.safebanking.exception.InvalidAccountException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service

public class AccountService {

    private final AccountRepository accountRepository;
    private final SecureRandom random = new SecureRandom();

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountResponse createAccount(CreateAccountRequest request) {
        validateAccountName(request);
        Account account = new Account();
        account.setAccountName(request.accountName().trim());
        account.setAccountNumber(generateUniqueAccountNumber());

        Account savedAccount = accountRepository.save(account);

        return toResponse(savedAccount);
    }

    private AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountName(),
                account.getBalance(),
                account.getCreatedAt()
        );
    }

    private String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = String.format("%010d", random.nextLong(10_000_000_000L));
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }

    private void validateAccountName(CreateAccountRequest request) {
        if (request == null || request.accountName() == null || request.accountName().isBlank()) {
            throw new InvalidAccountException("Account name is required");
        }
    }
}
