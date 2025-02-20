package com.bisang.backend.installment.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.installment.controller.request.InstallmentRequest;
import com.bisang.backend.installment.controller.response.InstallmentResponse;
import com.bisang.backend.installment.service.InstallmentService;
import com.bisang.backend.user.domain.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/installment")
@RequiredArgsConstructor
public class InstallmentController {
    private final InstallmentService installmentService;

    @PostMapping("/create")
    public ResponseEntity<Void> createInstallment(
            @AuthUser User user,
            @RequestParam("teamId") Long teamId,
            @RequestBody List<InstallmentRequest> installmentRequests
    ) {
        installmentService.createInstallment(user.getId(), teamId, installmentRequests);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(null);
    }

    @GetMapping("/payer-details")
    public ResponseEntity<List<InstallmentResponse>> getInstallmentPayerDetails(
            @AuthUser User user,
            @RequestParam("teamId") Long teamId,
            @RequestParam("round") Long round
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(installmentService.getInstallmentPayerDetails(user.getId(), teamId, round));
    }

    @GetMapping("/non-payer-details")
    public ResponseEntity<List<InstallmentResponse>> getInstallmentNonPayerDetails(
            @AuthUser User user,
            @RequestParam("teamId") Long teamId,
            @RequestParam("round") Long round
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(installmentService.getInstallmentNonPayerDetails(user.getId(), teamId, round));
    }

    @GetMapping("/check-user-pay")
    public ResponseEntity<Boolean> isUserPayInstallment(
            @AuthUser User user,
            @RequestParam("teamId") Long teamId,
            @RequestParam("round") Long round
    ) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(installmentService.isUserPayInstallment(user.getId(), teamId, round));
    }

    @GetMapping("/get-team-round")
    public ResponseEntity<Long> getTeamRound(
        @AuthUser User user,
        @RequestParam("teamId") Long teamId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(installmentService.getTeamRound(user.getId(), teamId));
    }
}
