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
        name = "AccountDetails",
        indexes = {
                @Index(name = "idx_sender_tx_category",
                        columnList = "senderAccountNumber, transactionStatus, transactionId desc"),
                @Index(name = "idx_receiver_tx_category",
                        columnList = "receiverAccountNumber, transactionStatus, transactionId desc"),
                @Index(name = "idx_tx_status, transaction_id",
                        columnList = "transactionStatus"
                )
        }
)
public class AccountDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountDetailsId;

    @OneToOne
    @JoinColumn(name = "transaction_id", unique = true, nullable = false)
    private Transaction transactionId;

    @Column(nullable = false)
    private Long balance;

    @Column(name = "sender_account_number", nullable = true)
    private String senderAccountNumber;

    @Column(nullable = false)
    private String receiverAccountNumber;

    @Column(nullable = true)
    private String senderName;

    @Column(nullable = false)
    private String receiverName;

    @Column(nullable = true)
    private String memo;

    @Enumerated(STRING)
    @Column(nullable = false)
    private TransactionCategory transactionCategory;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public AccountDetails(
            Transaction transactionId,
            Long balance,
            String senderAccountNumber,
            String receiverAccountNumber,
            String senderName,
            String receiverName,
            String memo,
            TransactionCategory transactionCategory
    ) {
        this.transactionId = transactionId;
        this.balance = balance;
        this.senderAccountNumber = senderAccountNumber;
        this.receiverAccountNumber = receiverAccountNumber;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.memo = memo;
        this.transactionCategory = transactionCategory;
    }
}