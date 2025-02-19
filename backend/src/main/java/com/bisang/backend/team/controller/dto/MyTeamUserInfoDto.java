package com.bisang.backend.team.controller.dto;

import static com.bisang.backend.common.utils.StringUtils.randomAlphaNumeric;
import static com.bisang.backend.team.domain.TeamUserRole.GUEST;

import java.time.LocalDateTime;
import java.util.Optional;

import com.bisang.backend.invite.domain.TeamInvite;
import com.bisang.backend.team.domain.Team;
import com.bisang.backend.team.domain.TeamNotificationStatus;
import com.bisang.backend.team.domain.TeamUser;
import com.bisang.backend.team.domain.TeamUserRole;

import lombok.Builder;

@Builder
public record MyTeamUserInfoDto(
        Long teamId,
        Long teamUserId,
        Boolean hasReview,
        Boolean isInvited,
        String nickname,
        TeamUserRole role,
        TeamNotificationStatus notificationStatus,
        LocalDateTime joinDate
) {
    public static MyTeamUserInfoDto teamUserToDto(
            Optional<TeamUser> optionalTeamUser,
            Team team,
            Optional<TeamInvite> optionalTeamInvite,
            Boolean hasReview
            ) {
        if (optionalTeamUser.isPresent()) {
            TeamUser teamUser = optionalTeamUser.get();
            return MyTeamUserInfoDto.builder()
                    .teamId(teamUser.getTeamId())
                    .teamUserId(teamUser.getId())
                    .hasReview(hasReview)
                    .isInvited(true)
                    .nickname(teamUser.getNickname())
                    .role(teamUser.getRole())
                    .notificationStatus(teamUser.getStatus())
                    .joinDate(teamUser.getCreatedAt()).build();
        }

        Boolean isInvited = optionalTeamInvite.isPresent();

        return MyTeamUserInfoDto.builder()
                .teamId(team.getId())
                .teamUserId(0L)
                .hasReview(hasReview)
                .isInvited(isInvited)
                .nickname("GUEST" + randomAlphaNumeric(10))
                .role(GUEST)
                .notificationStatus(null)
                .joinDate(LocalDateTime.now())
                .build();
    }
}
