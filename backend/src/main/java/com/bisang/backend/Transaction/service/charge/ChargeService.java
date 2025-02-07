package com.bisang.backend.Transaction.service.charge;


import com.bisang.backend.Transaction.domain.AccountDetails;
import com.bisang.backend.Transaction.domain.Transaction;
import com.bisang.backend.Transaction.domain.TransactionCategory;
import com.bisang.backend.Transaction.service.AccountDetailsService;
import com.bisang.backend.account.domain.Account;
import com.bisang.backend.account.repository.AccountJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChargeService {
    private static final String ADMIN_ACCOUNT_NUMBER = "1000123456789";

    private final AccountDetailsService accountDetailsService;

    private final AccountJpaRepository accountJpaRepository;

    @Transactional
    public void charge(Transaction transaction) {
        updateAccountBalance(ADMIN_ACCOUNT_NUMBER, transaction.getBalance());
        updateAccountBalance(transaction.getReceiverAccountNumber(), transaction.getBalance());

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