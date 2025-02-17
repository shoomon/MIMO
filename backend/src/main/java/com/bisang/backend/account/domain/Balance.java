package com.bisang.backend.account.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
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
