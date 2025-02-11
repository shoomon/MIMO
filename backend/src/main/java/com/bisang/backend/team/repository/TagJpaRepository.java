package com.bisang.backend.team.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.team.domain.Tag;

public interface TagJpaRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
}
