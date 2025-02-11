package com.bisang.backend.transaction.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentRequest {
    private String uuid;
    private Long paidAmount;
    private String senderAccountNumber;
    private String senderName;
    private String receiverName;
    private String memo;
}
