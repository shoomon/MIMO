package com.bisang.backend.chat.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QChatroomUser is a Querydsl query type for ChatroomUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatroomUser extends EntityPathBase<ChatroomUser> {

    private static final long serialVersionUID = 2075699039L;

    public static final QChatroomUser chatroomUser = new QChatroomUser("chatroomUser");

    public final NumberPath<Long> chatroomId = createNumber("chatroomId", Long.class);

    public final NumberPath<Long> enterChatId = createNumber("enterChatId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> enterDate = createDateTime("enterDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = createDateTime("lastModifiedAt", java.time.LocalDateTime.class);

    public final StringPath nickname = createString("nickname");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QChatroomUser(String variable) {
        super(ChatroomUser.class, forVariable(variable));
    }

    public QChatroomUser(Path<? extends ChatroomUser> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChatroomUser(PathMetadata metadata) {
        super(ChatroomUser.class, metadata);
    }

}

