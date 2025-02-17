package com.bisang.backend.team.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.team.domain.TeamUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamUserJpaRepository extends JpaRepository<TeamUser, Long> {
    List<TeamUser> findByTeamId(Long teamId);

    Optional<TeamUser> findByTeamIdAndUserId(Long teamId, Long userId);

    Long countTeamUserByTeamId(Long teamId);

    Boolean existsByTeamIdAndNickname(Long teamId, String nickname);

    @Query("SELECT u.nickname FROM TeamUser u WHERE u.id = :teamUserId")
    String getTeamUserNickname(@Param("teamUserId") Long teamUserId);
}
