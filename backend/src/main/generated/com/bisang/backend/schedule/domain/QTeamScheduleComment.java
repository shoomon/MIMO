package com.bisang.backend.schedule.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTeamScheduleComment is a Querydsl query type for TeamScheduleComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTeamScheduleComment extends EntityPathBase<TeamScheduleComment> {

    private static final long serialVersionUID = 1437339563L;

    public static final QTeamScheduleComment teamScheduleComment = new QTeamScheduleComment("teamScheduleComment");

    public final com.bisang.backend.common.domain.QBaseTimeEntity _super = new com.bisang.backend.common.domain.QBaseTimeEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final NumberPath<Long> parentCommentId = createNumber("parentCommentId", Long.class);

    public final NumberPath<Long> teamScheduleId = createNumber("teamScheduleId", Long.class);

    public final NumberPath<Long> teamUserId = createNumber("teamUserId", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QTeamScheduleComment(String variable) {
        super(TeamScheduleComment.class, forVariable(variable));
    }

    public QTeamScheduleComment(Path<? extends TeamScheduleComment> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTeamScheduleComment(PathMetadata metadata) {
        super(TeamScheduleComment.class, metadata);
    }

}

