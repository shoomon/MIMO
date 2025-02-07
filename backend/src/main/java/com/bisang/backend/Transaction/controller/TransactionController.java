package com.bisang.backend.Transaction.controller;

import com.bisang.backend.Transaction.controller.request.PaymentResultRequest;
import com.bisang.backend.Transaction.controller.request.TransferRequest;
import com.bisang.backend.Transaction.service.TransactionService;
import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        transactionService.chargeBalance(paymentResultRequest);
    }

    @PostMapping("/transfer")
    public void transferBalance(
            @AuthUser User user,
            @RequestBody TransferRequest transferRequest
    ) {
        transactionService.transferBalance(transferRequest);
    }
}