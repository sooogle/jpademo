package com.github.sooogle.jpademo.querydsl;

import com.github.sooogle.jpademo.entity.Owner;
import com.github.sooogle.jpademo.entity.QOwner;
import com.github.sooogle.jpademo.entity.QPet;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class OneToManyTest {

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("OneToManyをJOIN FETCHしてDISTINCT")
    void testJoinFetch() {
        JPAQuery<Owner> query = new JPAQuery<>(em);
        QOwner o = QOwner.owner;
        // SELECT DISTINCT o
        // FROM Owner o
        // INNER JOIN FETCH o.pets
        List<Owner> owners = query.select(o)
            .from(o)
            .innerJoin(o.pets).fetchJoin()
            .fetch();
        for (Owner owner : owners) {
            System.out.println("owner = " + owner + ", pets = " + owner.getPets());
        }
    }

    @Test
    @DisplayName("EXISTSを使ってOneToManyの関連エンティティを検索条件にする")
    void testWhereExists() {
        // typeId=1のPet（ネコ）を飼っているOwnerを検索
        JPAQuery<Owner> query = new JPAQuery<>(em);
        QOwner o = QOwner.owner;
        QPet p = QPet.pet;
        // SELECT o
        // FROM Owner o
        // WHERE EXISTS (
        //   SELECT 1
        //   FROM Pet p
        //   WHERE p.owner.id = o.id
        //     AND p.type.id = 1
        // )
        List<Owner> owners = query.select(o)
            .from(o)
            .where(JPAExpressions.selectOne()
                .from(p)
                .where(
                    p.owner.id.eq(o.id),
                    p.type.id.eq(1)
                )
                .exists())
            .fetch();
        for (Owner owner : owners) {
            System.out.println("owner = " + owner);
        }
    }

    @Test
    @DisplayName("INを使ってOneToManyの関連エンティティを検索条件にする")
    void testWhereIn() {
        // typeId=1のPet（ネコ）を飼っているOwnerを検索
        JPAQuery<Owner> query = new JPAQuery<>(em);
        QOwner o = QOwner.owner;
        QPet p = QPet.pet;
        // SELECT o
        // FROM Owner o
        // WHERE o.id IN (
        //   SELECT p.owner.id
        //   FROM Pet p
        //   WHERE p.type.id = 1
        // )
        List<Owner> owners = query.select(o)
            .from(o)
            .where(o.id.in(
                JPAExpressions.select(p.owner.id)
                    .from(p)
                    .where(p.type.id.eq(1))
            ))
            .fetch();
        for (Owner owner : owners) {
            System.out.println("owner = " + owner);
        }
    }
}
