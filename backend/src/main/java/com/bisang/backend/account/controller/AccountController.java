package com.bisang.backend.account.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bisang.backend.account.domain.AccountDetails;
import com.bisang.backend.account.service.AccountDetailsService;
import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.user.domain.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountDetailsService accountDetailsService;

    @GetMapping("/user/deposit/details")
    public ResponseEntity<List<AccountDetails>> getUserDepositAccountDetails(
            @AuthUser User user
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountDetailsService.getUserDepositAccountDetails(user));
    }

    @GetMapping("/user/charge/details")
    public ResponseEntity<List<AccountDetails>> getUserChargeAccountDetails(
            @AuthUser User user
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountDetailsService.getUserChargeAccountDetails(user));
    }

    @GetMapping("/user/transfer/details")
    public ResponseEntity<List<AccountDetails>> getUserTransferAccountDetails(
            @AuthUser User user
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountDetailsService.getUserTransferAccountDetails(user));
    }

    @GetMapping("/user/pay/details")
    public ResponseEntity<List<AccountDetails>> getUserPayAccountDetails(
            @AuthUser User user
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountDetailsService.getUserPayAccountDetails(user));
    }

    @GetMapping("/user/all/details")
    public ResponseEntity<List<AccountDetails>> getUserAllAccountDetails(
            @AuthUser User user
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountDetailsService.getUserAllAccountDetails(user));
    }
}
