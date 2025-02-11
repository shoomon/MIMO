package com.bisang.backend.account.service;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.bisang.backend.account.domain.Account;
import com.bisang.backend.account.repository.AccountJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
    private static final int MAX_RETRY = 3;
    private static final String TEAM_ACCOUNT_PREFIX = "1001";
    private static final String USER_ACCOUNT_PREFIX = "1002";
    private static final int RANDOM_NUMBER_RANGE_MIN = 100_000_000;
    private static final int RANDOM_NUMBER_RANGE_MAX = 1_000_000_000;

    private final AccountJpaRepository accountJpaRepository;

    public Account createTeamAccount() {
        String accountNumber = IntStream.range(0, MAX_RETRY)
                .mapToObj(i -> TEAM_ACCOUNT_PREFIX + createRandomNineNumber())
                .filter(this::validateAccountNumber)
                .findFirst()
                .orElseThrow(()
                        -> new IllegalStateException("세 번의 시도 끝에도 유효한 계좌번호를 생성하지 못했습니다."));

        return accountJpaRepository.save(new Account(accountNumber));
    }

    public Account createUserAccount() {
        String accountNumber = IntStream.range(0, MAX_RETRY)
                .mapToObj(i -> USER_ACCOUNT_PREFIX + createRandomNineNumber())
                .filter(this::validateAccountNumber)
                .findFirst()
                .orElseThrow(()
                        -> new IllegalStateException("세 번의 시도 끝에도 유효한 계좌번호를 생성하지 못했습니다."));

        return accountJpaRepository.save(new Account(accountNumber));
    }

    private String createRandomNineNumber() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(RANDOM_NUMBER_RANGE_MIN, RANDOM_NUMBER_RANGE_MAX));
    }

    private Boolean validateAccountNumber(String accountNumber) {
        Optional<Account> optionalAccount = accountJpaRepository.findById(accountNumber);
        return optionalAccount.isEmpty();
    }
}
