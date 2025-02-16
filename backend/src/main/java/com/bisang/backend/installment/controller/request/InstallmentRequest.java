package com.bisang.backend.installment.controller.request;

import com.bisang.backend.transaction.controller.request.TransferRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InstallmentRequest {
    private Long teamId;
    private Long userId;
    private Long round;
    private Long amount;
    private TransferRequest transferRequest;
}
