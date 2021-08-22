package com.github.sooogle.jpademo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSpecialty is a Querydsl query type for Specialty
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSpecialty extends EntityPathBase<Specialty> {

    private static final long serialVersionUID = 1372595915L;

    public static final QSpecialty specialty = new QSpecialty("specialty");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final ListPath<Vet, QVet> vets = this.<Vet, QVet>createList("vets", Vet.class, QVet.class, PathInits.DIRECT2);

    public QSpecialty(String variable) {
        super(Specialty.class, forVariable(variable));
    }

    public QSpecialty(Path<? extends Specialty> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSpecialty(PathMetadata metadata) {
        super(Specialty.class, metadata);
    }

}

