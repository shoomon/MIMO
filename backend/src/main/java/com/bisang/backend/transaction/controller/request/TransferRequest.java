package com.bisang.backend.transaction.controller.request;

import static lombok.AccessLevel.PROTECTED;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class TransferRequest {
    private Long amount;
    private String receiverAccountNumber;
}
