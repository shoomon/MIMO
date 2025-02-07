package com.bisang.backend.account.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.bisang.backend.common.domain.BaseTimeEntity;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "account",
        indexes = {
        }
)
public class Account extends BaseTimeEntity {
<<<<<<< HEAD

    @Id
    @Column(name = "account_number", length = 13)
=======
    @Id @Column(length = 13, name = "account_number")
>>>>>>> 15f939e (feat: myInfo 로직 추가)
    private String accountNumber;

    @Embedded
    private Balance balance;

    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
        this.balance = new Balance();
    }

    public void increaseBalance(Long balance) {
        this.balance.increaseBalance(balance);
    }

    public void decreaseBalance(Long balance) {
        this.balance.decreaseBalance(balance);
    }

    public boolean validateBalance(Long balance) {
        return this.balance.validateBalance(balance);
    }
}
