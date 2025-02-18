package com.bisang.backend.qrcode.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bisang.backend.auth.annotation.AuthUser;
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
            @RequestParam(name = "teamId") Long teamId,
            @RequestParam(name = "accountNumber") String accountNumber
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(qrCodeService.generateExpiringUuidForTeam(user.getId(), teamId, accountNumber));
    }

    @GetMapping("/user")
    public ResponseEntity<String> generateUserQrCodeDetails(
            @AuthUser User user,
            @RequestParam(name = "accountNumber") String accountNumber
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(qrCodeService.generateExpiringUuidForUser(user, accountNumber));
    }
}
