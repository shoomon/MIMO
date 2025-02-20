package com.bisang.backend.qrcode.service;

import com.bisang.backend.account.service.AccountService;
import com.bisang.backend.common.exception.ExceptionCode;
import com.bisang.backend.common.exception.TeamException;
import com.bisang.backend.common.exception.TransactionException;
import com.bisang.backend.team.annotation.TeamLeader;
import com.bisang.backend.team.repository.TeamUserJpaRepository;
import com.bisang.backend.user.domain.User;
import com.bisang.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class QrCodeService {
    private final RedisTemplate<String, String> redisTemplate;
    private final TeamUserJpaRepository teamUserJpaRepository;
    private final AccountService accountService;

    @TeamLeader
    public String generateExpiringUuidForTeam(Long userId, Long teamId, String accountNumber) {
        accountService.validateTeamAccount(teamId, accountNumber);

        return generateExpiringUuid(accountNumber);
    }

    public String generateExpiringUuidForUser(User user, String accountNumber) {
        Long userId = user.getId();
        accountService.validateUserAccount(userId, accountNumber);

        return generateExpiringUuid(accountNumber);
    }

    public String getSenderAccountNumber(String uuid) {
        validateQrCode(uuid);

        return redisTemplate.opsForValue().get(uuid);
    }

    private void validateQrCode(String uuid) {
        String accountNumber = redisTemplate.opsForValue().get(uuid);

        if (accountNumber == null) {
            throw new TransactionException(ExceptionCode.NOT_VALID_QRCODE);
        }
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

    public void expireUuid(String uuid) {
        redisTemplate.delete(uuid);
    }
}
