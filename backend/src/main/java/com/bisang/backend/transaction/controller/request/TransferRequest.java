package com.bisang.backend.transaction.controller.request;

import static lombok.AccessLevel.PROTECTED;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class TransferRequest {
    private Long balance;
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private String senderName;
    private String receiverName;
}
