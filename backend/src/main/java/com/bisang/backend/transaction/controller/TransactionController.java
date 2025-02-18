package com.bisang.backend.transaction.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.transaction.controller.request.ChargeRequest;
import com.bisang.backend.installment.controller.request.InstallmentRequest;
import com.bisang.backend.transaction.controller.request.PaymentRequest;
import com.bisang.backend.transaction.controller.request.TransferRequest;
import com.bisang.backend.transaction.service.TransactionService;
import com.bisang.backend.user.domain.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/balance")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/charge")
    public ResponseEntity<Void> chargeBalance(
            @AuthUser User user,
            @RequestBody ChargeRequest chargeRequest
    ) {
        transactionService.chargeBalance(TransactionService.ADMIN_ACCOUNT_NUMBER, chargeRequest, user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(null);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transferBalance(
            @AuthUser User user,
            @RequestBody TransferRequest transferRequest
    ) {
        transactionService.transferBalance(transferRequest, user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(null);
    }

    @PostMapping("/installment")
    public ResponseEntity<Void> installmentBalance(
            @AuthUser User user,
            @RequestBody InstallmentRequest installmentRequest
    ) {
        transactionService.installmentBalance(
                user.getId(),
                installmentRequest.getTeamId(),
                installmentRequest,
                user
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(null);
    }

    @PostMapping("/pay")
    public ResponseEntity<Void> pay(
            @RequestBody PaymentRequest paymentRequest
    ) {
        transactionService.pay(TransactionService.ADMIN_ACCOUNT_NUMBER, paymentRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(null);
    }
}
