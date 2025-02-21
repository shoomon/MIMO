package com.bisang.backend.team.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTeam is a Querydsl query type for Team
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTeam extends EntityPathBase<Team> {

    private static final long serialVersionUID = -1533881437L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTeam team = new QTeam("team");

    public final StringPath accountNumber = createString("accountNumber");

    public final EnumPath<Area> areaCode = createEnum("areaCode", Area.class);

    public final EnumPath<TeamCategory> category = createEnum("category", TeamCategory.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final QTeamDescription description;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> maxCapacity = createNumber("maxCapacity", Long.class);

    public final StringPath name = createString("name");

    public final EnumPath<TeamPrivateStatus> privateStatus = createEnum("privateStatus", TeamPrivateStatus.class);

    public final EnumPath<TeamRecruitStatus> recruitStatus = createEnum("recruitStatus", TeamRecruitStatus.class);

    public final StringPath shortDescription = createString("shortDescription");

    public final NumberPath<Long> teamChatroomId = createNumber("teamChatroomId", Long.class);

    public final NumberPath<Long> teamLeaderId = createNumber("teamLeaderId", Long.class);

    public final StringPath teamProfileUri = createString("teamProfileUri");

    public final NumberPath<Long> teamRound = createNumber("teamRound", Long.class);

    public QTeam(String variable) {
        this(Team.class, forVariable(variable), INITS);
    }

    public QTeam(Path<? extends Team> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTeam(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTeam(PathMetadata metadata, PathInits inits) {
        this(Team.class, metadata, inits);
    }

    public QTeam(Class<? extends Team> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.description = inits.isInitialized("description") ? new QTeamDescription(forProperty("description")) : null;
    }

}

