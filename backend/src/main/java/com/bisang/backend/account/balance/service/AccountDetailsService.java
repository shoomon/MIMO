package com.bisang.backend.account.balance.service;

import com.bisang.backend.account.balance.converter.AccountDetailsConverter;
import com.bisang.backend.account.balance.domain.AccountDetails;
import com.bisang.backend.account.balance.domain.Transaction;
import com.bisang.backend.account.balance.repository.AccountDetailsJpaRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountDetailsService {

    private final AccountDetailsJpaRepository accountDetailsJpaRepository;

    public void saveAccountDetails(Transaction transaction) {
        AccountDetails accountDetails
                = AccountDetailsConverter.transactionToAccountDetails(transaction);
        accountDetailsJpaRepository.save(accountDetails);
    }
}