package com.bisang.backend.schedule.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTeamSchedule is a Querydsl query type for TeamSchedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTeamSchedule extends EntityPathBase<TeamSchedule> {

    private static final long serialVersionUID = -1056770028L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTeamSchedule teamSchedule = new QTeamSchedule("teamSchedule");

    public final com.bisang.backend.common.domain.QBaseTimeEntity _super = new com.bisang.backend.common.domain.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> currentParticipants = createNumber("currentParticipants", Long.class);

    public final DateTimePath<java.time.LocalDateTime> date = createDateTime("date", java.time.LocalDateTime.class);

    public final QScheduleDescription description;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final StringPath location = createString("location");

    public final NumberPath<Long> maxParticipants = createNumber("maxParticipants", Long.class);

    public final EnumPath<ScheduleStatus> scheduleStatus = createEnum("scheduleStatus", ScheduleStatus.class);

    public final StringPath shortDescription = createString("shortDescription");

    public final NumberPath<Long> teamId = createNumber("teamId", Long.class);

    public final NumberPath<Long> teamUserId = createNumber("teamUserId", Long.class);

    public final StringPath title = createString("title");

    public QTeamSchedule(String variable) {
        this(TeamSchedule.class, forVariable(variable), INITS);
    }

    public QTeamSchedule(Path<? extends TeamSchedule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTeamSchedule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTeamSchedule(PathMetadata metadata, PathInits inits) {
        this(TeamSchedule.class, metadata, inits);
    }

    public QTeamSchedule(Class<? extends TeamSchedule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.description = inits.isInitialized("description") ? new QScheduleDescription(forProperty("description")) : null;
    }

}

