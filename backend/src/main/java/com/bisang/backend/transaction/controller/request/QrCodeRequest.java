package com.bisang.backend.transaction.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QrCodeRequest {
    private String accountNumber;
    private Long teamId;
}
