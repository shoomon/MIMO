package com.bisang.backend.invite.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.invite.domain.TeamInvite;
import org.springframework.data.jpa.repository.Query;

public interface TeamInviteJpaRepository extends JpaRepository<TeamInvite, Long> {
    Optional<TeamInvite> findByTeamIdAndUserId(Long teamId, Long userId);

    @Query("select ti from TeamInvite ti where ti.teamId = :teamId and ti.userId = :userId" +
            " and (ti.status = 'WAITING' or ti.status = 'REJECTED')")
    Optional<TeamInvite> findByTeamIdAndUserIdAndStatus(Long teamId, Long userId);
}
