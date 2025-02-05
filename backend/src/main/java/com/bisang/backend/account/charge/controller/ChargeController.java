package com.bisang.backend.account.charge.controller;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.user.domain.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bisang.backend.account.charge.controller.request.PaymentResultRequest;
import com.bisang.backend.account.charge.service.ChargeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/point")
public class ChargeController {

    private final ChargeService chargeService;

    @PostMapping("/charge")
    public void chargePoint(
            @AuthUser User user,
            @RequestBody PaymentResultRequest paymentResultRequest
    ) {
        chargeService.chargePoint(paymentResultRequest);
    }

}
