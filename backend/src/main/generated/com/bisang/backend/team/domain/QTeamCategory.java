package com.bisang.backend.team.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTeamCategory is a Querydsl query type for TeamCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTeamCategory extends EntityPathBase<TeamCategory> {

    private static final long serialVersionUID = -931159615L;

    public static final QTeamCategory teamCategory = new QTeamCategory("teamCategory");

    public final StringPath category = createString("category");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> teamId = createNumber("teamId", Long.class);

    public QTeamCategory(String variable) {
        super(TeamCategory.class, forVariable(variable));
    }

    public QTeamCategory(Path<? extends TeamCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTeamCategory(PathMetadata metadata) {
        super(TeamCategory.class, metadata);
    }

}

