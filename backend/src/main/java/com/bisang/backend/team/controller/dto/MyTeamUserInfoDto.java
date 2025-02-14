package com.bisang.backend.team.controller.dto;

import java.time.LocalDateTime;

import com.bisang.backend.team.domain.TeamNotificationStatus;
import com.bisang.backend.team.domain.TeamUser;
import com.bisang.backend.team.domain.TeamUserRole;

import lombok.Builder;

@Builder
public record MyTeamUserInfoDto(
        Long teamId,
        Long teamUserId,
        String nickname,
        TeamUserRole role,
        TeamNotificationStatus notificationStatus,
        LocalDateTime joinDate
) {
    public static MyTeamUserInfoDto teamUserToDto(TeamUser teamUser) {
        return MyTeamUserInfoDto.builder()
                .teamId(teamUser.getTeamId())
                .teamUserId(teamUser.getId())
                .nickname(teamUser.getNickname())
                .role(teamUser.getRole())
                .notificationStatus(teamUser.getStatus())
                .joinDate(teamUser.getCreatedAt()).build();
    }
}
