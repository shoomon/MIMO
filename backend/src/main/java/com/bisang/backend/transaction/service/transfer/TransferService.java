package com.bisang.backend.transaction.service.transfer;


import com.bisang.backend.transaction.domain.AccountDetails;
import com.bisang.backend.transaction.domain.Transaction;
import com.bisang.backend.transaction.domain.TransactionCategory;
import com.bisang.backend.transaction.service.AccountDetailsService;
import com.bisang.backend.account.domain.Account;
import com.bisang.backend.account.repository.AccountJpaRepository;
import com.bisang.backend.common.exception.AccountException;
import com.bisang.backend.common.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final AccountDetailsService accountDetailsService;

    private final AccountJpaRepository accountJpaRepository;

    @Transactional
    public void transfer(Transaction transaction) {
        String senderAccountNumber = transaction.getSenderAccountNumber();
        String receiverAccountNumber = transaction.getReceiverAccountNumber();
        Long balance = transaction.getBalance();

        validateSenderAccountBalance(senderAccountNumber, balance);

        updateSenderAccountBalance(senderAccountNumber, balance);
        updateReceiverAccountBalance(receiverAccountNumber, balance);

        AccountDetails senderAccountDetails
                = accountDetailsService.createAccountDetails(transaction, TransactionCategory.TRANSFER, "송금");
        AccountDetails receiverAccountDetails
                = accountDetailsService.createAccountDetails(transaction, TransactionCategory.DEPOSIT, "입금");
        accountDetailsService.saveAccountDetails(senderAccountDetails);
        accountDetailsService.saveAccountDetails(receiverAccountDetails);
    }

    private void validateSenderAccountBalance(String senderAccountNumber, Long balance) {
        Account account = accountJpaRepository.findByAccountNumber(senderAccountNumber);

        if (!account.validateBalance(balance)) {
            throw new AccountException(ExceptionCode.NOT_ENOUGH_MONEY);
        }
    }

    private void updateSenderAccountBalance(String senderAccountNumber, Long balance) {
        Account account = accountJpaRepository.findByAccountNumber(senderAccountNumber);
        account.decreaseBalance(balance);
        accountJpaRepository.save(account);
    }

    private void updateReceiverAccountBalance(String accountNumber, Long balance) {
        Account account = accountJpaRepository.findByAccountNumber(accountNumber);
        account.increaseBalance(balance);
        accountJpaRepository.save(account);
    }
}
