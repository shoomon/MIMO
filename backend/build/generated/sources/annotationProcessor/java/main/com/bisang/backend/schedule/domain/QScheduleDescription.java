package com.bisang.backend.schedule.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QScheduleDescription is a Querydsl query type for ScheduleDescription
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QScheduleDescription extends EntityPathBase<ScheduleDescription> {

    private static final long serialVersionUID = -1018234651L;

    public static final QScheduleDescription scheduleDescription = new QScheduleDescription("scheduleDescription");

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QScheduleDescription(String variable) {
        super(ScheduleDescription.class, forVariable(variable));
    }

    public QScheduleDescription(Path<? extends ScheduleDescription> path) {
        super(path.getType(), path.getMetadata());
    }

    public QScheduleDescription(PathMetadata metadata) {
        super(ScheduleDescription.class, metadata);
    }

}

