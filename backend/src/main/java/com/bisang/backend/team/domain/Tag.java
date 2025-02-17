package com.bisang.backend.team.domain;

import static com.bisang.backend.team.domain.TagStatus.EXTRA;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "tag")
public class Tag {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Enumerated(STRING)
    @Column(name = "status", nullable = false)
    private TagStatus status;

    public Tag(String name) {
        this.name = name;
        this.status = EXTRA;
    }
}
