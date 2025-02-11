package com.bisang.backend.team.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTeamUser is a Querydsl query type for TeamUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTeamUser extends EntityPathBase<TeamUser> {

    private static final long serialVersionUID = -1307399666L;

    public static final QTeamUser teamUser = new QTeamUser("teamUser");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nickname = createString("nickname");

    public final EnumPath<TeamUserRole> role = createEnum("role", TeamUserRole.class);

    public final EnumPath<TeamNotificationStatus> status = createEnum("status", TeamNotificationStatus.class);

    public final NumberPath<Long> teamId = createNumber("teamId", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QTeamUser(String variable) {
        super(TeamUser.class, forVariable(variable));
    }

    public QTeamUser(Path<? extends TeamUser> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTeamUser(PathMetadata metadata) {
        super(TeamUser.class, metadata);
    }

}

