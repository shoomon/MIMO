package com.bisang.backend.account.domain;

import static com.bisang.backend.common.exception.ExceptionCode.NOT_ENOUGH_MONEY;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import com.bisang.backend.common.exception.AccountException;

@Embeddable
public class Balance {

    @Column(nullable = false)
    private Long balance;

    public Balance() {
        this.balance = 0L;
    }

    public void increaseBalance(Long balance) {
        this.balance += balance;
    }

    public void decreaseBalance(Long balance) {
        this.balance -= balance;
    }

    public boolean validateBalance(Long balance) {
        return this.balance - balance >= 0;
    }
}
