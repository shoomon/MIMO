package com.bisang.backend.transaction.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.transaction.controller.request.ChargeRequest;
import com.bisang.backend.transaction.controller.request.PaymentRequest;
import com.bisang.backend.transaction.controller.request.QrCodeRequest;
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
    public void chargeBalance(
            @AuthUser User user,
            @RequestBody ChargeRequest chargeRequest
    ) {
        transactionService.chargeBalance(TransactionService.ADMIN_ACCOUNT_NUMBER, chargeRequest);
    }

    @PostMapping("/transfer")
    public void transferBalance(
            @AuthUser User user,
            @RequestBody TransferRequest transferRequest
    ) {
        transactionService.transferBalance(transferRequest);
    }

    @PostMapping("/payment/qrcode/team")
    public ResponseEntity<String> generateTeamQrCodeDetails(
            @AuthUser User user,
            @RequestBody QrCodeRequest qrCodeRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactionService.generateExpiringUuidForTeam(qrCodeRequest));
    }

    @PostMapping("/payment/qrcode/user")
    public ResponseEntity<String> generateUserQrCodeDetails(
            @AuthUser User user,
            @RequestBody QrCodeRequest qrCodeRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactionService.generateExpiringUuidForUser(qrCodeRequest));
    }

    @PostMapping("/payment/pay")
    public void pay(
            @AuthUser User user,
            @RequestBody PaymentRequest paymentRequest
    ) {
        transactionService.pay(TransactionService.ADMIN_ACCOUNT_NUMBER, paymentRequest);
    }
}
