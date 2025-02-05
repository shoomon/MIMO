package com.bisang.backend.team.controller.request;

import com.bisang.backend.team.domain.Area;
import com.bisang.backend.team.domain.TeamNotificationStatus;
import com.bisang.backend.team.domain.TeamPrivateStatus;
import com.bisang.backend.team.domain.TeamRecruitStatus;

public record CreateTeamRequest(
    String nickname,
    TeamNotificationStatus notificationStatus,
    String name,
    String description,
    TeamRecruitStatus teamRecruitStatus,
    TeamPrivateStatus teamPrivateStatus,
    String teamProfileUri,
    Area area,
    Long maxCapacity
) {
}
