package com.bisang.backend.transaction.service;

import com.bisang.backend.common.exception.AccountException;
import com.bisang.backend.team.annotation.TeamMember;
import org.springframework.stereotype.Service;

import com.bisang.backend.common.annotation.DistributedLock;
import com.bisang.backend.common.exception.ExceptionCode;
import com.bisang.backend.common.exception.TransactionException;
import com.bisang.backend.installment.controller.request.InstallmentRequest;
import com.bisang.backend.qrcode.service.QrCodeService;
import com.bisang.backend.transaction.controller.request.ChargeRequest;
import com.bisang.backend.transaction.controller.request.PaymentRequest;
import com.bisang.backend.transaction.controller.request.TransferRequest;
import com.bisang.backend.transaction.converter.TransactionConverter;
import com.bisang.backend.transaction.domain.Transaction;
import com.bisang.backend.transaction.domain.TransactionStatus;
import com.bisang.backend.transaction.repository.TransactionJpaRepository;
import com.bisang.backend.transaction.service.charge.ChargeService;
import com.bisang.backend.transaction.service.payment.PaymentService;
import com.bisang.backend.transaction.service.transfer.TransferService;
import com.bisang.backend.user.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
    public static final String ADMIN_ACCOUNT_NUMBER = "1000123456789";

    private final ChargeService chargeService;
    private final PaymentService paymentService;
    private final QrCodeService qrCodeService;
    private final TransferService transferService;

    private final TransactionJpaRepository transactionLogJpaRepository;

    @DistributedLock(name = "관리자 계좌번호", waitTime = 10, leaseTime = 3)
    public void chargeBalance(String key, ChargeRequest chargeRequest, User user) {
        Transaction transaction
                = TransactionConverter.chargeRequestToTransaction(chargeRequest, user);
        transaction = saveTransactionLog(transaction);

        try {
            chargeService.charge(transaction);

            updateTransactionStatus(transaction, TransactionStatus.SUCCESS);
            log.info("Balance Charge Success: {}", transaction);
        } catch (Exception e) {
            log.error("Balance Charge Error Occur : ", e);
            updateTransactionStatus(transaction, TransactionStatus.FAIL);
            throw new TransactionException(ExceptionCode.BALANCE_CHARGE_FAIL);
        }
    }

    public void transferBalance(TransferRequest transferRequest, User user) {
        Transaction transaction
                = TransactionConverter.transferRequestToTransaction(transferRequest, user);
        transaction = saveTransactionLog(transaction);

        try {
            transferService.transfer(transaction);

            updateTransactionStatus(transaction, TransactionStatus.SUCCESS);
            log.info("Balance Transfer Success: {}", transaction);
        } catch (Exception e) {
            log.error("Balance Transfer Error Occur : ", e);
            updateTransactionStatus(transaction, TransactionStatus.FAIL);
            throw new TransactionException(ExceptionCode.BALANCE_TRANSFER_FAIL);
        }
    }

    @TeamMember
    public void installmentBalance(
            Long userId,
            Long teamId,
            InstallmentRequest installmentRequest,
            User user
    ) {
        Transaction transaction
                = TransactionConverter.transferRequestToTransaction(installmentRequest.getTransferRequest(), user);
        transaction = saveTransactionLog(transaction);

        try {
            transferService.installment(installmentRequest, transaction);

            updateTransactionStatus(transaction, TransactionStatus.SUCCESS);
            log.info("Balance Installment Success: {}", transaction);
        } catch (AccountException e1) {
            log.error("Balance Installment Error Occur : ", e1);
            updateTransactionStatus(transaction, TransactionStatus.FAIL);
            throw new TransactionException(ExceptionCode.NOT_ENOUGH_MONEY);
        } catch (Exception e2) {
            log.error("Balance Installment Error Occur : ", e2);
            updateTransactionStatus(transaction, TransactionStatus.FAIL);
            throw new TransactionException(ExceptionCode.BALANCE_TRANSFER_FAIL);
        }

    }

    @DistributedLock(name = "관리자 계좌번호", waitTime = 3, leaseTime = 3)
    public void pay(String key, PaymentRequest paymentRequest) {
        String senderAccountNumber = qrCodeService.getSenderAccountNumber(paymentRequest.getUuid());

        Transaction transaction
                = TransactionConverter.paymentRequestToTransaction(paymentRequest, senderAccountNumber);
        transaction = saveTransactionLog(transaction);

        try {
            paymentService.pay(transaction);

            updateTransactionStatus(transaction, TransactionStatus.SUCCESS);
            log.info("Balance Pay Success: {}", transaction);
        } catch (AccountException e1) {
            log.error("Balance Pay Error Occur : ", e1);
            updateTransactionStatus(transaction, TransactionStatus.FAIL);
            throw new TransactionException(ExceptionCode.NOT_ENOUGH_MONEY);
        } catch (Exception e2) {
            log.error("Balance Pay Error Occur : ", e2);
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
