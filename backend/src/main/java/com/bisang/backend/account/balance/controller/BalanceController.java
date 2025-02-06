package com.bisang.backend.account.balance.controller;

import com.bisang.backend.account.balance.controller.request.PaymentResultRequest;
import com.bisang.backend.account.balance.service.BalanceService;
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
public class BalanceController {
    private final BalanceService balanceService;

    @PostMapping("/charge")
    public void chargeBalance(
            @AuthUser User user,
            @RequestBody PaymentResultRequest paymentResultRequest
    ) {
        balanceService.charge(paymentResultRequest);
    }

}
