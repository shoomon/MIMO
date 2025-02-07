package com.bisang.backend.Transaction.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * CHARGE: 잔액 충천
 * TRANSFER: 잔액 송금
 * DEPOSIT: 잔액 입금
 * PAYMENT: 잔액 결제
 */
@Getter
@AllArgsConstructor
public enum TransactionCategory {
    CHARGE(), TRANSFER(), DEPOSIT(), PAYMENT;
}
