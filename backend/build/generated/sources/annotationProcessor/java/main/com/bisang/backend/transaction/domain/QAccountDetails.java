package com.bisang.backend.transaction.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAccountDetails is a Querydsl query type for AccountDetails
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccountDetails extends EntityPathBase<AccountDetails> {

    private static final long serialVersionUID = -1869689310L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAccountDetails accountDetails = new QAccountDetails("accountDetails");

    public final NumberPath<Long> accountDetailsId = createNumber("accountDetailsId", Long.class);

    public final NumberPath<Long> balance = createNumber("balance", Long.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath memo = createString("memo");

    public final StringPath receiverAccountNumber = createString("receiverAccountNumber");

    public final StringPath receiverName = createString("receiverName");

    public final StringPath senderAccountNumber = createString("senderAccountNumber");

    public final StringPath senderName = createString("senderName");

    public final EnumPath<TransactionCategory> transactionCategory = createEnum("transactionCategory", TransactionCategory.class);

    public final QTransaction transactionId;

    public QAccountDetails(String variable) {
        this(AccountDetails.class, forVariable(variable), INITS);
    }

    public QAccountDetails(Path<? extends AccountDetails> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAccountDetails(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAccountDetails(PathMetadata metadata, PathInits inits) {
        this(AccountDetails.class, metadata, inits);
    }

    public QAccountDetails(Class<? extends AccountDetails> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.transactionId = inits.isInitialized("transactionId") ? new QTransaction(forProperty("transactionId")) : null;
    }

}

