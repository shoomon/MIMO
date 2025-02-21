package com.bisang.backend.transaction.domain;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(
        name = "account_transaction",
        indexes = {
                @Index(name = "idx_sender_tx_status",
                        columnList = "sender_account_number, transaction_status"),
                @Index(name = "idx_receiver_tx_status",
                        columnList = "receiver_account_number, transaction_status")
        }
)
public class Transaction {
    @Id
    @Column(name = "transaction_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "sender_account_number", nullable = true)
    private String senderAccountNumber;

    @Column(name = "receiver_account_number", nullable = true)
    private String receiverAccountNumber;

    @Column(name = "memo", nullable = true)
    private String memo;

    @Column(name = "imp_uid", nullable = true)
    private String impUid;

    @Column(name = "merchant_uid", nullable = true)
    private String merchantUid;

    @Enumerated(STRING)
    @Column(name = "transaction_category", nullable = false)
    private TransactionCategory transactionCategory;

    @Enumerated(STRING)
    @Column(name = "transaction_status", nullable = false)
    private TransactionStatus transactionStatus;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Transaction(
            Long amount,
            String senderAccountNumber,
            String receiverAccountNumber,
            String memo,
            String impUid,
            String merchantUid,
            TransactionCategory transactionCategory,
            TransactionStatus transactionStatus
    ) {
        this.amount = amount;
        this.senderAccountNumber = senderAccountNumber;
        this.receiverAccountNumber = receiverAccountNumber;
        this.memo = memo;
        this.impUid = impUid;
        this.merchantUid = merchantUid;
        this.transactionCategory = transactionCategory;
        this.transactionStatus = transactionStatus;
    }

    public void updateTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
}
