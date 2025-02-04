package com.bisang.backend.point.common.repository;

import com.bisang.backend.point.common.domain.PointTransaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PointTransactionJpaRepository extends JpaRepository<PointTransaction, Long> {
}
