package com.github.sooogle.jpademo.querydsl;

import com.github.sooogle.jpademo.entity.Owner;
import com.github.sooogle.jpademo.entity.QOwner;
import com.github.sooogle.jpademo.entity.QPet;
import com.github.sooogle.jpademo.entitysub.OwnerAndCountDTO;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AggregationTest {

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("COUNT ~ GROUP BYによる件数取得")
    void testGroupBy() {
        JPAQuery<Owner> query = new JPAQuery<>(em);
        QOwner o = QOwner.owner;
        QPet p = QPet.pet;
        // SELECT o, COUNT(o)
        // FROM Owner o
        // LEFT JOIN Pet p ON p.owner.id = o.id
        // GROUP BY o
        List<Tuple> results = query.select(o, o.count())
            .from(o)
            .leftJoin(p).on(p.owner.id.eq(o.id))
            .groupBy(o)
            .fetch();
        for (Tuple result : results) {
            System.out.println("owner = " + result.get(0, Owner.class) + ", count = " + result.get(1, Long.class));
        }
    }

    @Test
    @DisplayName("コンストラクタ式を利用したDTOへの結果マッピング")
    void testConstructorExpression() {
        JPAQuery<Owner> query = new JPAQuery<>(em);
        QOwner o = QOwner.owner;
        QPet p = QPet.pet;
        // SELECT new com.github.sooogle.jpademo.entitysub.OwnerAndCountDTO(o, COUNT(o))
        // FROM Owner o
        // LEFT JOIN Pet p ON p.owner.id = o.id
        // GROUP BY o
        List<OwnerAndCountDTO> dtos = query.select(Projections.constructor(OwnerAndCountDTO.class, o, o.count()))
            .from(o)
            .leftJoin(p).on(p.owner.id.eq(o.id))
            .groupBy(o)
            .fetch();
        for (OwnerAndCountDTO dto : dtos) {
            System.out.println("owner = " + dto.getOwner() + ", count = " + dto.getCount());
        }
    }

}
