package com.bisang.backend.board.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBoardImage is a Querydsl query type for BoardImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardImage extends EntityPathBase<BoardImage> {

    private static final long serialVersionUID = 1547421610L;

    public static final QBoardImage boardImage = new QBoardImage("boardImage");

    public final NumberPath<Long> boardId = createNumber("boardId", Long.class);

    public final StringPath fileExtension = createString("fileExtension");

    public final StringPath fileUri = createString("fileUri");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> teamBoardId = createNumber("teamBoardId", Long.class);

    public final NumberPath<Long> teamId = createNumber("teamId", Long.class);

    public QBoardImage(String variable) {
        super(BoardImage.class, forVariable(variable));
    }

    public QBoardImage(Path<? extends BoardImage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoardImage(PathMetadata metadata) {
        super(BoardImage.class, metadata);
    }

}

