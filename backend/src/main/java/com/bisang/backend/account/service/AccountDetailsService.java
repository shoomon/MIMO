package com.bisang.backend.account.service;

import static com.bisang.backend.transaction.domain.TransactionCategory.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.bisang.backend.common.exception.ExceptionCode;
import com.bisang.backend.common.exception.TeamException;
import com.bisang.backend.team.domain.Team;
import com.bisang.backend.team.repository.TeamJpaRepository;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.bisang.backend.account.domain.AccountDetails;
import com.bisang.backend.account.controller.response.AccountDetailsResponse;
import com.bisang.backend.account.converter.AccountDetailsConverter;
import com.bisang.backend.user.domain.User;
import com.bisang.backend.transaction.domain.Transaction;
import com.bisang.backend.transaction.domain.TransactionCategory;
import com.bisang.backend.transaction.repository.AccountDetailsJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountDetailsService {
    private final AccountService accountService;

    private final TeamJpaRepository teamJpaRepository;
    private final AccountDetailsJpaRepository accountDetailsJpaRepository;

    public AccountDetails createAccountDetails(
            Transaction transaction,
            TransactionCategory transactionCategory,
            String memo
    ) {
        return AccountDetails.builder()
                .transactionId(transaction.getId())
                .amount(transaction.getAmount())
                .senderAccountNumber(transaction.getSenderAccountNumber())
                .receiverAccountNumber(transaction.getReceiverAccountNumber())
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

        List<AccountDetails> accountDetails
                = accountDetailsJpaRepository.findByReceiverAccountNumberAndTransactionCategory(accountNumber, DEPOSIT);
        return AccountDetailsConverter.accountDetailsToAccountDetailsResponses(accountDetails);
    }
    
    public List<AccountDetailsResponse> getUserChargeAccountDetails(User user) {
        Long userId = user.getId();
        String accountNumber  = user.getAccountNumber();

        accountService.validateUserAccount(userId, accountNumber);

        List<AccountDetails> accountDetails
                = accountDetailsJpaRepository.findByReceiverAccountNumberAndTransactionCategory(accountNumber, CHARGE);
        return AccountDetailsConverter.accountDetailsToAccountDetailsResponses(accountDetails);
    }

    public List<AccountDetailsResponse> getUserTransferAccountDetails(User user) {
        Long userId = user.getId();
        String accountNumber  = user.getAccountNumber();

        accountService.validateUserAccount(userId, accountNumber);

        List<AccountDetails> accountDetails
                = accountDetailsJpaRepository.findBySenderAccountNumberAndTransactionCategory(accountNumber, TRANSFER);
        return AccountDetailsConverter.accountDetailsToAccountDetailsResponses(accountDetails);
    }

    public List<AccountDetailsResponse> getUserPayAccountDetails(User user) {
        Long userId = user.getId();
        String accountNumber  = user.getAccountNumber();

        accountService.validateUserAccount(userId, accountNumber);

        List<AccountDetails> accountDetails
                = accountDetailsJpaRepository.findBySenderAccountNumberAndTransactionCategory(accountNumber, PAYMENT);
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

    public List<AccountDetailsResponse> getTeamDepositAccountDetails(Long teamId) {
        Team team = findByTeamId(teamId);
        String accountNumber = team.getAccountNumber();

        List<AccountDetails> accountDetails =
                accountDetailsJpaRepository.findByReceiverAccountNumberAndTransactionCategory(accountNumber, DEPOSIT);
        return AccountDetailsConverter.accountDetailsToAccountDetailsResponses(accountDetails);
    }

    public List<AccountDetailsResponse> getTeamPayAccountDetails(Long teamId) {
        Team team = findByTeamId(teamId);
        String accountNumber = team.getAccountNumber();

        List<AccountDetails> accountDetails =
                accountDetailsJpaRepository.findBySenderAccountNumberAndTransactionCategory(accountNumber, PAYMENT);
        return AccountDetailsConverter.accountDetailsToAccountDetailsResponses(accountDetails);
    }

    private Team findByTeamId(Long teamId) {
        return teamJpaRepository.findTeamById(teamId)
                .orElseThrow(() -> new TeamException(ExceptionCode.NOT_FOUND_TEAM));
    }
}
