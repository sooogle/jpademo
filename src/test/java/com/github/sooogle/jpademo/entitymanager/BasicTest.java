package com.github.sooogle.jpademo.entitymanager;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.sooogle.jpademo.entity.Owner;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class BasicTest {

    @PersistenceContext
    EntityManager em;

    // 基本CRUD

    @Test
    @DisplayName("1. persistメソッドによるinsert")
    void testPersist() {
        Owner owner = new Owner();
        owner.setFirstName("Taro");
        owner.setLastName("Tanaka");
        em.persist(owner);
        System.out.println("owner = " + owner);
        assertThat(owner.getId()).isNotNull();
    }

    @Test
    @DisplayName("2. findメソッドによるselectとupdate")
    void testFindAndUpdate() {
        Owner owner = em.find(Owner.class, 1);
        System.out.println("owner = " + owner);
        owner.setFirstName("Mike");
        em.flush();
    }

    @Test
    @DisplayName("3. removeメソッドによるdelete")
    void testRemove() {
        Owner owner = new Owner();
        owner.setFirstName("Taro");
        owner.setLastName("Tanaka");
        em.persist(owner);
        em.remove(owner);
        em.flush();
    }

    // PersistenceContext

    @Test
    @DisplayName("4. findを複数回実行してもSQLは1回しか発行されない（L1キャッシュ）")
    void testFindTwice() {
        Owner owner1 = em.find(Owner.class, 1);
        Owner owner2 = em.find(Owner.class, 1);
        assertThat(owner1).isEqualTo(owner2);
    }

    @Test
    @DisplayName("5. JPQLだとSQLが複数回発行される")
    void testGetSingleResultTwice() {
        Owner owner1 = em.createQuery("SELECT o FROM Owner o WHERE o.id = 1", Owner.class).getSingleResult();
        Owner owner2 = em.createQuery("SELECT o FROM Owner o WHERE o.id = 1", Owner.class).getSingleResult();
        assertThat(owner1).isEqualTo(owner2);
    }

    @Test
    @DisplayName("6. detachすると非managed状態になり、mergeするとmanaged状態に戻る")
    void testDetachAndMerge() {
        Owner owner = em.find(Owner.class, 1); // <1>

        System.out.println("===== detach =====");
        em.detach(owner); // <2>
        owner.setFirstName("Mike");
        em.flush();

        System.out.println("===== merge =====");
        em.merge(owner); // <3>
        em.flush();
    }

    // 1. findしてきたエンンティティはmanaged状態
    // 2. detachするとエンティティがdetached状態になる。そのため、エンティティを修正してflushしてもupdate文は発行されない
    // 3. mergeするとエンティティが再度managed状態になる。そのため、flushするとupdate文が発行される。

    @Test
    @DisplayName("7. getReferenceでは、select文を発行せずにidのみが指定されたエンティティが取得される")
    void testGetReference() {
        Owner owner = em.getReference(Owner.class, 1);
        assertThat(owner.getId()).isNotNull();
    }

}
