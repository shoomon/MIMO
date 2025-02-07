package com.bisang.backend.transaction.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * PENDING: 트랜잭션 진행중
 * SUCCESS: 트랜잭션 성공
 * FAIL: 트랜잭션 실패
 */
@Getter
@AllArgsConstructor
public enum TransactionStatus {
    PENDING, SUCCESS, FAIL
}
