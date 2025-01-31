package com.bisang.backend.team_board.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Table(
        name = "board_description"
)
public class BoardDescription {
    @Id
    @Column(name = "board_description_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String description;

    public BoardDescription(String description) {
        this.description = description;
    }
}

