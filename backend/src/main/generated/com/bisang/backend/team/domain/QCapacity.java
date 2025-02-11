package com.bisang.backend.team.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCapacity is a Querydsl query type for Capacity
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QCapacity extends BeanPath<Capacity> {

    private static final long serialVersionUID = 293311232L;

    public static final QCapacity capacity = new QCapacity("capacity");

    public final NumberPath<Long> currentCapacity = createNumber("currentCapacity", Long.class);

    public final NumberPath<Long> maxCapacity = createNumber("maxCapacity", Long.class);

    public QCapacity(String variable) {
        super(Capacity.class, forVariable(variable));
    }

    public QCapacity(Path<? extends Capacity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCapacity(PathMetadata metadata) {
        super(Capacity.class, metadata);
    }

}

