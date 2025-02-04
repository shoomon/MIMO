package com.bisang.backend.point.charge.service;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.bisang.backend.account.domain.Account;
import com.bisang.backend.account.repository.AccountJpaRepository;
import com.bisang.backend.point.common.converter.PointConverter;
import com.bisang.backend.point.common.domain.PointTransaction;
import com.bisang.backend.point.charge.controller.request.PaymentResultRequest;
import com.bisang.backend.point.common.repository.PointTransactionJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChargeService {
    private final PointTransactionJpaRepository pointTransactionJpaRepository;
    private final AccountJpaRepository accountJpaRepository;

    @Transactional
    public void chargePoint(PaymentResultRequest paymentResultRequest) {
        PointTransaction pointTransaction = PointConverter.PaymentResultRequestToPointTransaction(paymentResultRequest);

        try {
            recordPointTransactionLog(pointTransaction);
            updateAccountBalance(pointTransaction.getReceiverAccountNumber(), pointTransaction.getPoint());
        } catch (Exception e) {
            // TODO
            // 1. PG사 환불 로직 추가
            // 2. 커스텀 예외 처리 클래스 생성
            throw new IllegalStateException("충전에 실패하였습니다.");
        }
    }

    private void recordPointTransactionLog(PointTransaction pointTransaction) {
        pointTransactionJpaRepository.save(pointTransaction);
    }

    private void updateAccountBalance(String accountNumber, Long point) {
        Account account = accountJpaRepository.findByAccountNumber(accountNumber);
        account.increaseBalance(point);
        accountJpaRepository.save(account);
    }
}
