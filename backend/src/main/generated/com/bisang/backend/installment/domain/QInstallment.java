package com.bisang.backend.installment.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QInstallment is a Querydsl query type for Installment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInstallment extends EntityPathBase<Installment> {

    private static final long serialVersionUID = -1123618991L;

    public static final QInstallment installment = new QInstallment("installment");

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> installmentDate = createDateTime("installmentDate", java.time.LocalDateTime.class);

    public final EnumPath<InstallmentStatus> installmentStatus = createEnum("installmentStatus", InstallmentStatus.class);

    public final NumberPath<Long> round = createNumber("round", Long.class);

    public final NumberPath<Long> teamId = createNumber("teamId", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QInstallment(String variable) {
        super(Installment.class, forVariable(variable));
    }

    public QInstallment(Path<? extends Installment> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInstallment(PathMetadata metadata) {
        super(Installment.class, metadata);
    }

}

