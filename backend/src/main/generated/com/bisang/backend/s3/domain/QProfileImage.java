package com.bisang.backend.s3.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProfileImage is a Querydsl query type for ProfileImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProfileImage extends EntityPathBase<ProfileImage> {

    private static final long serialVersionUID = -1104054277L;

    public static final QProfileImage profileImage = new QProfileImage("profileImage");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<ImageType> imageType = createEnum("imageType", ImageType.class);

    public final StringPath profileUri = createString("profileUri");

    public final NumberPath<Long> teamId = createNumber("teamId", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QProfileImage(String variable) {
        super(ProfileImage.class, forVariable(variable));
    }

    public QProfileImage(Path<? extends ProfileImage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProfileImage(PathMetadata metadata) {
        super(ProfileImage.class, metadata);
    }

}

