package com.bisang.backend.team.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTeamReview is a Querydsl query type for TeamReview
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTeamReview extends EntityPathBase<TeamReview> {

    private static final long serialVersionUID = 1916023003L;

    public static final QTeamReview teamReview = new QTeamReview("teamReview");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath memo = createString("memo");

    public final NumberPath<Long> score = createNumber("score", Long.class);

    public final NumberPath<Long> teamId = createNumber("teamId", Long.class);

    public final NumberPath<Long> teamUserId = createNumber("teamUserId", Long.class);

    public QTeamReview(String variable) {
        super(TeamReview.class, forVariable(variable));
    }

    public QTeamReview(Path<? extends TeamReview> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTeamReview(PathMetadata metadata) {
        super(TeamReview.class, metadata);
    }

}

