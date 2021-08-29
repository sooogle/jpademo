package com.github.sooogle.jpademo.jpql;

import com.github.sooogle.jpademo.entity.Owner;
import com.github.sooogle.jpademo.entitysub.OwnerAndCountDTO;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("5. 集計を伴うクエリ")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class AggregationTest {

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("5.1. COUNT ~ GROUP BYによる件数取得")
    void testGroupBy() {
        String jpql = "SELECT o, COUNT(o) FROM Owner o LEFT JOIN Pet p ON p.owner.id = o.id GROUP BY o";
        List<Tuple> results = em.createQuery(jpql, Tuple.class).getResultList();
        for (Tuple result : results) {
            System.out.println("owner = " + result.get(0, Owner.class) + ", count = " + result.get(1, Long.class));
        }
    }

    // GROUP BY o とすると、Ownerのid（PK）でグルーピングが行われる
    // 集計関数はCOUNT, MIN, MAX, SUM, AVGの5つがサポートされている
    // https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#hql-aggregate-functions

    @Test
    @DisplayName("5.2. HAVING句による絞り込み")
    void testGroupByHaving() {
        String jpql = "SELECT o.id, COUNT(o) FROM Owner o LEFT JOIN Pet p ON p.owner.id = o.id GROUP BY o HAVING COUNT(o) > 1";
        List<Tuple> results = em.createQuery(jpql, Tuple.class).getResultList();
        for (Tuple result : results) {
            System.out.println("id = " + result.get(0, Integer.class) + ", count = " + result.get(1, Long.class));
        }
    }

    // HAVING句を持つ場合、GROUP BYに指定した項目と集計項目しかSELECTできない（これは、標準SQLのGROUP BYの仕様）

    @Test
    @DisplayName("5.3. サブクエリによる件数取得")
    void testSubQuery() {
        String jpql = "SELECT o, (SELECT COUNT(p) FROM Pet p WHERE p.owner.id = o.id) FROM Owner o";
        List<Tuple> results = em.createQuery(jpql, Tuple.class).getResultList();
        for (Tuple result : results) {
            System.out.println("owner = " + result.get(0, Owner.class) + ", count = " + result.get(1, Long.class));
        }
    }

    @Test
    @DisplayName("5.4. コンストラクタ式を利用したDTOへの結果マッピング")
    void testConstructorExpression() {
        String jpql = "SELECT new com.github.sooogle.jpademo.entitysub.OwnerAndCountDTO(o, COUNT(o)) " +
            "FROM Owner o LEFT JOIN Pet p ON p.owner.id = o.id GROUP BY o";
        List<OwnerAndCountDTO> dtos = em.createQuery(jpql, OwnerAndCountDTO.class).getResultList();
        for (OwnerAndCountDTO dto : dtos) {
            System.out.println("owner = " + dto.getOwner() + ", count = " + dto.getCount());
        }
    }

    // 	完全修飾クラス名 (Fully Qualified Class Name; FQCN) を指定する必要があるのでちょっと冗長

    @Test
    @DisplayName("5.5. SIZEによるコレクションの件数取得")
    void testSize() {
        List<Tuple> results = em.createQuery("SELECT o, SIZE(o.pets) FROM Owner o", Tuple.class).getResultList();
        for (Tuple result : results) {
            System.out.println("owner = " + result.get(0, Owner.class) + ", size = " + result.get(1, Integer.class));
        }
    }

    // SIZEはInteger型（おそらく `Collection.size` に対応）

}
