package com.bisang.backend.transaction.service.payment;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.bisang.backend.account.domain.Account;
import com.bisang.backend.account.repository.AccountJpaRepository;
import com.bisang.backend.common.exception.AccountException;
import com.bisang.backend.common.exception.ExceptionCode;
import com.bisang.backend.transaction.controller.request.QrCodeRequest;
import com.bisang.backend.transaction.domain.AccountDetails;
import com.bisang.backend.transaction.domain.Transaction;
import com.bisang.backend.transaction.domain.TransactionCategory;
import com.bisang.backend.transaction.service.AccountDetailsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private static final String ADMIN_ACCOUNT_NUMBER = "1000123456789";

    private final QrCodeService qrCodeService;
    private final AccountDetailsService accountDetailsService;

    private final AccountJpaRepository accountJpaRepository;

    public String generateExpiringUuidForTeam(QrCodeRequest qrCodeRequest) {
        return qrCodeService.generateExpiringUuidForTeam(qrCodeRequest);
    }

    public String generateExpiringUuidForUser(QrCodeRequest qrCodeRequest) {
        return qrCodeService.generateExpiringUuidForUser(qrCodeRequest);
    }

    @Transactional
    public void pay(Transaction transaction) {
        String senderAccountNumber = transaction.getSenderAccountNumber();
        Long balance = transaction.getBalance();

        validateAccountBalance(ADMIN_ACCOUNT_NUMBER, balance);
        validateAccountBalance(senderAccountNumber, balance);

        updateAccountBalance(ADMIN_ACCOUNT_NUMBER, balance);
        updateAccountBalance(senderAccountNumber, balance);

        AccountDetails payerAccountDetails
                = accountDetailsService.createAccountDetails(transaction, TransactionCategory.PAYMENT, "결제");
        accountDetailsService.saveAccountDetails(payerAccountDetails);
    }

    public void validateQrCodeExpiration(String uuid) {
        qrCodeService.validateUuidExpiringTime(uuid);
    }

    private void validateAccountBalance(String senderAccountNumber, Long balance) {
        Account account = accountJpaRepository.findByAccountNumber(senderAccountNumber);

        if (!account.validateBalance(balance)) {
            throw new AccountException(ExceptionCode.NOT_ENOUGH_MONEY);
        }
    }

    private void updateAccountBalance(String accountNumber, Long balance) {
        Account account = accountJpaRepository.findByAccountNumber(accountNumber);
        account.decreaseBalance(balance);
        accountJpaRepository.save(account);
    }
}
