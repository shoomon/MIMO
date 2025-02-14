package com.bisang.backend.transaction.service.payment;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.bisang.backend.account.service.AccountService;
import com.bisang.backend.team.annotation.TeamLeader;
import com.bisang.backend.team.service.TeamLeaderService;
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
    private final TeamLeaderService teamLeadService;

    public String generateExpiringUuidForTeam(User user, QrCodeRequest qrCodeRequest) {
        Long teamId = qrCodeRequest.getTeamId();
        Long userId = user.getId();
        String accountNumber = qrCodeRequest.getAccountNumber();
        
        // TODO
        // 아래 컬럼들 인덱싱
        teamLeadService.validateTeamLeader(teamId, userId);
        accountService.validateTeamAccount(userId, accountNumber);

        return generateExpiringUuid(accountNumber);
    }

    public String generateExpiringUuidForUser(User user, QrCodeRequest qrCodeRequest) {
        Long userId = user.getId();
        String accountNumber = qrCodeRequest.getAccountNumber();

        accountService.validateUserAccount(userId, accountNumber);

        return generateExpiringUuid(accountNumber);
    }

    public void validateUuidExpiringTime(String uuid) {
        if (redisTemplate.opsForValue().get(uuid) == null) {
            throw new TransactionException(ExceptionCode.EXPIRE_QR_CODE);
        }
    }

    private String generateExpiringUuid(String accountNumber) {
        if (validateRedisKeyExistence(accountNumber)) {
            redisTemplate.delete(accountNumber);
        }

        String uuid = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(accountNumber, uuid, 63, TimeUnit.SECONDS);

        return uuid;
    }

    private boolean validateRedisKeyExistence(String accountNumber) {
        return redisTemplate.opsForValue().get(accountNumber) != null;
    }
}
