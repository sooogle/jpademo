package com.github.sooogle.jpademo.entitysub;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPetNoRelation is a Querydsl query type for PetNoRelation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPetNoRelation extends EntityPathBase<PetNoRelation> {

    private static final long serialVersionUID = -1865299117L;

    public static final QPetNoRelation petNoRelation = new QPetNoRelation("petNoRelation");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> ownerId = createNumber("ownerId", Integer.class);

    public final NumberPath<Integer> typeId = createNumber("typeId", Integer.class);

    public QPetNoRelation(String variable) {
        super(PetNoRelation.class, forVariable(variable));
    }

    public QPetNoRelation(Path<? extends PetNoRelation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPetNoRelation(PathMetadata metadata) {
        super(PetNoRelation.class, metadata);
    }

}

