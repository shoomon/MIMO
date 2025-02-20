package com.bisang.backend.transaction.service.payment;

import static com.bisang.backend.transaction.service.TransactionService.ADMIN_ACCOUNT_NUMBER;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.bisang.backend.account.domain.Account;
import com.bisang.backend.account.domain.AccountDetails;
import com.bisang.backend.account.repository.AccountJpaRepository;
import com.bisang.backend.account.service.AccountDetailsService;
import com.bisang.backend.common.exception.AccountException;
import com.bisang.backend.common.exception.ExceptionCode;
import com.bisang.backend.transaction.domain.Transaction;
import com.bisang.backend.transaction.domain.TransactionCategory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final AccountDetailsService accountDetailsService;

    private final AccountJpaRepository accountJpaRepository;

    @Transactional
    public void pay(Transaction transaction) {
        String senderAccountNumber = transaction.getSenderAccountNumber();
        Long amount = transaction.getAmount();

        validateAccountBalance(ADMIN_ACCOUNT_NUMBER, amount);
        validateAccountBalance(senderAccountNumber, amount);

        updateAccountBalance(ADMIN_ACCOUNT_NUMBER, amount);
        updateAccountBalance(senderAccountNumber, amount);

        AccountDetails payerAccountDetails
                = accountDetailsService.createAccountDetails(transaction, TransactionCategory.PAYMENT);
        accountDetailsService.saveAccountDetails(payerAccountDetails);
    }

    private void validateAccountBalance(String senderAccountNumber, Long amount) {
        Account account = accountJpaRepository.findByAccountNumberWithLockingReads(senderAccountNumber);

        if (!account.validateBalance(amount)) {
            throw new AccountException(ExceptionCode.NOT_ENOUGH_MONEY);
        }
    }

    private void updateAccountBalance(String accountNumber, Long amount) {
        Account account = accountJpaRepository.findByAccountNumberWithLockingReads(accountNumber);
        account.decreaseBalance(amount);
        accountJpaRepository.save(account);
    }
}
