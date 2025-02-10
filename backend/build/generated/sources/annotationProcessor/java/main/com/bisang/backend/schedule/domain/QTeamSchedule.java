package com.bisang.backend.schedule.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTeamSchedule is a Querydsl query type for TeamSchedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTeamSchedule extends EntityPathBase<TeamSchedule> {

    private static final long serialVersionUID = -1056770028L;

    public static final QTeamSchedule teamSchedule = new QTeamSchedule("teamSchedule");

    public final com.bisang.backend.common.domain.QBaseTimeEntity _super = new com.bisang.backend.common.domain.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> currentParticipants = createNumber("currentParticipants", Long.class);

    public final DateTimePath<java.time.LocalDateTime> date = createDateTime("date", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final StringPath location = createString("location");

    public final NumberPath<Long> maxParticipants = createNumber("maxParticipants", Long.class);

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public final EnumPath<ScheduleStatus> scheduleStatus = createEnum("scheduleStatus", ScheduleStatus.class);

    public final StringPath shortDescription = createString("shortDescription");

    public final NumberPath<Long> teamId = createNumber("teamId", Long.class);

    public final NumberPath<Long> teamUserId = createNumber("teamUserId", Long.class);

    public final StringPath title = createString("title");

    public QTeamSchedule(String variable) {
        super(TeamSchedule.class, forVariable(variable));
    }

    public QTeamSchedule(Path<? extends TeamSchedule> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTeamSchedule(PathMetadata metadata) {
        super(TeamSchedule.class, metadata);
    }

}

