package com.bisang.backend.team.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.team.domain.TeamTag;

public interface TeamTagJpaRepository extends JpaRepository<TeamTag, Long> {
    Optional<TeamTag> findByTeamIdAndTagName(Long teamId, String tagName);
}
