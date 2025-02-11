package com.bisang.backend.schedule.controller.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.bisang.backend.schedule.domain.ScheduleStatus;

public record TeamScheduleUpdateRequest(
        @NotNull(message = "teamId 값은 필수입니다.")
        Long teamId,
        @NotNull(message = "teamScheduleId 값은 필수입니다.")
        Long teamScheduleId,
        @NotBlank(message = "일정 제목 값은 필수입니다.")
        String title,
        @NotBlank(message = "일정 설명 값은 필수입니다.")
        String description,
        @NotNull(message = "날짜 값은 필수입니다.")
        LocalDateTime date,
        @NotBlank(message = "장소 값은 필수입니다.")
        String location,
        @NotNull(message = "최대 참여자 수 값은 필수입니다.")
        Long maxParticipants,
        @NotNull(message = "참여비 값은 필수입니다. 없으면 0원으로 설정해주세요.")
        Long price,
        @NotNull(message = "일정 종류 값은 필수입니다.")
        ScheduleStatus status
) {
}
