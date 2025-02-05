package com.bisang.backend.account.balance.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "AccountTransaction",
        indexes = {
                @Index(name = "idx_sender_tx_category",
                        columnList = "senderAccountNumber, transactionCategory"),
                @Index(name = "idx_receiver_tx_category",
                        columnList = "receiverAccountNumber, transactionCategory"),
                @Index(name = "idx_tx_category",
                        columnList = "transactionCategory"
                )
        }
)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Column(nullable = false)
    private Long balance;

    @Column(nullable = true)
    private String senderAccountNumber;

    @Column(nullable = false)
    private String receiverAccountNumber;

    @Column(nullable = true)
    private String senderName;

    @Column(nullable = false)
    private String receiverName;

    @Column(nullable = true)
    private String memo;

    @Column(nullable = true)
    private String impUid;

    @Column(nullable = true)
    private String merchantUid;

    @Enumerated(STRING)
    @Column(nullable = false)
    private TransactionCategory transactionCategory;

    @Enumerated(STRING)
    @Column(nullable = false)
    private TransactionStatus transactionStatus;

    @CreatedDate
    @Column(updatable = false)
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
}
