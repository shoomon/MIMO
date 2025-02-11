package com.bisang.backend.invite.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTeamInvite is a Querydsl query type for TeamInvite
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTeamInvite extends EntityPathBase<TeamInvite> {

    private static final long serialVersionUID = 677221112L;

    public static final QTeamInvite teamInvite = new QTeamInvite("teamInvite");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath memo = createString("memo");

    public final EnumPath<InviteStatus> status = createEnum("status", InviteStatus.class);

    public final NumberPath<Long> teamId = createNumber("teamId", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QTeamInvite(String variable) {
        super(TeamInvite.class, forVariable(variable));
    }

    public QTeamInvite(Path<? extends TeamInvite> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTeamInvite(PathMetadata metadata) {
        super(TeamInvite.class, metadata);
    }

}

