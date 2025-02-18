package com.bisang.backend.team.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.bisang.backend.team.domain.TeamNotificationStatus;
import com.bisang.backend.team.domain.TeamPrivateStatus;
import com.bisang.backend.team.domain.TeamRecruitStatus;

public record CreateTeamRequest(
        @NotBlank(message = "닉네임 값은 필수입니다.")
        String nickname,
        @NotNull(message = "알림 설정 값은 필수입니다.")
        TeamNotificationStatus notificationStatus,
        @NotBlank(message = "모임 이름 값은 필수입니다.")
        String name,
        @NotBlank(message = "모임 설명 값은 필수입니다.")
        String description,
        @NotNull(message = "모임 모집 형태 값은 필수입니다.")
        TeamRecruitStatus teamRecruitStatus,
        @NotNull(message = "모임의 공개 여부 값은 필수입니다.")
        TeamPrivateStatus teamPrivateStatus,
        MultipartFile teamProfile,
        @NotNull(message = "지역 값은 필수입니다.")
        String area,
        @NotNull(message = "모임 카테고리 값은 필수입니다.")
        String category,
        @NotNull(message = "모임 최대 인원 값은 필수입니다.")
        Long maxCapacity
) {
}
