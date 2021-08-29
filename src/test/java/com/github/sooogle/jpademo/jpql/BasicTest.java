package com.github.sooogle.jpademo.jpql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.sooogle.jpademo.entity.Pet;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("1. JOINを伴わない基本クエリ")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class BasicTest {

    @PersistenceContext
    EntityManager em;

    //// getResultList

    @Test
    @DisplayName("1.1. Petをnameの昇順で全件取得")
    void testFindAll() {
        List<Pet> pets = em.createQuery("SELECT p FROM Pet p ORDER BY p.name ASC", Pet.class).getResultList();
        for (Pet pet : pets) {
            System.out.println("pet = " + pet);
        }
        assertThat(pets).isNotEmpty();
    }

    // SELECT エイリアス FROM エンティティ名 エイリアス [WHERE ...] [ORDER BY ...]

    @Test
    @DisplayName("1.2. nameが'L'で始まるPetを全件取得")
    void testFindByNameStartsWith() {
        List<Pet> pets = em.createQuery("SELECT p FROM Pet p WHERE p.name LIKE :name", Pet.class)
            .setParameter("name", "L%")
            .getResultList();
        for (Pet pet : pets) {
            System.out.println("pet = " + pet);
        }
        assertThat(pets).allMatch(pet -> pet.getName().startsWith("L"));
    }

    // 比較演算子は =, <>, <, >, <=, >=, LIKE, BETWEEN, INなど
    // Hibernate ユーザーガイド [Relational comparisons](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#hql-conditional-expressions)

    // JPQLでサポートされている関数
    // - 文字列 CONCAT, SUBSTRING, UPPER, LOWER, TRIM, LENGTH, LOCATE
    // - 算術 ABS, MOD, SQRT
    // - 時刻 CURRENT_DATE, CURRENT_TIME, CURRENT_TIMESTAMP
    // Hibernate ユーザーガイド [JPQL standardized functions](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#jpql-standardized-functions)

    // 大文字小文字を無視するなら
    // SELECT p FROM Pet p WHERE LOWER(p.name) LIKE :name
    // として、setParameter("name", "l%")とする

    //// getSingleResult

    @Test
    @DisplayName("1.3. nameが'Leo'のPetを1件取得")
    void testFindOne() {
        Pet pet = em.createQuery("SELECT p FROM Pet p WHERE p.name = :name", Pet.class)
            .setParameter("name", "Leo")
            .getSingleResult();
        System.out.println("pet = " + pet);
        assertThat(pet.getName()).isEqualTo("Leo");
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
        assertThat(pet.getName()).isEqualTo("Lucky");
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

    // Streamを返すgetResultStreamもある

}
