package com.github.sooogle.jpademo.entitysub;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPetEager is a Querydsl query type for PetEager
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPetEager extends EntityPathBase<PetEager> {

    private static final long serialVersionUID = 1440303874L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPetEager petEager = new QPetEager("petEager");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final com.github.sooogle.jpademo.entity.QOwner owner;

    public final com.github.sooogle.jpademo.entity.QType type;

    public QPetEager(String variable) {
        this(PetEager.class, forVariable(variable), INITS);
    }

    public QPetEager(Path<? extends PetEager> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPetEager(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPetEager(PathMetadata metadata, PathInits inits) {
        this(PetEager.class, metadata, inits);
    }

    public QPetEager(Class<? extends PetEager> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.owner = inits.isInitialized("owner") ? new com.github.sooogle.jpademo.entity.QOwner(forProperty("owner")) : null;
        this.type = inits.isInitialized("type") ? new com.github.sooogle.jpademo.entity.QType(forProperty("type")) : null;
    }

}

