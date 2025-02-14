package com.bisang.backend.transaction.service;

import com.bisang.backend.installment.controller.request.InstallmentRequest;
import com.bisang.backend.transaction.domain.Transaction;
import com.bisang.backend.user.domain.User;
import org.springframework.stereotype.Service;

import com.bisang.backend.common.annotation.DistributedLock;
import com.bisang.backend.common.exception.ExceptionCode;
import com.bisang.backend.common.exception.TransactionException;
import com.bisang.backend.transaction.controller.request.ChargeRequest;
import com.bisang.backend.transaction.controller.request.PaymentRequest;
import com.bisang.backend.transaction.controller.request.QrCodeRequest;
import com.bisang.backend.transaction.controller.request.TransferRequest;
import com.bisang.backend.transaction.converter.TransactionConverter;
import com.bisang.backend.transaction.domain.TransactionStatus;
import com.bisang.backend.transaction.repository.TransactionLogJpaRepository;
import com.bisang.backend.transaction.service.charge.ChargeService;
import com.bisang.backend.transaction.service.payment.PaymentService;
import com.bisang.backend.transaction.service.transfer.TransferService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
    public static final String ADMIN_ACCOUNT_NUMBER = "1000123456789";

    private final ChargeService chargeService;
    private final TransferService transferService;
    private final PaymentService paymentService;

    private final TransactionLogJpaRepository transactionLogJpaRepository;

    @DistributedLock(name = "관리자 계좌번호", waitTime = 3, leaseTime = 3)
    public void chargeBalance(String key, ChargeRequest chargeRequest) {
        Transaction transaction
                = TransactionConverter.chargeRequestToTransaction(chargeRequest);

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
                = TransactionConverter.transferRequestToTransaction(transferRequest);

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

    @Transactional
    public void installmentBalance(InstallmentRequest installmentRequest, TransferRequest transferRequest) {
        Transaction transaction
                = TransactionConverter.transferRequestToTransaction(transferRequest);

        transaction = saveTransactionLog(transaction);

        try {
            transferService.installment(installmentRequest, transaction);

            updateTransactionStatus(transaction, TransactionStatus.SUCCESS);
            log.info("Balance Installment Success: {}", transaction);
        } catch (Exception e) {
            updateTransactionStatus(transaction, TransactionStatus.FAIL);
            throw new TransactionException(ExceptionCode.BALANCE_TRANSFER_FAIL);
        }

    }

    public String generateExpiringUuidForTeam(User user, QrCodeRequest qrCodeRequest) {
        return paymentService.generateExpiringUuidForTeam(user, qrCodeRequest);
    }

    public String generateExpiringUuidForUser(User user, QrCodeRequest qrCodeRequest) {
        return paymentService.generateExpiringUuidForUser(user, qrCodeRequest);
    }

    @DistributedLock(name = "관리자 계좌번호", waitTime = 3, leaseTime = 3)
    public void pay(String key, PaymentRequest paymentRequest) {
        paymentService.validateQrCodeExpiration(paymentRequest.getUuid());

        Transaction transaction
                = TransactionConverter.paymentRequestToTransaction(paymentRequest);

        transaction = saveTransactionLog(transaction);

        try {
            paymentService.pay(transaction);

            updateTransactionStatus(transaction, TransactionStatus.SUCCESS);
            log.info("Balance Pay Success: {}", transaction);
        } catch (Exception e) {
            updateTransactionStatus(transaction, TransactionStatus.FAIL);
            throw new TransactionException(ExceptionCode.BALANCE_PAY_FAIL);
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
