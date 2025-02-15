package com.bisang.backend.qrcode.service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.bisang.backend.account.service.AccountService;
import com.bisang.backend.common.exception.TeamException;
import com.bisang.backend.transaction.controller.request.PaymentRequest;
import com.bisang.backend.user.domain.User;
import com.bisang.backend.user.service.UserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.bisang.backend.common.exception.ExceptionCode;
import com.bisang.backend.common.exception.TransactionException;
import com.bisang.backend.team.repository.TeamUserJpaRepository;
import com.bisang.backend.transaction.controller.request.QrCodeRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QrCodeService {
    private final RedisTemplate<String, String> redisTemplate;
    private final TeamUserJpaRepository teamUserJpaRepository;
    private final AccountService accountService;
    private final UserService userService;

    public String generateExpiringUuidForTeam(User user, QrCodeRequest qrCodeRequest) {
        Long teamId = qrCodeRequest.getTeamId();
        Long userId = user.getId();
        String accountNumber = qrCodeRequest.getAccountNumber();
        
        // TODO
        // 아래 컬럼들 인덱싱
        validateTeamLeader(teamId, userId);
        accountService.validateTeamAccount(userId, accountNumber);

        return generateExpiringUuid(accountNumber);
    }

    public String generateExpiringUuidForUser(User user, QrCodeRequest qrCodeRequest) {
        Long userId = user.getId();
        String accountNumber = qrCodeRequest.getAccountNumber();

        accountService.validateUserAccount(userId, accountNumber);

        return generateExpiringUuid(accountNumber);
    }

    public void validateQrCode(String uuid) {
        if (redisTemplate.opsForValue().get(uuid) == null) {
            throw new TransactionException(ExceptionCode.NOT_VALID_QRCODE);
        }
    }

    public User findUserByQrCode(String uuid) {
        String accountNumber = getUserAccountNumberByQrCode(uuid);
        return userService.findUserByAccountNumber(accountNumber);
    }

    private void validateTeamLeader(Long teamId, Long userId) {
        if (teamUserJpaRepository.findByTeamIdAndUserId(teamId, userId).isEmpty()) {
            throw new TeamException(ExceptionCode.UNAUTHORIZED_USER);
        }
    }

    private String generateExpiringUuid(String accountNumber) {
        String uuid = UUID.randomUUID() + "-" + accountNumber;

        redisTemplate.opsForValue().set(uuid, accountNumber, 183, TimeUnit.SECONDS);

        return uuid;
    }

    private String getUserAccountNumberByQrCode(String uuid) {
        return redisTemplate.opsForValue().get(uuid);
    }
}
