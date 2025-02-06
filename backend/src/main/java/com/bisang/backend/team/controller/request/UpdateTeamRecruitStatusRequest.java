package com.bisang.backend.team.controller.request;

import com.bisang.backend.team.domain.TeamRecruitStatus;

public record UpdateTeamRecruitStatusRequest(
    Long teamId,
    TeamRecruitStatus status
) {
}
