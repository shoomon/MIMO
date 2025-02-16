package com.bisang.backend.installment.domain;

import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@ToString
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "installment",
        indexes = {
                @Index(name = "idx_installment_status",
                        columnList = "team_id, round, installment_status"),
        }
)
public class Installment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "team_id", nullable = false)
    private Long teamId;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "round", nullable = false)
    private Long round;
    @Column(name = "amount", nullable = false)
    private Long amount;
    @Enumerated(EnumType.STRING)
    @Column(name = "installment_status", nullable = false)
    private InstallmentStatus installmentStatus;
    @Column(name = "installment_date")
    private LocalDateTime installmentDate;

    @Builder
    public Installment(
            Long teamId,
            Long userId,
            Long round,
            Long amount,
            InstallmentStatus installmentStatus,
            LocalDateTime installmentDate
    ) {
        this.teamId = teamId;
        this.userId = userId;
        this.round = round;
        this.amount = amount;
        this.installmentStatus = installmentStatus;
        this.installmentDate = installmentDate;
    }

    public boolean isUserPayInstallment() {
        return installmentStatus.toString().equals("YES");
    }

    public void updateInstallmentStatusToYes () {
        this.installmentStatus = InstallmentStatus.YES;
    }

    public void updateInstallmentDate() {
        this.installmentDate = LocalDateTime.now();
    }
}


