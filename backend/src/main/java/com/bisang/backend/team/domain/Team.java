package com.bisang.backend.team.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = PROTECTED)
public class Team {
    @Id @Column(name = "team_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "team_leader_id", nullable = false)
    private Long teamLeaderId;

    @Column(name = "team_name", length = 30, nullable = false, unique = true)
    private String name;

    @OneToOne(cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "team_description_id", referencedColumnName = "team_description_id")
    private TeamDescription description;

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

    @Embedded
    private Capacity capacity;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    protected Team(
            Long teamLeaderId,
            String name,
            String description,
            TeamRecruitStatus recruitStatus,
            TeamPrivateStatus privateStatus,
            String teamProfileUri,
            Area areaCode,
            Long capacity
    ) {
        this.capacity = new Capacity(capacity);
        this.teamLeaderId = teamLeaderId;
        this.name = name;
        this.description = new TeamDescription(description);
        this.recruitStatus = recruitStatus;
        this.privateStatus = privateStatus;
        this.teamProfileUri = teamProfileUri;
        this.areaCode = areaCode;
    }
}
