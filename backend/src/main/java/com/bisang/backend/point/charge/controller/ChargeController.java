package com.bisang.backend.point.charge.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bisang.backend.point.charge.controller.request.PaymentResultRequest;
import com.bisang.backend.point.charge.service.ChargeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/point")
public class ChargeController {

    private final ChargeService chargeService;

    @PostMapping("/charge")
    public void chargePoint(
            @RequestBody PaymentResultRequest paymentResultRequest
    ) {
        chargeService.chargePoint(paymentResultRequest);
    }

}
