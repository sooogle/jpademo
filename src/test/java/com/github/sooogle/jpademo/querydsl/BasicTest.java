package com.github.sooogle.jpademo.querydsl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.sooogle.jpademo.entity.Pet;
import com.github.sooogle.jpademo.entity.QPet;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BasicTest {

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("結果が複数件の場合はfetchメソッドを使う")
    void testFindAll() {
        JPAQuery<Pet> query = new JPAQuery<>(em);
        QPet p = QPet.pet;
        // SELECT p FROM Pet p WHERE p.name LIKE :name ORDER BY p.name ASC
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
        JPAQuery<Pet> query = new JPAQuery<>(em);
        QPet p = QPet.pet;
        // SELECT p FROM Pet p WHERE p.name = :name AND p.type.id = :id
        List<Pet> pets = query.select(p)
            .from(p)
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
        JPAQuery<Pet> query = new JPAQuery<>(em);
        QPet p = QPet.pet;
        // SELECT p FROM Pet p WHERE p.name = :name AND p.type.id = :id
        List<Pet> pets = query.select(p)
            .from(p)
            .where(p.name.eq("Lucky").and(p.type.id.eq(2)))
            .fetch();
        for (Pet pet : pets) {
            System.out.println("pet = " + pet);
        }
    }

    @Test
    @DisplayName("WHERE句のAND条件3")
    void testWhere3() {
        JPAQuery<Pet> query = new JPAQuery<>(em);
        QPet p = QPet.pet;
        // SELECT p FROM Pet p WHERE p.name = :name AND p.type.id = :id
        List<Pet> pets = query.select(p)
            .from(p)
            .where(p.name.eq("Lucky"))
            .where(p.type.id.eq(2))
            .fetch();
        for (Pet pet : pets) {
            System.out.println("pet = " + pet);
        }
    }


    @Test
    @DisplayName("結果が1件の場合はfetchOneメソッドを使う")
    void testFindOne() {
        JPAQuery<Pet> query = new JPAQuery<>(em);
        QPet p = QPet.pet;
        // SELECT p FROM Pet p WHERE p.name = :name
        Pet pet = query.select(p).from(p).where(p.name.eq("Leo")).fetchOne();
        System.out.println("pet = " + pet);
    }

    @Test
    @DisplayName("fetchOneは結果が複数件だとNonUniqueResultException")
    void testNonUniqueResult() {
        JPAQuery<Pet> query = new JPAQuery<>(em);
        QPet p = QPet.pet;
        // SELECT p FROM Pet p WHERE p.name = :name
        assertThatThrownBy(() -> query.select(p).from(p).where(p.name.eq("Lucky")).fetchOne())
            .isInstanceOf(com.querydsl.core.NonUniqueResultException.class);
    }

    @Test
    @DisplayName("fetchFirstは先頭の1件を返す")
    void testFindFirst() {
        JPAQuery<Pet> query = new JPAQuery<>(em);
        QPet p = QPet.pet;
        // SELECT p FROM Pet p WHERE p.name = :name
        Pet pet = query.select(p).from(p).where(p.name.eq("Lucky")).fetchFirst();
        System.out.println("pet = " + pet);
    }

    @Test
    @DisplayName("条件に一致するレコードが存在しない場合、結果はnull")
    void testNoResult() {
        JPAQuery<Pet> query = new JPAQuery<>(em);
        QPet p = QPet.pet;
        // SELECT p FROM Pet p WHERE p.name = :name
        Pet pet = query.select(p).from(p).where(p.name.eq("Hoge")).fetchFirst();
        System.out.println("pet = " + pet);
        assertThat(pet).isNull();
    }

}
