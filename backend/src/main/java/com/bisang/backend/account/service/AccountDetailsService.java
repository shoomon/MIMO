package com.bisang.backend.account.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.bisang.backend.account.domain.AccountDetails;
import com.bisang.backend.user.domain.User;
import com.bisang.backend.transaction.domain.Transaction;
import com.bisang.backend.transaction.domain.TransactionCategory;
import com.bisang.backend.transaction.repository.AccountDetailsJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountDetailsService {
    private AccountService accountService;

    private AccountDetailsJpaRepository accountDetailsJpaRepository;

    public AccountDetails createAccountDetails(
            Transaction transaction,
            TransactionCategory transactionCategory,
            String memo
    ) {
        return AccountDetails.builder()
                .transactionId(transaction)
                .balance(transaction.getBalance())
                .senderAccountNumber(transaction.getSenderAccountNumber())
                .receiverAccountNumber(transaction.getReceiverAccountNumber())
                .senderName(transaction.getSenderName())
                .receiverName(transaction.getReceiverName())
                .memo(memo)
                .transactionCategory(transactionCategory)
                .build();
    }

    public void saveAccountDetails(AccountDetails accountDetails) {
        accountDetailsJpaRepository.save(accountDetails);
    }

    public List<AccountDetails> getUserDepositAccountDetails(User user) {
        Long userId = user.getId();
        String accountNumber  = user.getAccountNumber();

        accountService.validateUserAccount(userId, accountNumber);

        return accountDetailsJpaRepository.findByReceiverAccountNumber(accountNumber, "DEPOSIT");
    }
    
    public List<AccountDetails> getUserChargeAccountDetails(User user) {
        Long userId = user.getId();
        String accountNumber  = user.getAccountNumber();

        accountService.validateUserAccount(userId, accountNumber);

        return accountDetailsJpaRepository.findByReceiverAccountNumber(accountNumber, "CHARGE");
    }

    public List<AccountDetails> getUserTransferAccountDetails(User user) {
        Long userId = user.getId();
        String accountNumber  = user.getAccountNumber();

        accountService.validateUserAccount(userId, accountNumber);

        return accountDetailsJpaRepository.findBySenderAccountNumber(accountNumber, "TRANSFER");
    }

    public List<AccountDetails> getUserPayAccountDetails(User user) {
        Long userId = user.getId();
        String accountNumber  = user.getAccountNumber();

        accountService.validateUserAccount(userId, accountNumber);

        return accountDetailsJpaRepository.findBySenderAccountNumber(accountNumber, "PAY");
    }

    @Transactional
    public List<AccountDetails> getUserAllAccountDetails(User user) {
        Long userId = user.getId();
        String accountNumber  = user.getAccountNumber();

        accountService.validateUserAccount(userId, accountNumber);

        List<AccountDetails> allAccountDetails = new ArrayList<>();

        allAccountDetails.addAll(getUserDepositAccountDetails(user));
        allAccountDetails.addAll(getUserChargeAccountDetails(user));
        allAccountDetails.addAll(getUserTransferAccountDetails(user));
        allAccountDetails.addAll(getUserPayAccountDetails(user));

        allAccountDetails.sort(Comparator.comparing(AccountDetails::getCreatedAt).reversed());

        return allAccountDetails;
    }

}
