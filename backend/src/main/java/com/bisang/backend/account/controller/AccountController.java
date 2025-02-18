package com.bisang.backend.account.controller;

import java.util.List;


import com.bisang.backend.account.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bisang.backend.account.controller.response.AccountDetailsResponse;
import com.bisang.backend.account.service.AccountDetailsService;
import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.user.domain.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final AccountDetailsService accountDetailsService;

    @GetMapping("/user/balance")
    public ResponseEntity<Long> getUserBalance(
            @AuthUser User user
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountService.getUserBalance(user));
    }

    @GetMapping("/user/deposit/details")
    public ResponseEntity<List<AccountDetailsResponse>> getUserDepositAccountDetails(
            @AuthUser User user
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountDetailsService.getUserDepositAccountDetails(user));
    }

    @GetMapping("/user/charge/details")
    public ResponseEntity<List<AccountDetailsResponse>> getUserChargeAccountDetails(
            @AuthUser User user
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountDetailsService.getUserChargeAccountDetails(user));
    }

    @GetMapping("/user/transfer/details")
    public ResponseEntity<List<AccountDetailsResponse>> getUserTransferAccountDetails(
            @AuthUser User user
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountDetailsService.getUserTransferAccountDetails(user));
    }

    @GetMapping("/user/pay/details")
    public ResponseEntity<List<AccountDetailsResponse>> getUserPayAccountDetails(
            @AuthUser User user
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountDetailsService.getUserPayAccountDetails(user));
    }

    @GetMapping("/user/all/details")
    public ResponseEntity<List<AccountDetailsResponse>> getUserAllAccountDetails(
            @AuthUser User user
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountDetailsService.getUserAllAccountDetails(user));
    }

    @GetMapping("/team/balance")
    public ResponseEntity<Long> getTeamBalance(
            @RequestParam(name = "teamId") Long teamId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountService.getTeamBalance(teamId));
    }

    @GetMapping("/team/deposit/details")
    public ResponseEntity<List<AccountDetailsResponse>> getTeamDepositAccountDetails(
            @RequestParam(name = "teamId") Long teamId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountDetailsService.getTeamDepositAccountDetails(teamId));
    }

    @GetMapping("/team/pay/details")
    public ResponseEntity<List<AccountDetailsResponse>> getTeamPayAccountDetails(
            @RequestParam(name = "teamId") Long teamId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountDetailsService.getTeamPayAccountDetails(teamId));
    }
}
