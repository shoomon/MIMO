package com.bisang.backend.account.domain;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.bisang.backend.transaction.domain.TransactionCategory;

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
        name = "account_details",
        indexes = {
                @Index(name = "idx_tx_id",
                        columnList = "transaction_id"),
                @Index(name = "idx_sender_tx_category",
                        columnList = "sender_account_number, transaction_category"),
                @Index(name = "idx_receiver_tx_category",
                        columnList = "receiver_account_number, transaction_category")
        }
)
public class AccountDetails {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "account_details_id")
    private Long id;

    @Column(name = "transaction_id", nullable = false)
    private Long transactionId;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "sender_account_number", nullable = true)
    private String senderAccountNumber;

    @Column(name = "receiver_account_number", nullable = true)
    private String receiverAccountNumber;

    @Column(name = "memo", nullable = true)
    private String memo;

    @Enumerated(STRING)
    @Column(name = "transaction_category", nullable = false)
    private TransactionCategory transactionCategory;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public AccountDetails(
            Long transactionId,
            Long amount,
            String senderAccountNumber,
            String receiverAccountNumber,
            String memo,
            TransactionCategory transactionCategory
    ) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.senderAccountNumber = senderAccountNumber;
        this.receiverAccountNumber = receiverAccountNumber;
        this.memo = memo;
        this.transactionCategory = transactionCategory;
    }
}
