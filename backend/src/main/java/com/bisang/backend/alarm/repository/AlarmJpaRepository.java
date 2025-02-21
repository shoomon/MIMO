package com.bisang.backend.alarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.alarm.domain.Alarm;

public interface AlarmJpaRepository extends JpaRepository<Alarm, Long> {
}
