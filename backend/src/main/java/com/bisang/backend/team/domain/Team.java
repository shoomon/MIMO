package com.bisang.backend.team.domain;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

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
        @Index(name = "idx_team_area", columnList = "team_area_code, team_id desc"),
        @Index(name = "idx_team_category", columnList = "team_category, team_id desc")
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

    @OneToOne(cascade = ALL, orphanRemoval = true)
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
            Area areaCode,
            TeamCategory category,
            Long maxCapacity
    ) {
        this.maxCapacity = maxCapacity;
        this.teamLeaderId = teamLeaderId;
        this.teamChatroomId = teamChatroomId;
        this.name = name;
        this.shortDescription = description.getDescription().substring(100);
        this.description = description;
        this.accountNumber = accountNumber;
        this.recruitStatus = recruitStatus;
        this.privateStatus = privateStatus;
        this.teamProfileUri = teamProfileUri;
        this.areaCode = areaCode;
        this.category = category;
    }

    public void updateTeamName(String name) {
        this.name = name;
    }

    public void updateRecruitStatus(TeamRecruitStatus recruitStatus) {
        this.recruitStatus = recruitStatus;
    }

    public void updatePrivateStatus(TeamPrivateStatus privateStatus) {
        this.privateStatus = privateStatus;
    }

    public void updateTeamProfileUri(String teamProfileUri) {
        this.teamProfileUri = teamProfileUri;
    }

    public void updateAreaCode(Area areaCode) {
        this.areaCode = areaCode;
    }

    public void updateShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
}
