package com.bisang.backend.account.service;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import com.bisang.backend.common.exception.TeamException;
import com.bisang.backend.team.repository.TeamJpaRepository;
import com.bisang.backend.user.repository.UserJpaRepository;
import org.springframework.stereotype.Service;

import com.bisang.backend.account.domain.Account;
import com.bisang.backend.account.repository.AccountJpaRepository;
import com.bisang.backend.common.exception.AccountException;
import com.bisang.backend.common.exception.ExceptionCode;
import com.bisang.backend.user.domain.User;

import lombok.RequiredArgsConstructor;

import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AccountService {
    private static final int MAX_RETRY = 3;
    private static final String TEAM_ACCOUNT_PREFIX = "1001";
    private static final String USER_ACCOUNT_PREFIX = "1002";
    private static final int RANDOM_NUMBER_RANGE_MIN = 100_000_000;
    private static final int RANDOM_NUMBER_RANGE_MAX = 1_000_000_000;

    private final AccountJpaRepository accountJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final TeamJpaRepository teamJpaRepository;

    public String createTeamAccount() {
        String accountNumber = IntStream.range(0, MAX_RETRY)
                .mapToObj(i -> TEAM_ACCOUNT_PREFIX + createRandomNineNumber())
                .filter(this::validateAccountNumber)
                .findFirst()
                .orElseThrow(()
                        -> new IllegalStateException("세 번의 시도 끝에도 유효한 계좌번호를 생성하지 못했습니다."));

        accountJpaRepository.save(new Account(accountNumber));

        return accountNumber;
    }

    public String createUserAccount() {
        String accountNumber = IntStream.range(0, MAX_RETRY)
                .mapToObj(i -> USER_ACCOUNT_PREFIX + createRandomNineNumber())
                .filter(this::validateAccountNumber)
                .findFirst()
                .orElseThrow(()
                        -> new IllegalStateException("세 번의 시도 끝에도 유효한 계좌번호를 생성하지 못했습니다."));

        accountJpaRepository.save(new Account(accountNumber));

        return accountNumber;
    }

    public void validateTeamAccount(Long teamId, String accountNumber) {
        teamJpaRepository.findByIdAndAccountNumber(teamId, accountNumber)
                .orElseThrow(() -> new AccountException(ExceptionCode.NOT_MATCHED_TEAM_AND_ACCOUNT_NUMBER));
    }

    public void validateUserAccount(Long userId, String accountNumber) {
        userJpaRepository.findByIdAndAccountNumber(userId, accountNumber)
                .orElseThrow(() -> new AccountException(ExceptionCode.NOT_MATCHED_USER_AND_ACCOUNT_NUMBER));
    }

    public Long getUserBalance(User user) {
        String accountNumber = user.getAccountNumber();
        return accountJpaRepository.findByAccountNumber(accountNumber).getBalance().getBalance();
    }

    public Long getTeamBalance(Long teamId) {
        validateTeam(teamId);

        String accountNumber = teamJpaRepository.findTeamById(teamId).get().getAccountNumber();
        return accountJpaRepository.findByAccountNumber(accountNumber).getBalance().getBalance();
    }

    private String createRandomNineNumber() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(RANDOM_NUMBER_RANGE_MIN, RANDOM_NUMBER_RANGE_MAX));
    }

    private Boolean validateAccountNumber(String accountNumber) {
        Optional<Account> optionalAccount = accountJpaRepository.findById(accountNumber);
        return optionalAccount.isEmpty();
    }

    private void validateTeam(Long teamId) {
        teamJpaRepository.findTeamById(teamId)
                .orElseThrow(() -> new TeamException(NOT_FOUND));
    }
}
