package com.bisang.backend.board.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBoardDescription is a Querydsl query type for BoardDescription
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardDescription extends EntityPathBase<BoardDescription> {

    private static final long serialVersionUID = -927700213L;

    public static final QBoardDescription boardDescription = new QBoardDescription("boardDescription");

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QBoardDescription(String variable) {
        super(BoardDescription.class, forVariable(variable));
    }

    public QBoardDescription(Path<? extends BoardDescription> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoardDescription(PathMetadata metadata) {
        super(BoardDescription.class, metadata);
    }

}

