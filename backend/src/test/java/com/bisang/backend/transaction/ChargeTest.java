package com.bisang.backend.transaction;

import static com.bisang.backend.user.FakeUser.*;

import java.util.concurrent.CountDownLatch;
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
    public void concurrentChargeTest() throws InterruptedException {
        fakeUser.createFakeUser();

        ExecutorService executor = Executors.newFixedThreadPool(USER_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(USER_COUNT);

        for (int i = 1; i <= USER_COUNT; i++) {
            final int idx = i;
            executor.submit(() -> {
                try {
                    ChargeRequest chargeRequest = ChargeRequest.builder()
                            .amount(10000L)
                            .impUid(null)
                            .merchantUid(null)
                            .build();

                    User user = User.builder()
                            .accountNumber(String.valueOf(TEST_ACCOUNT_NUMBER + idx))
                            .build();

                    startLatch.await();
                    transactionService.chargeBalance(TransactionService.ADMIN_ACCOUNT_NUMBER, chargeRequest, user);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        long startTime = System.currentTimeMillis();
        startLatch.countDown();
        doneLatch.await(60, TimeUnit.SECONDS);
        executor.shutdown();

        long endTime = System.currentTimeMillis();
        System.out.println("총 처리 시간: " + (endTime - startTime) + " 밀리초");
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
