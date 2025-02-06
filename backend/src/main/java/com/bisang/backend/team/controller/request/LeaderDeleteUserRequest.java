package com.bisang.backend.team.controller.request;

public record LeaderDeleteUserRequest(
    Long teamId,
    Long teamUserId
) {
}
