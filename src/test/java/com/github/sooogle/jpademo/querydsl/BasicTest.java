package com.github.sooogle.jpademo.querydsl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.sooogle.jpademo.entity.Pet;
import com.github.sooogle.jpademo.entity.QPet;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class BasicTest {

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("結果が複数件の場合はfetchメソッドを使う")
    void testFindAll() {
        JPQLQueryFactory query = new JPAQueryFactory(em);
        QPet p = QPet.pet;
        // SELECT p
        // FROM Pet p
        // WHERE p.name LIKE 'L%'
        // ORDER BY p.name ASC
        List<Pet> pets = query.select(p)
            .from(p)
            .where(p.name.startsWith("L"))
            .orderBy(p.name.asc())
            .fetch();
        for (Pet pet : pets) {
            System.out.println("pet = " + pet);
        }
    }

    @Test
    @DisplayName("WHERE句のAND条件1")
    void testWhere1() {
        JPQLQueryFactory query = new JPAQueryFactory(em);
        QPet p = QPet.pet;
        // SELECT p
        // FROM Pet p
        // WHERE p.name = 'Lucky'
        //   AND p.type.id = 2
        List<Pet> pets = query.selectFrom(p)
            .where(
                p.name.eq("Lucky"),
                p.type.id.eq(2)
            )
            .fetch();
        for (Pet pet : pets) {
            System.out.println("pet = " + pet);
        }
    }

    @Test
    @DisplayName("WHERE句のAND条件2")
    void testWhere2() {
        JPQLQueryFactory query = new JPAQueryFactory(em);
        QPet p = QPet.pet;
        // SELECT p
        // FROM Pet p
        // WHERE p.name = 'Lucky'
        //   AND p.type.id = 2
        List<Pet> pets = query.selectFrom(p)
            .where(p.name.eq("Lucky").and(p.type.id.eq(2)))
            .fetch();
        for (Pet pet : pets) {
            System.out.println("pet = " + pet);
        }
    }

    @Test
    @DisplayName("WHERE句のAND条件3")
    void testWhere3() {
        JPQLQueryFactory query = new JPAQueryFactory(em);
        QPet p = QPet.pet;
        // SELECT p
        // FROM Pet p
        // WHERE p.name = 'Lucky'
        //   AND p.type.id = 2
        List<Pet> pets = query.selectFrom(p)
            .where(p.name.eq("Lucky"))
            .where(p.type.id.eq(2))
            .fetch();
        for (Pet pet : pets) {
            System.out.println("pet = " + pet);
        }
    }

    @Test
    @DisplayName("動的なWHERE句")
    void testDynamicWhere() {
        List<Pet> pets = findByName("L");
        for (Pet pet : pets) {
            System.out.println("pet = " + pet);
        }
    }

    List<Pet> findByName(String name) {
        JPQLQueryFactory query = new JPAQueryFactory(em);
        QPet p = QPet.pet;
        JPQLQuery<Pet> petQuery = query.selectFrom(p)
            .where(p.type.id.eq(2));
        if (name != null && !name.isEmpty()) {
            petQuery.where(p.name.eq(name));
        }
        return petQuery.fetch();
    }

    @Test
    @DisplayName("limitとoffsetの指定")
    void testLimitOffset() {
        JPQLQueryFactory query = new JPAQueryFactory(em);
        QPet p = QPet.pet;
        List<Pet> pets = query.selectFrom(p)
            .limit(5)
            .offset(0)
            .fetch();
        for (Pet pet : pets) {
            System.out.println("pet = " + pet);
        }
    }

    @Test
    @DisplayName("結果が1件の場合はfetchOneメソッドを使う")
    void testFindOne() {
        JPQLQueryFactory query = new JPAQueryFactory(em);
        QPet p = QPet.pet;
        // SELECT p
        // FROM Pet p
        // WHERE p.name = 'Lucky'
        Pet pet = query.selectFrom(p)
            .where(p.name.eq("Leo"))
            .fetchOne();
        System.out.println("pet = " + pet);
    }

    @Test
    @DisplayName("fetchOneは結果が複数件だとNonUniqueResultException")
    void testNonUniqueResult() {
        JPQLQueryFactory query = new JPAQueryFactory(em);
        QPet p = QPet.pet;
        // SELECT p
        // FROM Pet p
        // WHERE p.name = 'Lucky'
        assertThatThrownBy(() -> query.selectFrom(p)
            .where(p.name.eq("Lucky"))
            .fetchOne()
        ).isInstanceOf(com.querydsl.core.NonUniqueResultException.class);
    }

    @Test
    @DisplayName("fetchFirstは先頭の1件を返す")
    void testFindFirst() {
        JPQLQueryFactory query = new JPAQueryFactory(em);
        QPet p = QPet.pet;
        // SELECT p
        // FROM Pet p
        // WHERE p.name = :name
        Pet pet = query.selectFrom(p)
            .where(p.name.eq("Lucky"))
            .fetchFirst();
        System.out.println("pet = " + pet);
    }

    @Test
    @DisplayName("条件に一致するレコードが存在しない場合、結果はnull")
    void testNoResult() {
        JPQLQueryFactory query = new JPAQueryFactory(em);
        QPet p = QPet.pet;
        // SELECT p
        // FROM Pet p
        // WHERE p.name = :name
        Pet pet = query.selectFrom(p)
            .where(p.name.eq("Hoge"))
            .fetchFirst();
        System.out.println("pet = " + pet);
        assertThat(pet).isNull();
    }

}
