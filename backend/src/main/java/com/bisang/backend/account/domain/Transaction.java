package com.bisang.backend.account.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(
    name = "transaction",
    indexes = {
        @Index(name = "idx_sender", columnList = "sender_account"),
        @Index(name = "idx_receiver", columnList = "receiver_account")
    }
)
public class Transaction {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    @Column(length = 13, name = "sender_account", nullable = false)
    private String senderAccount;

    @Column(length = 13, name = "receiver_account", nullable = false)
    private String receiverAccount;

    @Column(length = 20, name = "sender_name", nullable = false)
    private String senderName;

    @Column(length = 20, name = "receiver_name", nullable = false)
    private String receiverName;

    @Column(name = "balance", nullable = false)
    private Long balance;

    @Column(length = 60, name = "memo", nullable = false)
    private String memo;

    private Transaction(
        String senderAccount,
        String receiverAccount,
        String senderName,
        String receiverName,
        Long balance,
        String memo
    ) {
        this.senderAccount = senderAccount;
        this.receiverAccount = receiverAccount;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.balance = balance;
        this.memo = memo;
    }
}
