package com.github.sooogle.jpademo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVet is a Querydsl query type for Vet
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVet extends EntityPathBase<Vet> {

    private static final long serialVersionUID = 1775404850L;

    public static final QVet vet = new QVet("vet");

    public final StringPath firstName = createString("firstName");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath lastName = createString("lastName");

    public final ListPath<Specialty, QSpecialty> specialities = this.<Specialty, QSpecialty>createList("specialities", Specialty.class, QSpecialty.class, PathInits.DIRECT2);

    public QVet(String variable) {
        super(Vet.class, forVariable(variable));
    }

    public QVet(Path<? extends Vet> path) {
        super(path.getType(), path.getMetadata());
    }

    public QVet(PathMetadata metadata) {
        super(Vet.class, metadata);
    }

}

