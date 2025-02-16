package com.bisang.backend.account.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.bisang.backend.account.controller.response.AccountDetailsResponse;
import com.bisang.backend.account.converter.AccountDetailsConverter;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.bisang.backend.account.domain.AccountDetails;
import com.bisang.backend.user.domain.User;
import com.bisang.backend.transaction.domain.Transaction;
import com.bisang.backend.transaction.domain.TransactionCategory;
import com.bisang.backend.transaction.repository.AccountDetailsJpaRepository;

import lombok.RequiredArgsConstructor;

import static com.bisang.backend.transaction.domain.TransactionCategory.*;

@Service
@RequiredArgsConstructor
public class AccountDetailsService {
    private final AccountService accountService;

    private final AccountDetailsJpaRepository accountDetailsJpaRepository;

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

    public List<AccountDetailsResponse> getUserDepositAccountDetails(User user) {
        Long userId = user.getId();
        String accountNumber  = user.getAccountNumber();

        accountService.validateUserAccount(userId, accountNumber);

        List<AccountDetails> accountDetails = accountDetailsJpaRepository.findByReceiverAccountNumberAndTransactionCategory(accountNumber, DEPOSIT);
        return AccountDetailsConverter.accountDetailsToAccountDetailsResponses(accountDetails);
    }
    
    public List<AccountDetailsResponse> getUserChargeAccountDetails(User user) {
        Long userId = user.getId();
        String accountNumber  = user.getAccountNumber();

        accountService.validateUserAccount(userId, accountNumber);

        List<AccountDetails> accountDetails = accountDetailsJpaRepository.findByReceiverAccountNumberAndTransactionCategory(accountNumber, CHARGE);
        return AccountDetailsConverter.accountDetailsToAccountDetailsResponses(accountDetails);
    }

    public List<AccountDetailsResponse> getUserTransferAccountDetails(User user) {
        Long userId = user.getId();
        String accountNumber  = user.getAccountNumber();

        accountService.validateUserAccount(userId, accountNumber);

        List<AccountDetails> accountDetails = accountDetailsJpaRepository.findBySenderAccountNumberAndTransactionCategory(accountNumber, TRANSFER);
        return AccountDetailsConverter.accountDetailsToAccountDetailsResponses(accountDetails);
    }

    public List<AccountDetailsResponse> getUserPayAccountDetails(User user) {
        Long userId = user.getId();
        String accountNumber  = user.getAccountNumber();

        accountService.validateUserAccount(userId, accountNumber);

        List<AccountDetails> accountDetails = accountDetailsJpaRepository.findBySenderAccountNumberAndTransactionCategory(accountNumber, PAYMENT);
        return AccountDetailsConverter.accountDetailsToAccountDetailsResponses(accountDetails);
    }

    @Transactional
    public List<AccountDetailsResponse> getUserAllAccountDetails(User user) {
        Long userId = user.getId();
        String accountNumber  = user.getAccountNumber();

        accountService.validateUserAccount(userId, accountNumber);

        List<AccountDetailsResponse> allAccountDetails = new ArrayList<>();

        allAccountDetails.addAll(getUserDepositAccountDetails(user));
        allAccountDetails.addAll(getUserChargeAccountDetails(user));
        allAccountDetails.addAll(getUserTransferAccountDetails(user));
        allAccountDetails.addAll(getUserPayAccountDetails(user));

        allAccountDetails.sort(Comparator.comparing(AccountDetailsResponse::getCreatedAt).reversed());

        return allAccountDetails;
    }

}
