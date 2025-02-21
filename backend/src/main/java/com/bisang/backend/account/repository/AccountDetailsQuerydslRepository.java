package com.bisang.backend.account.repository;

import static com.bisang.backend.account.domain.QAccountDetails.accountDetails;
import static com.bisang.backend.user.domain.QUser.user;

import com.querydsl.core.BooleanBuilder;
import org.springframework.stereotype.Repository;

import com.bisang.backend.account.controller.response.AccountDetailsResponse;
import com.bisang.backend.account.domain.AccountDetails;
import com.bisang.backend.transaction.domain.TransactionCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccountDetailsQuerydslRepository {
    private final JPAQueryFactory queryFactory;

    public List<AccountDetailsResponse> findAccountResponsesBySender(String accountNumber, TransactionCategory transactionCategory) {
        List<AccountDetails> accountDetailsList = queryFactory
                .selectFrom(accountDetails)
                .where(accountDetails.senderAccountNumber.eq(accountNumber),
                        accountDetails.transactionCategory.eq(transactionCategory))
                .orderBy(accountDetails.createdAt.desc())
                .fetch();

        return findAccountDetailsResponses(accountDetailsList);
    }

    public List<AccountDetailsResponse> findAccountResponsesByReceiver(String accountNumber, TransactionCategory transactionCategory) {
        List<AccountDetails> accountDetailsList = queryFactory
                .selectFrom(accountDetails)
                .where(accountDetails.receiverAccountNumber.eq(accountNumber),
                        accountDetails.transactionCategory.eq(transactionCategory))
                .orderBy(accountDetails.createdAt.desc())
                .fetch();

        return findAccountDetailsResponses(accountDetailsList);
    }

    private List<AccountDetailsResponse> findAccountDetailsResponses(List<AccountDetails> accountDetailsList) {
        Set<String> senderAccountNumber = getSenderAccountNumbers(accountDetailsList);
        Map<String, String> senderNicknames = getNickNames(senderAccountNumber);

        Set<String> receiverAccountNumbers = getReceiverAccountNumber(accountDetailsList);
        Map<String, String> receiverNicknames = getNickNames(receiverAccountNumbers);

        return accountDetailsList.stream()
                .map(details -> new AccountDetailsResponse(
                        details.getAmount(),
                        details.getSenderAccountNumber(),
                        details.getReceiverAccountNumber(),
                        senderNicknames.get(details.getSenderAccountNumber()),
                        receiverNicknames.get(details.getReceiverAccountNumber()),
                        details.getMemo(),
                        details.getTransactionCategory(),
                        details.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    private Set<String> getSenderAccountNumbers(List<AccountDetails> accountDetailsList) {
        return accountDetailsList.stream()
                .map(AccountDetails::getSenderAccountNumber)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private Set<String> getReceiverAccountNumber(List<AccountDetails> accountDetailsList) {
        return accountDetailsList.stream()
                .map(AccountDetails::getReceiverAccountNumber)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private Map<String, String> getNickNames(Set<String> accountNumbers) {
        return queryFactory
                .select(user.accountNumber, user.nickname)
                .from(user)
                .where(user.accountNumber.in(accountNumbers))
                .fetch()
                .stream()
                .collect(Collectors.toMap(tuple -> tuple.get(user.accountNumber), tuple -> tuple.get(user.nickname)));
    }
}
