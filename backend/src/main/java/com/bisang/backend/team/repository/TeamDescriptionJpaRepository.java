package com.bisang.backend.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.team.domain.TeamDescription;

public interface TeamDescriptionJpaRepository extends JpaRepository<TeamDescription, Long> {

}
