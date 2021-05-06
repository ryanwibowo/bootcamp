package com.atm;

import com.atm.model.Account;
import com.atm.model.Transaction;
import com.atm.repository.AccountRepository;
import com.atm.service.AccountService;
import com.atm.service.TransactionService;
import com.atm.validation.Validation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
public class MainTests {

    private static final String ACCOUNT_NUMBER = "123001";
    private static final String DESTINATION_NUMBER = "123002";

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private Validation validation;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Test
    public void testAccountCreate() {
        Account account = accountRepository.save(new Account(1l, "Malika", "123123",
                BigDecimal.valueOf(30), ACCOUNT_NUMBER));
        Optional<Account> account1 = accountRepository.findByAccountNumber(ACCOUNT_NUMBER);
        assertEquals(account.getName(), account1.get().getName());
    }

    @Test
    public void testAccountCreate2() {
        Account account = accountRepository.save(new Account(1l, "Malika", "123123",
                BigDecimal.valueOf(30), ACCOUNT_NUMBER));
        List<Account> account1 = accountRepository.findAll();
        assertEquals(account.getName(), account1.get(0).getName());
    }

    @Test
    public void testWithdrawWithEnoughBalance() throws Exception {
        BigDecimal amount = BigDecimal.valueOf(20);
        Account account = accountRepository.save(new Account(1l, "Malika", "123123",
                BigDecimal.valueOf(30), ACCOUNT_NUMBER));
        transactionService.processWithdraw(account, amount);
        Optional<Account> account1 = accountRepository.findByAccountNumber(account.getAccountNumber());
        assertEquals(account1.get().getBalance().intValue(), 10);
    }

    @Test (expected = Exception.class)
    public void testWithdrawWithNotEnoughBalance() throws Exception {
        BigDecimal amount = BigDecimal.valueOf(40);
        Account account = accountRepository.save(new Account(1l, "Malika", "123123",
                BigDecimal.valueOf(30), ACCOUNT_NUMBER));
        transactionService.processWithdraw(account, amount);
    }

    @Test
    public void testTransferWithEnoughBalance() throws Exception {
        BigDecimal amount = BigDecimal.valueOf(20);
        Account sourceAccount = accountRepository.save(new Account(1l, "Malika", "123123",
                BigDecimal.valueOf(30), ACCOUNT_NUMBER));
        Account destinationAccount = accountRepository.save(new Account(2l, "Maliki", "112233",
                BigDecimal.valueOf(30), DESTINATION_NUMBER));
        transactionService.processTransfer(sourceAccount.getAccountNumber(), destinationAccount.getAccountNumber(), amount);
        Optional<Account> sAccount = accountRepository.findByAccountNumber(sourceAccount.getAccountNumber());
        Optional<Account> dAccount = accountRepository.findByAccountNumber(destinationAccount.getAccountNumber());

        assertEquals(sAccount.get().getBalance().intValue(), 10);
        assertEquals(dAccount.get().getBalance().intValue(), 50);
    }

    @Test (expected = Exception.class)
    public void testTransferWithNotEnoughBalance() throws Exception {
        BigDecimal amount = BigDecimal.valueOf(40);
        Account sourceAccount = accountRepository.save(new Account(1l, "Malika", "123123",
                BigDecimal.valueOf(30), ACCOUNT_NUMBER));
        Account destinationAccount = accountRepository.save(new Account(2l, "Maliki", "112233",
                BigDecimal.valueOf(30), DESTINATION_NUMBER));
        transactionService.processTransfer(sourceAccount.getAccountNumber(), destinationAccount.getAccountNumber(), amount);
    }

    @Test (expected = Exception.class)
    public void testTransferWithInvalidDestinationAccount() throws Exception {
        BigDecimal amount = BigDecimal.valueOf(20);
        Account sourceAccount = accountRepository.save(new Account(1l, "Malika", "123123",
                BigDecimal.valueOf(30), ACCOUNT_NUMBER));
        transactionService.processTransfer(sourceAccount.getAccountNumber(), "111111", amount);
    }

    @Test
    public void testLastTransaction() throws Exception {
        BigDecimal amount = BigDecimal.valueOf(20);
        Account account = accountRepository.save(new Account(3l, "Jane", "112244",
                BigDecimal.valueOf(50), "123003"));
        Transaction transaction = transactionService.processWithdraw(account, amount);
        List<Transaction> transactions = transactionService.getLast10Transaction(account.getAccountNumber());
        assertEquals(transaction, transactions.get(0));
    }

    @Test
    public void testBunchTransaction() throws Exception {
        BigDecimal amount = BigDecimal.valueOf(20);
        Account account = accountRepository.save(new Account(4l, "John", "112255",
                BigDecimal.valueOf(30), "123004"));
        transactionService.processWithdraw(account, amount);
        transactionService.processWithdraw(account, BigDecimal.valueOf(5));
        List<Transaction> transactions = transactionService.getLast10Transaction(account.getAccountNumber());
        assertTrue(transactions.size() == 2);
    }

    @Test
    public void testMoreThan10Transaction() throws Exception {
        BigDecimal amount = BigDecimal.valueOf(10);
        Account account = accountRepository.save(new Account(5l, "Mickael", "112266",
                BigDecimal.valueOf(100), "123005"));
        Account destinationAccount = accountRepository.save(new Account(6l, "Mickael", "112277",
                BigDecimal.valueOf(100), "123006"));
        transactionService.processWithdraw(account, amount);
        transactionService.processWithdraw(account, BigDecimal.valueOf(5));
        transactionService.processWithdraw(account, BigDecimal.valueOf(15));
        transactionService.processWithdraw(account, BigDecimal.valueOf(8));
        transactionService.processWithdraw(account, BigDecimal.valueOf(7));
        transactionService.processWithdraw(account, BigDecimal.valueOf(5));
        transactionService.processTransfer(account.getAccountNumber(), destinationAccount.getAccountNumber(), amount);
        transactionService.processTransfer(account.getAccountNumber(), destinationAccount.getAccountNumber(), BigDecimal.valueOf(5));
        transactionService.processTransfer(account.getAccountNumber(), destinationAccount.getAccountNumber(), BigDecimal.valueOf(8));
        transactionService.processTransfer(account.getAccountNumber(), destinationAccount.getAccountNumber(), BigDecimal.valueOf(7));
        transactionService.processTransfer(account.getAccountNumber(), destinationAccount.getAccountNumber(), BigDecimal.valueOf(5));
        transactionService.processTransfer(account.getAccountNumber(), destinationAccount.getAccountNumber(), BigDecimal.valueOf(15));
        transactionService.processTransfer(account.getAccountNumber(), destinationAccount.getAccountNumber(), BigDecimal.valueOf(2));

        List<Transaction> transactions = transactionService.getLast10Transaction(account.getAccountNumber());
        assertTrue(transactions.size() == 10);
    }
}
