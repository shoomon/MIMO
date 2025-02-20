package com.bisang.backend.team.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTeamTag is a Querydsl query type for TeamTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTeamTag extends EntityPathBase<TeamTag> {

    private static final long serialVersionUID = -1704743689L;

    public static final QTeamTag teamTag = new QTeamTag("teamTag");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath tagName = createString("tagName");

    public final NumberPath<Long> teamId = createNumber("teamId", Long.class);

    public QTeamTag(String variable) {
        super(TeamTag.class, forVariable(variable));
    }

    public QTeamTag(Path<? extends TeamTag> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTeamTag(PathMetadata metadata) {
        super(TeamTag.class, metadata);
    }

}

