package com.bisang.backend.team.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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

    public Tag(String name) {
        this.name = name;
    }
}
