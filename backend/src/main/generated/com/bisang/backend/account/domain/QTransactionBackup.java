package com.bisang.backend.transaction.domain.account.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTransactionBackup is a Querydsl query type for TransactionBackup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTransactionBackup extends EntityPathBase<TransactionBackup> {

    private static final long serialVersionUID = 26581604L;

    public static final QTransactionBackup transactionBackup = new QTransactionBackup("transactionBackup");

    public final NumberPath<Long> balance = createNumber("balance", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath receiverAccount = createString("receiverAccount");

    public final StringPath receiverName = createString("receiverName");

    public final StringPath senderAccount = createString("senderAccount");

    public final StringPath senderName = createString("senderName");

    public QTransactionBackup(String variable) {
        super(TransactionBackup.class, forVariable(variable));
    }

    public QTransactionBackup(Path<? extends TransactionBackup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTransactionBackup(PathMetadata metadata) {
        super(TransactionBackup.class, metadata);
    }

}

