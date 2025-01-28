package com.bisang.backend.auth.repository;

import org.springframework.data.repository.CrudRepository;

import com.bisang.backend.auth.domain.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
