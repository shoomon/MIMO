package com.bisang.backend.team.controller.request;

public record DowngradeRoleRequest(
    Long teamId,
    Long teamUserId
) {
}
