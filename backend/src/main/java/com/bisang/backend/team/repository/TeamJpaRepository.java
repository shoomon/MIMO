package com.bisang.backend.team.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.team.domain.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamJpaRepository extends JpaRepository<Team, Long> {
    Optional<Team> findTeamById(Long id);

    Optional<Team> findByIdAndAccountNumber(Long id, String accountNumber);

    Boolean existsByName(String name);
}
