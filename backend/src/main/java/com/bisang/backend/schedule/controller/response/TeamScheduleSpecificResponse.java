package com.bisang.backend.schedule.controller.response;

import java.time.LocalDateTime;
import java.util.List;

import com.bisang.backend.schedule.controller.dto.ProfileDto;
import com.bisang.backend.schedule.controller.dto.TeamScheduleCommentDto;
import com.bisang.backend.schedule.domain.ScheduleStatus;

import lombok.Builder;

@Builder
public record TeamScheduleSpecificResponse(
        Long teamScheduleId,
        Boolean isTeamMember,
        Boolean isTeamScheduleMember,
        Boolean isMyTeamSchedule,
        ScheduleStatus status,
        String location,
        LocalDateTime date,
        Long price,
        String nameOfLeader,
        List<ProfileDto> profileUris,
        Long maxParticipants,
        Long currentParticipants,
        String title,
        String description,
        List<TeamScheduleCommentDto> comments
) {
}
