package com.bisang.backend.account.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBalance is a Querydsl query type for Balance
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QBalance extends BeanPath<Balance> {

    private static final long serialVersionUID = 1644302336L;

    public static final QBalance balance1 = new QBalance("balance1");

    public final NumberPath<Long> balance = createNumber("balance", Long.class);

    public QBalance(String variable) {
        super(Balance.class, forVariable(variable));
    }

    public QBalance(Path<? extends Balance> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBalance(PathMetadata metadata) {
        super(Balance.class, metadata);
    }

}

