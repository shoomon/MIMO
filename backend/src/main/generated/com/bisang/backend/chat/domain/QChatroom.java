package com.bisang.backend.chat.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QChatroom is a Querydsl query type for Chatroom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatroom extends EntityPathBase<Chatroom> {

    private static final long serialVersionUID = -1490673292L;

    public static final QChatroom chatroom = new QChatroom("chatroom");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath profileUri = createString("profileUri");

    public final EnumPath<ChatroomStatus> status = createEnum("status", ChatroomStatus.class);

    public final NumberPath<Long> teamId = createNumber("teamId", Long.class);

    public final StringPath title = createString("title");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QChatroom(String variable) {
        super(Chatroom.class, forVariable(variable));
    }

    public QChatroom(Path<? extends Chatroom> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChatroom(PathMetadata metadata) {
        super(Chatroom.class, metadata);
    }

}

