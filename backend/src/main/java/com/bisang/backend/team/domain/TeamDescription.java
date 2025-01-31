package com.bisang.backend.team.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Table(
        name = "team_description"
)
public class TeamDescription {
    @Id @Column(name = "team_description_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String description;

    public TeamDescription(String description) {
        this.description = description;
    }
}
