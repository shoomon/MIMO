package com.bisang.backend.account.charge.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentResultRequest {
    private Long paidAmount;
    private String receiverAccountNumber;
    private String receiverName;
    private String impUid;
    private String merchantUid;
}