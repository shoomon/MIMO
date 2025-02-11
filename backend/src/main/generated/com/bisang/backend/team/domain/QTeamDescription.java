package com.bisang.backend.team.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTeamDescription is a Querydsl query type for TeamDescription
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTeamDescription extends EntityPathBase<TeamDescription> {

    private static final long serialVersionUID = 2050409561L;

    public static final QTeamDescription teamDescription = new QTeamDescription("teamDescription");

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QTeamDescription(String variable) {
        super(TeamDescription.class, forVariable(variable));
    }

    public QTeamDescription(Path<? extends TeamDescription> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTeamDescription(PathMetadata metadata) {
        super(TeamDescription.class, metadata);
    }

}

