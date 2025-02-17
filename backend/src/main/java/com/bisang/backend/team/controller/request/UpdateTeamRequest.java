package com.bisang.backend.team.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.bisang.backend.team.domain.TeamPrivateStatus;
import com.bisang.backend.team.domain.TeamRecruitStatus;

public record UpdateTeamRequest(
        @NotNull(message = "teamId 값은 필수입니다.")
        Long teamId,
        @NotBlank(message = "이름 값은 필수입니다.")
        String name,
        @NotBlank(message = "설명 값은 필수입니다.")
        String description,
        @NotNull(message = "모집 상태 값은 필수입니다.")
        TeamRecruitStatus recruitStatus,
        @NotNull(message = "공개 여부 상태 값은 필수입니다.")
        TeamPrivateStatus privateStatus,
        MultipartFile profile,
        @NotNull(message = "활동 지역 값은 필수입니다.")
        String area,
        @NotNull(message = "모임 카테고리 값은 필수입니다.")
        String category
) {
}
