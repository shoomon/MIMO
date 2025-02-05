package com.bisang.backend.account.charge.service;

import com.bisang.backend.account.charge.controller.request.PaymentResultRequest;
import com.bisang.backend.account.converter.TransactionConverter;
import com.bisang.backend.account.domain.Account;
import com.bisang.backend.account.domain.Transaction;
import com.bisang.backend.account.domain.TransactionStatus;
import com.bisang.backend.account.repository.AccountJpaRepository;
import com.bisang.backend.account.repository.TransactionJpaRepository;
import com.bisang.backend.common.annotation.DistributedLock;
import com.bisang.backend.common.exception.ExceptionCode;
import com.bisang.backend.common.exception.ChargeException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChargeService {
    private static final String ADMIN_ACCOUNT_NUMBER = "1000123456789";

    private final TransactionJpaRepository transactionJpaRepository;
    private final AccountJpaRepository accountJpaRepository;

    @DistributedLock(name = "관리자 계좌번호", key = ADMIN_ACCOUNT_NUMBER, waitTime = 3, leaseTime = 3)
    @Transactional
    public void chargePoint(PaymentResultRequest paymentResultRequest) {
        Transaction transaction
                = TransactionConverter.PaymentResultRequestToPointTransaction(paymentResultRequest);

        try {
            transaction = saveChargeTransactionLog(transaction);
            updateAccountBalance(ADMIN_ACCOUNT_NUMBER, transaction.getBalance());
            updateAccountBalance(transaction.getReceiverAccountNumber(), transaction.getBalance());

            updateTransactionStatus(transaction, TransactionStatus.SUCCESS);
            log.info("Balance Charge Success: {}", transaction);
        } catch (Exception e) {
            // TODO
            // PG사 환불 로직 추가
            updateTransactionStatus(transaction, TransactionStatus.FAIL);
            throw new ChargeException(ExceptionCode.BALANCE_CHARGE_FAIL);
        }
    }

    private Transaction saveChargeTransactionLog(Transaction transaction) {
        return transactionJpaRepository.save(transaction);
    }

    private void updateAccountBalance(String accountNumber, Long point) {
        Account account = accountJpaRepository.findByAccountNumber(accountNumber);
        account.increaseBalance(point);
        accountJpaRepository.save(account);
    }

    private void updateTransactionStatus(Transaction transaction, TransactionStatus transactionStatus) {
        transaction.updateTransactionStatus(transactionStatus);
        transactionJpaRepository.save(transaction);
    }
}
