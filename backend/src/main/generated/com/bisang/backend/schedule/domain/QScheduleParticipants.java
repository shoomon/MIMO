package com.bisang.backend.schedule.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QScheduleParticipants is a Querydsl query type for ScheduleParticipants
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QScheduleParticipants extends EntityPathBase<ScheduleParticipants> {

    private static final long serialVersionUID = -1558896681L;

    public static final QScheduleParticipants scheduleParticipants = new QScheduleParticipants("scheduleParticipants");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> teamScheduleId = createNumber("teamScheduleId", Long.class);

    public final NumberPath<Long> teamUserId = createNumber("teamUserId", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final EnumPath<ParticipantsRole> userRole = createEnum("userRole", ParticipantsRole.class);

    public QScheduleParticipants(String variable) {
        super(ScheduleParticipants.class, forVariable(variable));
    }

    public QScheduleParticipants(Path<? extends ScheduleParticipants> path) {
        super(path.getType(), path.getMetadata());
    }

    public QScheduleParticipants(PathMetadata metadata) {
        super(ScheduleParticipants.class, metadata);
    }

}

