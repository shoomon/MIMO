package com.bisang.backend.transaction.domain;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
/**
 *  1, 2번 인덱스 : 트랜잭션 오류시 개발자 수동 수정을 위한 인덱스
 *  3 번 인덱스 : 배치 작업을 통한 실패 트랜잭션 자동 복구를 위한 인덱스
 */
@Table(
    name = "account_transaction",
    indexes = {
        @Index(name = "idx_sender_tx_status",
            columnList = "sender_account_number, transaction_status, transaction_id"),
        @Index(name = "idx_receiver_tx_status",
            columnList = "receiver_account_number, transaction_status, transaction_id"),
        @Index(name = "idx_tx_status",
            columnList = "transaction_status, transaction_id")
    }
)
public class Transaction {
    @Id
    @Column(name = "transaction_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long transactionId;

    @Column(name = "balance", nullable = false)
    private Long balance;

    @Column(name = "sender_account_number", nullable = true)
    private String senderAccountNumber;

    @Column(name = "receiver_account_number", nullable = false)
    private String receiverAccountNumber;

    @Column(name = "sender_name", nullable = true)
    private String senderName;

    @Column(name = "receiver_name", nullable = false)
    private String receiverName;

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
        Long balance,
        String senderAccountNumber,
        String receiverAccountNumber,
        String senderName,
        String receiverName,
        String memo,
        String impUid,
        String merchantUid,
        TransactionCategory transactionCategory,
        TransactionStatus transactionStatus
    ) {
        this.balance = balance;
        this.senderAccountNumber = senderAccountNumber;
        this.receiverAccountNumber = receiverAccountNumber;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.memo = memo;
        this.impUid = impUid;
        this.merchantUid = merchantUid;
        this.transactionCategory = transactionCategory;
        this.transactionStatus = transactionStatus;
    }

    public void updateTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public void updateTransactionMemo(String memo) {
        this.memo = memo;
    }

    public void updateTransactionCategory(TransactionCategory transactionCategory) {
        this.transactionCategory = transactionCategory;
    }
}
