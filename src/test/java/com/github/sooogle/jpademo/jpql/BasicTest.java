package com.github.sooogle.jpademo.jpql;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.sooogle.jpademo.entity.Pet;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("1. JOINを伴わない基本クエリ")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class BasicTest {

    @Autowired
    EntityManager em;

    //// getResultList

    @Test
    @DisplayName("1.1. Petをnameの昇順で全件取得")
    void testFindAll() {
        List<Pet> pets = em.createQuery("SELECT p FROM Pet p ORDER BY p.name ASC", Pet.class).getResultList();
        for (Pet pet : pets) {
            System.out.println("pet = " + pet);
        }
    }

    @Test
    @DisplayName("1.2. nameが'L'で始まるPetを全件取得")
    void testFindByNameStartsWith() {
        List<Pet> pets = em.createQuery("SELECT p FROM Pet p WHERE p.name LIKE :name", Pet.class)
            .setParameter("name", "L%")
            .getResultList();
        for (Pet pet : pets) {
            System.out.println("pet = " + pet);
        }
    }

    // 比較演算子は =, <, >, <=, >=, LIKE, BETWEEN, INなどSQLでよく使うものは基本的にある

    //// getSingleResult

    @Test
    @DisplayName("1.3. nameが'Leo'のPetを1件取得")
    void testFindOne() {
        Pet pet = em.createQuery("SELECT p FROM Pet p WHERE p.name = :name", Pet.class)
            .setParameter("name", "Leo")
            .getSingleResult();
        System.out.println("pet = " + pet);
    }

    @Test
    @DisplayName("1.4. nameが'Lucky'のPetは2件存在するのでNonUniqueResultExceptionが発生する")
    void testNonUniqueResult() {
        assertThatThrownBy(() ->
            em.createQuery("SELECT p FROM Pet p WHERE p.name = :name", Pet.class)
                .setParameter("name", "Lucky")
                .getSingleResult()
        ).isInstanceOf(NonUniqueResultException.class);
    }

    @Test
    @DisplayName("1.5. setMaxResults(1)とすると先頭の1件が取得される")
    void testFindFirst() {
        Pet pet = em.createQuery("SELECT p FROM Pet p WHERE p.name = :name", Pet.class)
            .setParameter("name", "Lucky")
            .setMaxResults(1)
            .getSingleResult();
        System.out.println("pet = " + pet);
    }

    @Test
    @DisplayName("1.6. 条件に一致する結果が存在しない場合、NoResultExceptionが発生する")
    void testNoResult() {
        assertThatThrownBy(() ->
            em.createQuery("SELECT p FROM Pet p WHERE p.name = :name", Pet.class)
                .setParameter("name", "Hoge")
                .getSingleResult()
        ).isInstanceOf(NoResultException.class);
    }

    // `EntityManager.find` は結果が存在しない場合nullを返す

}
