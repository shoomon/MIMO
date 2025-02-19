package com.bisang.backend.account.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAccountDetails is a Querydsl query type for AccountDetails
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccountDetails extends EntityPathBase<AccountDetails> {

    private static final long serialVersionUID = -2010304079L;

    public static final QAccountDetails accountDetails = new QAccountDetails("accountDetails");

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath memo = createString("memo");

    public final StringPath receiverAccountNumber = createString("receiverAccountNumber");

    public final StringPath senderAccountNumber = createString("senderAccountNumber");

    public final EnumPath<com.bisang.backend.transaction.domain.TransactionCategory> transactionCategory = createEnum("transactionCategory", com.bisang.backend.transaction.domain.TransactionCategory.class);

    public final NumberPath<Long> transactionId = createNumber("transactionId", Long.class);

    public QAccountDetails(String variable) {
        super(AccountDetails.class, forVariable(variable));
    }

    public QAccountDetails(Path<? extends AccountDetails> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccountDetails(PathMetadata metadata) {
        super(AccountDetails.class, metadata);
    }

}

