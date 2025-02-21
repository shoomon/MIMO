package com.bisang.backend.account.converter;

import java.util.List;

import com.bisang.backend.account.controller.response.AccountDetailsResponse;
import com.bisang.backend.account.domain.AccountDetails;

public class AccountDetailsConverter {
    public static List<AccountDetailsResponse> accountDetailsToAccountDetailsResponses(List<AccountDetails> accountDetails) {
        return accountDetails.stream()
                        .map(it -> {
                            return AccountDetailsResponse.builder()
                                    .amount(it.getAmount())
                                    .senderAccountNumber(it.getSenderAccountNumber())
                                    .receiverAccountNumber(it.getReceiverAccountNumber())
                                    .memo(it.getMemo())
                                    .transactionCategory(it.getTransactionCategory())
                                    .createdAt(it.getCreatedAt())
                                    .build();
                        }).toList();
    }
}
