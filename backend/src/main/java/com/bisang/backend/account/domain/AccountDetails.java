package com.bisang.backend.account.domain;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.bisang.backend.transaction.domain.Transaction;
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
        }
)
public class AccountDetails {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "account_details_id")
    private Long accountDetailsId;

    @ManyToOne
    @JoinColumn(name = "transaction_id", referencedColumnName = "transaction_id", nullable = false)
    private Transaction transactionId;  // 단일 트랜잭션

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

    @Enumerated(STRING)
    @Column(name = "transaction_category", nullable = false)
    private TransactionCategory transactionCategory;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
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
