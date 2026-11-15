package ng.safebanking.service;

import jakarta.persistence.EntityManager;
import ng.safebanking.data.models.Account;
import ng.safebanking.data.repositories.AccountRepository;
import ng.safebanking.dtos.request.CreateAccountRequest;
import ng.safebanking.dtos.response.AccountResponse;
import ng.safebanking.exception.InvalidAccountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private EntityManager entityManager;
    @BeforeEach
    public void setUp () {
        accountRepository.deleteAll();
    }


    @Test
    void shouldCreateAccountWithValidDetails() {
        CreateAccountRequest request = new CreateAccountRequest("Godwin", "godwin@gmail.com", "08012345678", "Password123");
        AccountResponse response = accountService.createAccount(request);
        assertNotNull(response);
        assertNotNull(response.id());
        assertEquals("Godwin", response.accountName());
        assertNotNull(response.createdAt());
    }

    @Test
    void shouldStartNewAccountWithZeroBalance() {
        AccountResponse response = accountService.createAccount(new CreateAccountRequest("Godwin", "godwin@gmail.com", "08012345678", "Password123"));
        assertEquals(0, BigDecimal.ZERO.compareTo(response.balance()));
    }

    @Test
    void shouldTrimAccountNameBeforeSaving() {
        CreateAccountRequest request =new CreateAccountRequest("  Godwin ", "godwin@gmail.com", "08012345678", "Password123");
        AccountResponse response = accountService.createAccount(request);
        assertEquals("Godwin", response.accountName());
    }

    @Test
    void shouldRejectBlankAccountName() {
        CreateAccountRequest request = new CreateAccountRequest(" ", "godwin@gmail.com", "08012345678", "Password123");
        assertThrows(InvalidAccountException.class,
                () -> accountService.createAccount(request));
    }

    @Test
    void shouldRejectNullAccountName() {
        CreateAccountRequest request = new CreateAccountRequest(null, "godwin@gmail.com", "08012345678", "Password123");
        assertThrows(InvalidAccountException.class,
                () -> accountService.createAccount(request));
    }

    @Test
    void shouldGenerateTenDigitAccountNumber() {
        AccountResponse response = accountService.createAccount(new CreateAccountRequest("Godwin", "godwin@gmail.com", "08012345678", "Password123"));
        assertTrue(response.accountNumber().matches("\\d{10}"));
    }

    @Test
    void shouldGenerateUniqueAccountNumbersForDifferentAccounts() {
        AccountResponse firstAccount = accountService.createAccount(new CreateAccountRequest("Godwin", "godwin@gmail.com", "08012345678", "Password123"));
        AccountResponse secondAccount = accountService.createAccount(new CreateAccountRequest("John", "john@gmail.com", "08098765432", "Password456"));
        assertNotEquals(firstAccount.accountNumber(), secondAccount.accountNumber());
    }

    @Test
    void shouldNotAllowDuplicateAccountNumbersInDatabase() {
        Account firstAccount = new Account();
        firstAccount.setAccountName("Godwin");
        firstAccount.setAccountNumber("1234567890");
        accountRepository.saveAndFlush(firstAccount);
        Account secondAccount = new Account();
        secondAccount.setAccountName("John");
        secondAccount.setAccountNumber("1234567890");

        assertThrows(
                DataIntegrityViolationException.class,
                () -> accountRepository.saveAndFlush(secondAccount));
    }

    @Test
    void shouldPersistUpdatedAccountBalance() {
        Account account = new Account();
        account.setAccountName("Godwin");
        account.setAccountNumber("1234567890");
        Account savedAccount = accountRepository.saveAndFlush(account);
        savedAccount.setBalance(new BigDecimal("5000.00"));
        accountRepository.saveAndFlush(savedAccount);
        entityManager.clear();
        Account updatedAccount = accountRepository.findById(savedAccount.getId()).orElseThrow();
        assertEquals(0, new BigDecimal("5000.00").compareTo(updatedAccount.getBalance()));
    }

}
