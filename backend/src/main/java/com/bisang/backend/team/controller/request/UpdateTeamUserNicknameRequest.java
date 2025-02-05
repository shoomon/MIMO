package com.bisang.backend.team.controller.request;

public record UpdateTeamUserNicknameRequest(
    Long teamId,
    String nickname
) {
}
