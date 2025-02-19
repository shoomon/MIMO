package com.bisang.backend.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.team.domain.TeamReview;

public interface TeamReviewJpaRepository extends JpaRepository<TeamReview, Long> {
    Boolean existsTeamReviewByTeamUserId(Long teamUserId);
}
