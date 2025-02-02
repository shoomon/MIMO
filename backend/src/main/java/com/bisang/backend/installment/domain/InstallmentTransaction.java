package com.bisang.backend.installment.domain;

import static com.bisang.backend.installment.domain.InstallmentStatus.N;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "installment_transaction",
        indexes = {
            @Index(name = "idx_installment_trx_team_id_installment_num",
                columnList = "team_id, installment_number desc"),
        }
)
public class InstallmentTransaction {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "installment_trx_id")
    private Long id;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(name = "team_user_id", nullable = false)
    private Long teamUserId;

    @Column(name = "installment_number", nullable = false)
    private Long installmentNumber;

    @Enumerated
    @Column(length = 1, name = "installment_status", nullable = false)
    private InstallmentStatus installmentStatus;

    @Column(name = "balance", nullable = false)
    private Long balance;

    @Column(length = 13, name = "sender_account_number", nullable = false)
    private String senderAccountNumber;

    @Column(length = 13, name = "team_account_number", nullable = false)
    private String teamAccountNumber;

    private InstallmentTransaction(
            Long teamId,
            Long teamUserId,
            Long installmentNumber,
            Long balance,
            String senderAccountNumber,
            String teamAccountNumber
    ) {
        this.teamId = teamId;
        this.teamUserId = teamUserId;
        this.installmentNumber = installmentNumber;
        this.installmentStatus = N;
        this.balance = balance;
        this.senderAccountNumber = senderAccountNumber;
        this.teamAccountNumber = teamAccountNumber;
    }
}
