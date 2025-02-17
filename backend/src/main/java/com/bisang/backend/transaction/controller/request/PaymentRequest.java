package com.bisang.backend.transaction.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentRequest {
    private String uuid;
    private Long amount;
    private String receiverAccountNumber;
    private String memo;
}
