package com.bisang.backend.transaction.controller;

import com.bisang.backend.installment.controller.request.InstallmentRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<Void> chargeBalance(
            @AuthUser User user,
            @RequestBody ChargeRequest chargeRequest
    ) {
        transactionService.chargeBalance(TransactionService.ADMIN_ACCOUNT_NUMBER, chargeRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(null);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transferBalance(
            @AuthUser User user,
            @RequestBody TransferRequest transferRequest
    ) {
        transactionService.transferBalance(transferRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(null);
    }

    @PostMapping("/installment/")
    public ResponseEntity<Void> installmentBalance(
            @AuthUser User user,
            @RequestBody InstallmentRequest installmentRequest,
            @RequestBody TransferRequest transferRequest
    ) {
        transactionService.installmentBalance(installmentRequest, transferRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(null);
    }

    @GetMapping("/payment/qrcode/team")
    public ResponseEntity<String> generateTeamQrCodeDetails(
            @AuthUser User user,
            @RequestBody QrCodeRequest qrCodeRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactionService.generateExpiringUuidForTeam(user, qrCodeRequest));
    }

    @GetMapping("/payment/qrcode/user")
    public ResponseEntity<String> generateUserQrCodeDetails(
            @AuthUser User user,
            @RequestBody QrCodeRequest qrCodeRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactionService.generateExpiringUuidForUser(user, qrCodeRequest));
    }

    @PostMapping("/payment/pay")
    public ResponseEntity<Void> pay(
            @RequestBody PaymentRequest paymentRequest
    ) {
        transactionService.pay(TransactionService.ADMIN_ACCOUNT_NUMBER, paymentRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(null);
    }
}
