package com.bisang.backend.team.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class Capacity {

    @Column(nullable = false)
    protected Long maxCapacity; // 최대 정원

    @Column(nullable = false)
    protected Long currentCapacity; // 현재 가입된 유저 수

    public Capacity(Long maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.currentCapacity = 0L;
    }

    public boolean canJoin() {
        return currentCapacity < maxCapacity;
    }

    public void addMember() {
        if (canJoin()) {
            currentCapacity++;
        } else {
            throw new IllegalStateException("정원이 가득 찼습니다.");
        }
    }

    public void removeMember() {
        if (currentCapacity > 0) {
            currentCapacity--;
        }
    }
}
