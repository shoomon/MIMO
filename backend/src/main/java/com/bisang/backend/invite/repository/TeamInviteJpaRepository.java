package com.bisang.backend.invite.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.invite.domain.TeamInvite;

public interface TeamInviteJpaRepository extends JpaRepository<TeamInvite, Long> {
    Optional<TeamInvite> findByTeamIdAndUserId(Long teamId, Long userId);
}
