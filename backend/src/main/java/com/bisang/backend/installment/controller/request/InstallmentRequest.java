package com.bisang.backend.installment.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InstallmentRequest {
    private Long teamId;
    private Long userId;
    private Long round;
    private Long amount;
}
