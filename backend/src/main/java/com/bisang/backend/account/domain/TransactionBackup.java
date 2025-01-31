package com.bisang.backend.account.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(
    name = "transaction_backup"
)
public class TransactionBackup {
    @Id @Column(name = "transaction_backup_id")
    @GeneratedValue(strategy = IDENTITY)
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

    private TransactionBackup(
        String senderAccount,
        String receiverAccount,
        String senderName,
        String receiverName,
        Long balance
    ) {
        this.senderAccount = senderAccount;
        this.receiverAccount = receiverAccount;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.balance = balance;
    }
}
