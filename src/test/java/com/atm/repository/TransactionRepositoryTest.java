package com.atm.repository;

import com.atm.model.Account;
import com.atm.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void getTop10ByAccountNumberOrDestinationNumberOrderByTransactionDateAsc_returnsSortedTransactions() {
        Account account = new Account();
        account.setAccountNumber("123456");
        account.setPin("123456");
        account.setName("John Doe");
        account.setBalance(BigDecimal.valueOf(1000));
        accountRepository.save(account);

        for (int i = 1; i <= 12; i++) {
            Transaction tx = Transaction.builder()
                    .transactionType("WITHDRAW")
                    .transactionDate(LocalDateTime.now().plusMinutes(i))
                    .accountNumber("123456")
                    .amount(BigDecimal.valueOf(100 + i))
                    .accountBalance(BigDecimal.valueOf(900 - i))
                    .build();
            transactionRepository.save(tx);
        }

        List<Transaction> transactions =
                transactionRepository.getTop10ByAccountNumberOrDestinationNumberOrderByTransactionDateAsc("123456", "123456");

        assertThat(transactions).hasSize(10);
        assertThat(transactions.get(0).getTransactionDate())
                .isBefore(transactions.get(9).getTransactionDate()); // sorted ascending
    }

    @Test
    void getTop10ByAccountNumberOrDestinationNumberOrderByTransactionDateAsc_returnsEmptyList() {
        List<Transaction> transactions =
                transactionRepository.getTop10ByAccountNumberOrDestinationNumberOrderByTransactionDateAsc("999999", "999999");

        assertThat(transactions).isEmpty();
    }
}
