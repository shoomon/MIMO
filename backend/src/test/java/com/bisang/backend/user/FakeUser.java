package com.bisang.backend.user;

import static com.bisang.backend.transaction.service.TransactionService.ADMIN_ACCOUNT_NUMBER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.account.domain.Account;
import com.bisang.backend.account.repository.AccountJpaRepository;
import com.bisang.backend.user.domain.User;
import com.bisang.backend.user.repository.UserJpaRepository;

@Component
public class FakeUser {
    public static final int USER_COUNT = 250;
    public static final String TEST_USER_NAME = "TEST";
    public static final Long TEST_ACCOUNT_NUMBER = 1000000000000L;

    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private AccountJpaRepository accountJpaRepository;

    @Transactional
    public void createFakeUser() {
        accountJpaRepository.save(new Account(ADMIN_ACCOUNT_NUMBER));

        for (int i = 1; i <= USER_COUNT; i++) {
            String TEST_USER_ACCOUNT_NUMBER = String.valueOf(TEST_ACCOUNT_NUMBER + i);

            User user = User.builder()
                    .accountNumber(TEST_USER_ACCOUNT_NUMBER)
                    .email(TEST_USER_NAME + i + "@email.com")
                    .name(TEST_USER_NAME + i)
                    .nickname(null)
                    .profileUri(null)
                    .build();

            userJpaRepository.save(user);
            accountJpaRepository.save(new Account(TEST_USER_ACCOUNT_NUMBER));
        }
    }
}

