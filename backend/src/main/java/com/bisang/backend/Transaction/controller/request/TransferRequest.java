package com.bisang.backend.Transaction.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class TransferRequest {
    private Long balance;

    private String senderAccountNumber;

    private String receiverAccountNumber;

    private String senderName;

    private String receiverName;
}
