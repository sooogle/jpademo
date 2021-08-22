package com.github.sooogle.jpademo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QType is a Querydsl query type for Type
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QType extends EntityPathBase<Type> {

    private static final long serialVersionUID = -797064883L;

    public static final QType type = new QType("type1");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public QType(String variable) {
        super(Type.class, forVariable(variable));
    }

    public QType(Path<? extends Type> path) {
        super(path.getType(), path.getMetadata());
    }

    public QType(PathMetadata metadata) {
        super(Type.class, metadata);
    }

}

