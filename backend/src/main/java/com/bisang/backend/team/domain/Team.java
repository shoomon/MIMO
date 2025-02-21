package com.bisang.backend.team.domain;

import static com.bisang.backend.common.exception.ExceptionCode.INVALID_REQUEST;
import static com.bisang.backend.common.exception.ExceptionCode.TEAM_MEMBER_RANGE;
import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static java.lang.Math.min;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import com.bisang.backend.common.exception.TeamException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import org.apache.commons.lang3.Validate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "team",
        indexes = {
            @Index(name = "idx_status_area_team", columnList = "private_status, team_area_code, team_id desc"),
            @Index(name = "idx_status_category_team", columnList = "private_status, team_category, team_id desc"),
            @Index(name = "idx_team_id_accountNumber", columnList = "team_id, team_account_number"),
            @Index(name = "idx_team_round", columnList = "team_round")
        }
)
public class Team {
    @Id @Column(name = "team_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "team_leader_id", nullable = false)
    private Long teamLeaderId;

    @Column(name = "team_chatroom_id", nullable = false)
    private Long teamChatroomId;

    @Column(name = "team_name", length = 30, nullable = false, unique = true)
    private String name;

    @Column(name = "short_description", length = 100, nullable = false)
    private String shortDescription;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "team_description_id", referencedColumnName = "team_description_id")
    private TeamDescription description;

    @Column(length = 13, name = "team_account_number", nullable = false)
    private String accountNumber;

    @Enumerated(STRING)
    @Column(name = "recruit_status", nullable = false)
    private TeamRecruitStatus recruitStatus;

    @Enumerated(STRING)
    @Column(name = "private_status", nullable = false)
    private TeamPrivateStatus privateStatus;

    @Column(name = "team_profile_uri")
    private String teamProfileUri;

    @Column(name = "team_round")
    private Long teamRound;

    @Enumerated(STRING)
    @Column(name = "team_area_code")
    private Area areaCode;

    @Enumerated(STRING)
    @Column(name = "team_category")
    private TeamCategory category;

    @Column(name = "max_capacity", nullable = false)
    protected Long maxCapacity;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Builder
    protected Team(
            Long teamLeaderId,
            Long teamChatroomId,
            String name,
            TeamDescription description,
            String accountNumber,
            TeamRecruitStatus recruitStatus,
            TeamPrivateStatus privateStatus,
            String teamProfileUri,
            Long teamRound,
            Area areaCode,
            TeamCategory category,
            Long maxCapacity
    ) {
        if (maxCapacity < 1 || maxCapacity > 1000) {
            throw new TeamException(TEAM_MEMBER_RANGE);
        }
        this.maxCapacity = maxCapacity;
        this.teamLeaderId = teamLeaderId;
        this.teamChatroomId = teamChatroomId;
        String pattern = "^[a-zA-Z0-9가-힣 ]{1,30}$";
        Validate.matchesPattern(name, pattern,
            "모임 이름은 30자 이하의 영문, 숫자, 한글로 이루어져 있으며 ㅇㅇㅇ 같은 문자는 허용하지 않습니다.");
        this.name = name;
        int shortDescriptionLength = min(description.getDescription().length(), 97);
        this.shortDescription = description.getDescription().substring(0, shortDescriptionLength) + "...";
        this.description = description;
        this.accountNumber = accountNumber;
        this.recruitStatus = recruitStatus;
        this.privateStatus = privateStatus;
        this.teamProfileUri = teamProfileUri;
        this.teamRound = teamRound;
        this.areaCode = areaCode;
        this.category = category;
    }

    public void updateTeamName(String name) {
        String pattern = "^[a-zA-Z0-9가-힣 ]{1,30}$";
        Validate.matchesPattern(name, pattern,
                "모임 이름은 30자 이하의 영문, 숫자, 한글로 이루어져 있으며 ㅇㅇㅇ 같은 문자는 허용하지 않습니다.");
        this.name = name;
    }

    public void updateRecruitStatus(TeamRecruitStatus recruitStatus) {
        this.recruitStatus = recruitStatus;
    }

    public void updatePrivateStatus(TeamPrivateStatus privateStatus) {
        this.privateStatus = privateStatus;
    }

    public void updateTeamProfileUri(String teamProfileUri) {
        if (!teamProfileUri.startsWith("https://bisang-mimo-bucket.s3.ap-northeast-2.amazonaws.com/")) {
            throw new IllegalArgumentException("이미지가 서버 내에 존재하지 않습니다. 이미지 업로드 후 다시 요청해주세요.");
        }
        this.teamProfileUri = teamProfileUri;
    }

    public void updateAreaCode(Area areaCode) {
        this.areaCode = areaCode;
    }

    public void updateDescription(String description) {
        int shortDescriptionLength = min(this.description.getDescription().length(), 97);
        this.shortDescription = this.description.getDescription().substring(0, shortDescriptionLength) + "...";
    }

    public void updateCategory(TeamCategory category) {
        this.category = category;
    }

    public void updateTeamRound(Long teamRound) {
        this.teamRound = teamRound;
    }
}
