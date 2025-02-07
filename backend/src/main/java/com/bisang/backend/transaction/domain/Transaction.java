package com.bisang.backend.transaction.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
/**
 *  1, 2번 인덱스 : 트랜잭션 오류시 개발자 수동 수정을 위한 인덱스
 *  3 번 인덱스 : 배치 작업을 통한 실패 트랜잭션 자동 복구를 위한 인덱스
 */
@Table(
        name = "AccountTransaction",
        indexes = {
                @Index(name = "idx_sender_tx_status",
                        columnList = "senderAccountNumber, transactionStatus, transactionId"),
                @Index(name = "idx_receiver_tx_status",
                        columnList = "receiverAccountNumber, transactionStatus, transactionId"),
                @Index(name = "idx_tx_status",
                        columnList = "transactionStatus, transactionId"
                )
        }
)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

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

    public void updateTransactionMemo(String memo) {
        this.memo = memo;
    }

    public void updateTransactionCategory(TransactionCategory transactionCategory) {
        this.transactionCategory = transactionCategory;
    }
}
