package com.bisang.backend.team.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.bisang.backend.team.domain.Area;
import com.bisang.backend.team.domain.TeamCategory;
import com.bisang.backend.team.domain.TeamNotificationStatus;
import com.bisang.backend.team.domain.TeamPrivateStatus;
import com.bisang.backend.team.domain.TeamRecruitStatus;

public record CreateTeamRequest(
        @NotBlank(message = "닉네임은 한글, 영문, 숫자로 최대 30자까지 가능합니다.")
        String nickname,
        @NotNull(message = "알림 설정을 해주세요.")
        TeamNotificationStatus notificationStatus,
        @NotBlank(message = "모임 이름은 한글, 영문, 숫자로 최대 30자까지 가능합니다.")
        String name,
        @NotBlank(message = "팀 설명을 적어주세요.")
        String description,
        @NotNull(message = "모임 모집 형태를 선택해주세요.")
        TeamRecruitStatus teamRecruitStatus,
        @NotNull(message = "모임의 공개 여부에 대해서 알려주세요.")
        TeamPrivateStatus teamPrivateStatus,
        String teamProfileUri,
        @NotNull
        Area area,
        @NotNull
        TeamCategory category,
        @NotNull
        Long maxCapacity
) {
}
