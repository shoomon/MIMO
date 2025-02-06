package com.bisang.backend.account.balance.service.charge;

import com.bisang.backend.account.balance.domain.Transaction;
import com.bisang.backend.account.balance.service.AccountDetailsService;
import com.bisang.backend.account.domain.Account;
import com.bisang.backend.account.repository.AccountJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChargeService {
    private static final String ADMIN_ACCOUNT_NUMBER = "1000123456789";

    private final AccountDetailsService accountDetailsService;

    private final AccountJpaRepository accountJpaRepository;

    @Transactional
    public void chargeBalance(Transaction transaction) {
        updateAccountBalance(ADMIN_ACCOUNT_NUMBER, transaction.getBalance());
        updateAccountBalance(transaction.getReceiverAccountNumber(), transaction.getBalance());

        accountDetailsService.saveAccountDetails(transaction);
    }

    private void updateAccountBalance(String accountNumber, Long point) {
        Account account = accountJpaRepository.findByAccountNumber(accountNumber);
        account.increaseBalance(point);
        accountJpaRepository.save(account);
    }
}