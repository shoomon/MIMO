package com.bisang.backend.transaction.controller;

import static com.bisang.backend.transaction.service.TransactionService.ADMIN_ACCOUNT_NUMBER;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.transaction.controller.request.PaymentResultRequest;
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
            @RequestBody PaymentResultRequest paymentResultRequest
    ) {
        transactionService.chargeBalance(ADMIN_ACCOUNT_NUMBER, paymentResultRequest);
    }

    @PostMapping("/transfer")
    public void transferBalance(
            @AuthUser User user,
            @RequestBody TransferRequest transferRequest
    ) {
        transactionService.transferBalance(transferRequest);
    }
}