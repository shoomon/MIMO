package com.bisang.backend.team.domain;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(
        name = "team_category",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_team_category", columnNames = {"team_id", "category"})
        }
)
public class TeamCategory {
    @Id
    @Column(name = "team_category_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(length = 10, nullable = false, name = "category")
    private String category;

    private TeamCategory(Long teamId, String category) {
        this.teamId = teamId;
        this.category = category;
    }
}
