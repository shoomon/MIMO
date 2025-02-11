package com.bisang.backend.schedule.controller.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.bisang.backend.schedule.domain.ScheduleStatus;

public record TeamScheduleCreateRequest(
        @NotNull(message = "teamId 값은 필수입니다.")
        Long teamId,
        @NotBlank(message = "일정 제목 값은 필수입니다.")
        String title,
        @NotBlank(message = "일정 설명 값은 필수입니다.")
        String description,
        @NotBlank(message = "일정 장소 값은 필수입니다.")
        String location,
        @NotNull(message = "일정 시간 값은 필수입니다.")
        LocalDateTime date,
        @NotNull(message = "일정 최대 참여자 수 값은 필수입니다.")
        Long maxParticipants,
        @NotNull(message = "일정 참여 금액 값은 필수입니다.")
        Long price,
        @NotNull(message = "일정 유형 값은 필수입니다.")
        ScheduleStatus status
) {
}
