package com.bisang.backend.transaction.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTransaction is a Querydsl query type for Transaction
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTransaction extends EntityPathBase<Transaction> {

    private static final long serialVersionUID = 2052154161L;

    public static final QTransaction transaction = new QTransaction("transaction");

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath impUid = createString("impUid");

    public final StringPath memo = createString("memo");

    public final StringPath merchantUid = createString("merchantUid");

    public final StringPath receiverAccountNumber = createString("receiverAccountNumber");

    public final StringPath senderAccountNumber = createString("senderAccountNumber");

    public final EnumPath<TransactionCategory> transactionCategory = createEnum("transactionCategory", TransactionCategory.class);

    public final EnumPath<TransactionStatus> transactionStatus = createEnum("transactionStatus", TransactionStatus.class);

    public QTransaction(String variable) {
        super(Transaction.class, forVariable(variable));
    }

    public QTransaction(Path<? extends Transaction> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTransaction(PathMetadata metadata) {
        super(Transaction.class, metadata);
    }

}

