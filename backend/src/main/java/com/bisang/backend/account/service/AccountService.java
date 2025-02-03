package com.bisang.backend.account.service;

import com.bisang.backend.account.domain.Account;
import com.bisang.backend.account.repository.AccountJpaRepository;
import com.bisang.backend.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {
    private static final String TEAM_ACCOUNT_PREFIX = "1001";
    private static final String USER_ACCOUNT_PREFIX = "1002";
    private static final String TEAM_TYPE = "T";
    private static final String USER_TYPE = "U";

    private final AccountJpaRepository accountJpaRepository;

    public Account createTeamAccount(
    ) {
        String accountNumber = createAccountNumber(TEAM_TYPE);
        Account account = new Account(accountNumber);

        return accountJpaRepository.createAccount(account);
    }

    public Account createUserAccount(
    ) {
        String accountNumber = createAccountNumber(USER_TYPE);
        Account account = new Account(accountNumber);

        return accountJpaRepository.createAccount(account);
    }

    private String createAccountNumber(String type) {
        String accountNumber = "";

        String uuid = UUID.randomUUID().toString();
        String nineDigit = uuid.substring(0, 9);
        String accountSuffix = Integer.parseInt(nineDigit, 16) % 1000000 + "";

        if (type.equals("T")) {
            accountNumber = TEAM_ACCOUNT_PREFIX + accountSuffix;
        }

        if (type.equals("U")) {
            accountNumber = USER_ACCOUNT_PREFIX + accountSuffix;
        }

        return accountNumber;
    }
}
