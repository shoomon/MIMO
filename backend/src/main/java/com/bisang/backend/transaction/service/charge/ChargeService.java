package com.bisang.backend.transaction.service.charge;

import static com.bisang.backend.transaction.service.TransactionService.ADMIN_ACCOUNT_NUMBER;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.account.domain.Account;
import com.bisang.backend.account.repository.AccountJpaRepository;
import com.bisang.backend.transaction.domain.AccountDetails;
import com.bisang.backend.transaction.domain.Transaction;
import com.bisang.backend.transaction.domain.TransactionCategory;
import com.bisang.backend.transaction.service.AccountDetailsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChargeService {


    private final AccountDetailsService accountDetailsService;
    private final AccountJpaRepository accountJpaRepository;

    @Transactional
    public void charge(Transaction transaction) {
        String receiverAccountNumber = transaction.getSenderAccountNumber();
        Long balance = transaction.getBalance();

        updateAccountBalance(ADMIN_ACCOUNT_NUMBER, balance);
        updateAccountBalance(receiverAccountNumber, balance);

        AccountDetails receiverAccountDetails
                = accountDetailsService.createAccountDetails(transaction, TransactionCategory.CHARGE, "충전");
        accountDetailsService.saveAccountDetails(receiverAccountDetails);
    }

    private void updateAccountBalance(String accountNumber, Long balance) {
        Account account = accountJpaRepository.findByAccountNumber(accountNumber);
        account.increaseBalance(balance);
        accountJpaRepository.save(account);
    }
}