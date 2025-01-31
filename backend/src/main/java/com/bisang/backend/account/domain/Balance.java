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

    public void increaseBalance(Long money) {
        this.balance += money;
    }

    public void decreaseBalance(Long money) {
        if (this.balance < money) {
            throw new AccountException(NOT_ENOUGH_MONEY);
        }
        this.balance -= money;
    }
}
