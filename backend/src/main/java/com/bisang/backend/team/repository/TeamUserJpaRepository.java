package com.bisang.backend.team.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.team.domain.TeamUser;

public interface TeamUserJpaRepository extends JpaRepository<TeamUser, Long> {
    List<TeamUser> findByTeamId(Long teamId);

    Optional<TeamUser> findByTeamIdAndUserId(Long teamId, Long userId);

    Long countTeamUserByTeamId(Long teamId);
}
