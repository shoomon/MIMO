package com.bisang.backend.Transaction.service;

import com.bisang.backend.Transaction.controller.request.PaymentResultRequest;
import com.bisang.backend.Transaction.controller.request.TransferRequest;
import com.bisang.backend.Transaction.converter.TransactionConverter;
import com.bisang.backend.Transaction.domain.Transaction;
import com.bisang.backend.Transaction.domain.TransactionStatus;
import com.bisang.backend.Transaction.repository.TransactionLogJpaRepository;
import com.bisang.backend.Transaction.service.charge.ChargeService;
import com.bisang.backend.Transaction.service.transfer.TransferService;
import com.bisang.backend.common.annotation.DistributedLock;
import com.bisang.backend.common.exception.ExceptionCode;
import com.bisang.backend.common.exception.TransactionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TransactionService {
    private static final String ADMIN_ACCOUNT_NUMBER = "1000123456789";

    private final ChargeService chargeService;
    private final TransferService transferService;

    private final TransactionLogJpaRepository transactionLogJpaRepository;

    @DistributedLock(name = "관리자 계좌번호", key = ADMIN_ACCOUNT_NUMBER, waitTime = 3, leaseTime = 3)
    public void chargeBalance(PaymentResultRequest paymentResultRequest) {
        Transaction transaction
                = TransactionConverter.PaymentResultRequestToTransaction(paymentResultRequest);

        transaction = saveTransactionLog(transaction);

        try {
            chargeService.charge(transaction);

            updateTransactionStatus(transaction, TransactionStatus.SUCCESS);
            log.info("Balance Charge Success: {}", transaction);
        } catch (Exception e) {
            updateTransactionStatus(transaction, TransactionStatus.FAIL);
            throw new TransactionException(ExceptionCode.BALANCE_CHARGE_FAIL);
        }
    }

    public void transferBalance(TransferRequest transferRequest) {
        Transaction transaction
                = TransactionConverter.TransferRequestToTransaction(transferRequest);

        transaction = saveTransactionLog(transaction);

        try {
            transferService.transfer(transaction);

            updateTransactionStatus(transaction, TransactionStatus.SUCCESS);
            log.info("Balance Transfer Success: {}", transaction);
        } catch (Exception e) {
            updateTransactionStatus(transaction, TransactionStatus.FAIL);
            throw new TransactionException(ExceptionCode.BALANCE_TRANSFER_FAIL);
        }
    }

    private Transaction saveTransactionLog(Transaction transaction) {
        return transactionLogJpaRepository.save(transaction);
    }

    private void updateTransactionStatus(
            Transaction transaction,
            TransactionStatus transactionStatus
    ) {
        transaction.updateTransactionStatus(transactionStatus);
        transactionLogJpaRepository.save(transaction);
    }
}
