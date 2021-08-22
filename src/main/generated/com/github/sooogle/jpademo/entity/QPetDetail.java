package com.github.sooogle.jpademo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPetDetail is a Querydsl query type for PetDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPetDetail extends EntityPathBase<PetDetail> {

    private static final long serialVersionUID = 45958173L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPetDetail petDetail = new QPetDetail("petDetail");

    public final DatePath<java.time.LocalDate> birthDate = createDate("birthDate", java.time.LocalDate.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final QPet pet;

    public QPetDetail(String variable) {
        this(PetDetail.class, forVariable(variable), INITS);
    }

    public QPetDetail(Path<? extends PetDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPetDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPetDetail(PathMetadata metadata, PathInits inits) {
        this(PetDetail.class, metadata, inits);
    }

    public QPetDetail(Class<? extends PetDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.pet = inits.isInitialized("pet") ? new QPet(forProperty("pet"), inits.get("pet")) : null;
    }

}

