package com.bisang.backend.installment.domain;

/**
 * Y: 납부
 * N: 미납부
 */
public enum InstallmentStatus {
    Y("납부"), N("미납부");

    String name;

    InstallmentStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
