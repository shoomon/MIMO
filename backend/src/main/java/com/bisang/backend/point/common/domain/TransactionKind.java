package com.bisang.backend.point.common.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TransactionKind {
    CHARGE("충전"),
    TRANSFER("송금"),
    PAYMENT("결제");

    private String kind;
}
