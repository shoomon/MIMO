package com.bisang.backend.transaction.service.transfer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.installment.controller.request.InstallmentRequest;
import com.bisang.backend.installment.domain.Installment;
import com.bisang.backend.installment.repository.InstallmentJpaRepository;
import com.bisang.backend.transaction.domain.Transaction;
import com.bisang.backend.account.domain.Account;
import com.bisang.backend.account.repository.AccountJpaRepository;
import com.bisang.backend.common.exception.AccountException;
import com.bisang.backend.common.exception.ExceptionCode;
import com.bisang.backend.account.domain.AccountDetails;
import com.bisang.backend.transaction.domain.TransactionCategory;
import com.bisang.backend.account.service.AccountDetailsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final AccountDetailsService accountDetailsService;
    private final AccountJpaRepository accountJpaRepository;
    private final InstallmentJpaRepository installmentJpaRepository;

    @Transactional
    public void transfer(Transaction transaction) {
        String senderAccountNumber = transaction.getSenderAccountNumber();
        String receiverAccountNumber = transaction.getReceiverAccountNumber();
        Long amount = transaction.getAmount();

        validateSenderAccountBalance(senderAccountNumber, amount);

        updateSenderAccountBalance(senderAccountNumber, amount);
        updateReceiverAccountBalance(receiverAccountNumber, amount);

        AccountDetails senderAccountDetails
                = accountDetailsService.createAccountDetails(transaction, TransactionCategory.TRANSFER, "송금");
        AccountDetails receiverAccountDetails
                = accountDetailsService.createAccountDetails(transaction, TransactionCategory.DEPOSIT, "입금");
        accountDetailsService.saveAccountDetails(senderAccountDetails);
        accountDetailsService.saveAccountDetails(receiverAccountDetails);
    }

    @Transactional
    public void installment(InstallmentRequest installmentRequest, Transaction transaction) {
        transfer(transaction);
        updateInstallment(installmentRequest);
    }

    private void validateSenderAccountBalance(String senderAccountNumber, Long amount) {
        Account account = accountJpaRepository.findByAccountNumberWithLockingReads(senderAccountNumber);

        if (!account.validateBalance(amount)) {
            throw new AccountException(ExceptionCode.NOT_ENOUGH_MONEY);
        }
    }

    private void updateSenderAccountBalance(String senderAccountNumber, Long amount) {
        Account account = accountJpaRepository.findByAccountNumberWithLockingReads(senderAccountNumber);
        account.decreaseBalance(amount);
        accountJpaRepository.save(account);
    }

    private void updateReceiverAccountBalance(String receiverAccountNumber, Long amount) {
        Account account = accountJpaRepository.findByAccountNumberWithLockingReads(receiverAccountNumber);
        account.increaseBalance(amount);
        accountJpaRepository.save(account);
    }

    private void updateInstallment(InstallmentRequest installmentRequest) {
        Installment installment = installmentJpaRepository.findByTeamIdAndUserIdAndRound(
                installmentRequest.getTeamId(),
                installmentRequest.getUserId(),
                installmentRequest.getRound()
        ).orElseThrow(RuntimeException::new);

        installment.updateInstallmentStatusToYes();
        installment.updateInstallmentDate();
        installmentJpaRepository.save(installment);
    }
}
