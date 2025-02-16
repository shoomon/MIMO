package com.bisang.backend.qrcode.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.transaction.controller.request.QrCodeRequest;
import com.bisang.backend.qrcode.service.QrCodeService;
import com.bisang.backend.user.domain.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/qrcode")
public class QrcodeController {
    private final QrCodeService qrCodeService;

    @GetMapping("/team")
    public ResponseEntity<String> generateTeamQrCodeDetails(
            @AuthUser User user,
            @RequestBody QrCodeRequest qrCodeRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(qrCodeService.generateExpiringUuidForTeam(user, qrCodeRequest));
    }

    @GetMapping("/user")
    public ResponseEntity<String> generateUserQrCodeDetails(
            @AuthUser User user,
            @RequestBody QrCodeRequest qrCodeRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(qrCodeService.generateExpiringUuidForUser(user, qrCodeRequest));
    }
}
