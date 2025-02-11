package com.bisang.backend.team.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(
    name = "team_tag",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_team_user", columnNames = {"team_id", "tag_id"})
    })
public class TeamTag {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "team_tag_id")
    private Long id;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(name = "tag_id", nullable = false)
    private Long tagId;

    public TeamTag(Long teamId, Long tagId) {
        this.teamId = teamId;
        this.tagId = tagId;
    }
}
