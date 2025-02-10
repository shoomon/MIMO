package com.bisang.backend.board.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTeamBoard is a Querydsl query type for TeamBoard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTeamBoard extends EntityPathBase<TeamBoard> {

    private static final long serialVersionUID = 1592363604L;

    public static final QTeamBoard teamBoard = new QTeamBoard("teamBoard");

    public final StringPath boardName = createString("boardName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> teamId = createNumber("teamId", Long.class);

    public QTeamBoard(String variable) {
        super(TeamBoard.class, forVariable(variable));
    }

    public QTeamBoard(Path<? extends TeamBoard> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTeamBoard(PathMetadata metadata) {
        super(TeamBoard.class, metadata);
    }

}

