package com.bisang.backend.account.controller.response;

import java.time.LocalDateTime;

import com.bisang.backend.transaction.domain.TransactionCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetailsResponse {
    private Long amount;
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private String senderNickname;
    private String receiverNickname;
    private String memo;
    private TransactionCategory transactionCategory;
    private LocalDateTime createdAt;
}
