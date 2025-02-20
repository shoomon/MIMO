package com.bisang.backend.team.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(
    name = "team_tag",
    indexes = {
        @Index(name = "idx_team_tag_name", columnList = "team_id, tag_name")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_team_user", columnNames = {"tag_name", "team_id"})
    })
public class TeamTag {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "team_tag_id")
    private Long id;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(name = "tag_name", nullable = false)
    private String tagName;

    public TeamTag(Long teamId, String tagName) {
        this.teamId = teamId;
        this.tagName = tagName;
    }
}
