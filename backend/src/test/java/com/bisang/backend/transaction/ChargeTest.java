package com.bisang.backend.transaction;

import static com.bisang.backend.user.FakeUser.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.*;

import com.bisang.backend.account.domain.Account;
import com.bisang.backend.account.repository.AccountJpaRepository;
import com.bisang.backend.transaction.controller.request.ChargeRequest;
import com.bisang.backend.transaction.service.TransactionService;
import com.bisang.backend.user.FakeUser;
import com.bisang.backend.user.domain.User;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChargeTest {
    @Autowired
    private AccountJpaRepository accountJpaRepository;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private FakeUser fakeUser;

    @Test
    @Order(1)
    @DisplayName("250명의 유저 동시 충전 테스트")
    public void chargeTest() throws InterruptedException {
        fakeUser.createFakeUser();

        ExecutorService executor = Executors.newFixedThreadPool(USER_COUNT);

        long startTime = System.currentTimeMillis();

        try {
            for (int i = 1; i <= USER_COUNT; i++) {
                int userNumber = i;
                executor.submit(() -> {
                    ChargeRequest chargeRequest = ChargeRequest.builder()
                            .amount(10000L)
                            .impUid(null)
                            .merchantUid(null)
                            .build();

                    User user = User.builder()
                            .accountNumber(String.valueOf(TEST_ACCOUNT_NUMBER + userNumber))
                            .build();

                    transactionService.chargeBalance(TransactionService.ADMIN_ACCOUNT_NUMBER, chargeRequest, user);
                });
            }
        } finally {
            executor.shutdown();
            executor.awaitTermination(30, TimeUnit.SECONDS);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("처리 시간: " + duration + " 밀리초");
    }

    @Test
    @Order(2)
    @Transactional
    @DisplayName("동시 충천")
    public void checkChargeTestResult() {
        int successCount = 0;
        int failureCount = 0;

        for (int i = 1; i <= USER_COUNT; i++) {
            String accountNumber = String.valueOf(TEST_ACCOUNT_NUMBER + i);
            Account account = accountJpaRepository.findByAccountNumber(accountNumber);

            if (account.getBalance().getBalance() == 10000L) {
                successCount++;
                continue;
            }

            failureCount++;
        }

        System.out.println("성공 횟수: " + successCount);
        System.out.println("실패 횟수: " + failureCount);

        Assertions.assertEquals(0, failureCount, "실패가 발생했습니다.");
    }
}
