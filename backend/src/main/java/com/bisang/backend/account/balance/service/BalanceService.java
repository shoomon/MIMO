package com.bisang.backend.account.balance.service;

import com.bisang.backend.account.balance.controller.request.PaymentResultRequest;
import com.bisang.backend.account.balance.converter.TransactionConverter;
import com.bisang.backend.account.balance.domain.Transaction;
import com.bisang.backend.account.balance.domain.TransactionStatus;
import com.bisang.backend.account.balance.repository.TransactionJpaRepository;
import com.bisang.backend.account.balance.service.charge.ChargeService;
import com.bisang.backend.common.annotation.DistributedLock;
import com.bisang.backend.common.exception.ChargeException;
import com.bisang.backend.common.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceService {
    private static final String ADMIN_ACCOUNT_NUMBER = "1000123456789";

    private final ChargeService chargeService;

    private final TransactionJpaRepository transactionJpaRepository;

    @DistributedLock(name = "관리자 계좌번호", key = ADMIN_ACCOUNT_NUMBER, waitTime = 3, leaseTime = 3)
    public void charge(PaymentResultRequest paymentResultRequest) {
        Transaction transaction
                = TransactionConverter.PaymentResultRequestToTransaction(paymentResultRequest);

        try {
            transaction = saveChargeTransactionLog(transaction);

            chargeService.chargeBalance(transaction);

            updateTransactionStatus(transaction, TransactionStatus.SUCCESS);
            log.info("Balance Charge Success: {}", transaction);
        } catch (Exception e) {
            updateTransactionStatus(transaction, TransactionStatus.FAIL);
            throw new ChargeException(ExceptionCode.BALANCE_CHARGE_FAIL);
        }
    }

    private Transaction saveChargeTransactionLog(Transaction transaction) {
        return transactionJpaRepository.save(transaction);
    }

    private void updateTransactionStatus(Transaction transaction, TransactionStatus transactionStatus) {
        transaction.updateTransactionStatus(transactionStatus);
        transactionJpaRepository.save(transaction);
    }
}
