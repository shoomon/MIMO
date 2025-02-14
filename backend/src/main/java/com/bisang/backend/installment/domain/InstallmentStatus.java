package com.bisang.backend.installment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * YES: 완납
 * NO: 미납
 */
@Getter
@AllArgsConstructor
public enum InstallmentStatus {
    YES, NO
}
