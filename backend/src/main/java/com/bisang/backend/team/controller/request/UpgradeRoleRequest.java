package com.bisang.backend.team.controller.request;

public record UpgradeRoleRequest(
    Long teamId,
    Long teamUserId
) {
}
