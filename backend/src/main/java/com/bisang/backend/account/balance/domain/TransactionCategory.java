package com.bisang.backend.account.balance.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * CHARGE: 포인트 충천
 * TRANSFER: 포인트 송금
 * PAYMENT: 포인트 결제
 * WITHDRAWAL: 포인트 출금
 */

@Getter
@AllArgsConstructor
public enum TransactionCategory {
    CHARGE(), TRANSFER(), PAYMENT(), WITHDRAWAL;
}
