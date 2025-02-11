package com.bisang.backend.board.domain;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
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

    public void updateDescription(String description) {
        this.description = description;
    }
}

