package com.bisang.backend.transaction.service.payment;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.bisang.backend.common.exception.ExceptionCode;
import com.bisang.backend.common.exception.TeamException;
import com.bisang.backend.common.exception.TransactionException;
import com.bisang.backend.common.exception.UserException;
import com.bisang.backend.team.repository.TeamJpaRepository;
import com.bisang.backend.team.repository.TeamUserJpaRepository;
import com.bisang.backend.transaction.controller.request.QrCodeRequest;
import com.bisang.backend.user.repository.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QrCodeService {
    private final RedisTemplate<String, String> redisTemplate;
    private final TeamUserJpaRepository teamUserJpaRepository;
    private final TeamJpaRepository teamJpaRepository;
    private final UserJpaRepository userJpaRepository;

    public String generateExpiringUuidForTeam(QrCodeRequest qrCodeRequest) {
        Long teamId = qrCodeRequest.getTeamId();
        Long userId = qrCodeRequest.getUser().getId();
        String accountNumber = qrCodeRequest.getAccountNumber();

        // TODO
        // 아래 컬럼들 인덱싱 처리 필요
        validateTeamLeader(teamId, userId);
        validateTeamAccountNumber(teamId, accountNumber);

        return generateExpiringUuid(accountNumber);
    }

    public String generateExpiringUuidForUser(QrCodeRequest qrCodeRequest) {
        Long userId = qrCodeRequest.getUser().getId();
        String accountNumber = qrCodeRequest.getAccountNumber();

        validateUserAccountNumber(userId, accountNumber);

        return generateExpiringUuid(accountNumber);
    }

    public void validateUuidExpiringTime(String uuid) {
        if (redisTemplate.opsForValue().get(uuid) == null) {
            throw new TransactionException(ExceptionCode.EXPIRE_QR_CODE);
        }
    }

    private void validateTeamLeader(Long teamId, Long userId) {
        if (teamUserJpaRepository.findByTeamIdAndUserId(teamId, userId).isEmpty()) {
            throw new TeamException(ExceptionCode.UNAUTHORIZED_USER);
        }
    }

    private void validateTeamAccountNumber(Long teamId, String accountNumber) {
        if (teamJpaRepository.findByIdAndAccountNumber(teamId, accountNumber).isEmpty()) {
            throw new TeamException(ExceptionCode.NOT_MATCHED_TEAM_AND_ACCOUNT_NUMBER);
        }
    }

    private void validateUserAccountNumber(Long userId, String accountNumber) {
        if (userJpaRepository.findByIdAndAccountNumber(userId, accountNumber).isEmpty()) {
            throw new UserException(ExceptionCode.NOT_MATCHED_USER_AND_ACCOUNT_NUMBER);
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
