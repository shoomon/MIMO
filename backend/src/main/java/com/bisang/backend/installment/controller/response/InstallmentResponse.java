package com.bisang.backend.installment.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InstallmentResponse {
    private String nickname;
    private Long amount;
    private LocalDateTime installmentDate;
}
