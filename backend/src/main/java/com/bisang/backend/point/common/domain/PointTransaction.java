package com.bisang.backend.point.common.domain;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import org.springframework.data.annotation.CreatedDate;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "PointTransaction",
        indexes = {
                @Index(name = "idx_sender_account", columnList = "senderAccountNumber"),
                @Index(name = "idx_receiver_account", columnList = "receiverAccountNumber"),
                @Index(name = "idx_transaction_kind", columnList = "transactionKind"),
                @Index(name = "idx_transaction_status", columnList = "transactionStatus")
        }
)
public class PointTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Column(nullable = false)
    private Long point;

    @Column(nullable = false)
    private String senderAccountNumber;

    @Column(nullable = false)
    private String receiverAccountNumber;

    @Column(nullable = false)
    private String senderName;

    @Column(nullable = false)
    private String receiverName;

    private String memo;

    private String impUid;

    private String merchantUid;

    @Enumerated(STRING)
    @Column(nullable = false)
    private TransactionKind transactionKind;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public PointTransaction(Long point, String senderAccountNumber, String receiverAccountNumber, String senderName, String receiverName, String memo, String impUid, String merchantUid, TransactionKind transactionKind) {
        this.point = point;
        this.senderAccountNumber = senderAccountNumber;
        this.receiverAccountNumber = receiverAccountNumber;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.memo = memo;
        this.impUid = impUid;
        this.merchantUid = merchantUid;
        this.transactionKind = transactionKind;
    }
}
