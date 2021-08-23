package com.github.sooogle.jpademo.jpql;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.sooogle.jpademo.entity.Owner;
import com.github.sooogle.jpademo.entity.Pet;
import com.github.sooogle.jpademo.entity.Visit;
import com.github.sooogle.jpademo.entitysub.PetEager;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@DisplayName("2. ManyToOneの関連を伴うクエリ")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class ManyToOneTest {

    @Autowired
    EntityManager em;

    //// N+1問題の説明

    // getOwner実行時にクエリが発行されるのでトランザクションを切る必要がある
    @Transactional(readOnly = true)
    @Test
    @DisplayName("2.1. N+1問題が発生している例")
    void testNPlusOneProblem() {
        List<Pet> pets = em.createQuery("SELECT p FROM Pet p", Pet.class).getResultList();
        System.out.println("=== ↑petsテーブルへのselect文のみが実行される ===");
        System.out.println("=== ↓getOwnerを実行したタイミングでownersテーブルへのselectが実行される ===");
        for (Pet pet : pets) {
            System.out.println("pet = " + pet + ", owner = " + pet.getOwner());
        }
    }

    //// JOIN FETCH - 関連エンティティを1つのSQLで取得

    @Test
    @DisplayName("2.2. 関連エンティティをJOIN FETCHすると1つのSQLで取得できる")
    void testJoinFetch() {
        List<Pet> pets = em.createQuery("SELECT p FROM Pet p INNER JOIN FETCH p.owner ", Pet.class).getResultList();
        for (Pet pet : pets) {
            System.out.println("pet = " + pet + ", owner=" + pet.getOwner());
        }
    }

    @Test
    @DisplayName("2.3. ネストしたJOIN FETCH（誤った例）")
    void testWrongNestedJoinFetch() {
        // Visit -> Pet -> Owner とJOIN FETCHを行う。素直にv.pet.ownerと指定するのはダメ
        String jpql = "SELECT v FROM Visit v INNER JOIN FETCH v.pet INNER JOIN FETCH v.pet.owner";
        assertThatThrownBy(() -> em.createQuery(jpql, Visit.class).getResultList())
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("2.4. ネストしたJOIN FETCHは中間エンティティにエイリアスを付与する")
    void testNestedJoinFetch() {
        // Visit -> Pet -> Owner とJOIN FETCHを行う。Petに対して"p"というエイリアスを付与し、p.ownerでJOIN FETCHする
        String jpql = "SELECT v FROM Visit v INNER JOIN FETCH v.pet p INNER JOIN FETCH p.owner";
        List<Visit> visits = em.createQuery(jpql, Visit.class).getResultList();
        for (Visit visit : visits) {
            System.out.println(
                "visit = " + visit + ", pet = " + visit.getPet() + ", owner = " + visit.getPet().getOwner());
        }
    }

    // getType実行時にクエリが発行されるのでトランザクションを切る必要がある
    @Transactional(readOnly = true)
    @Test
    @DisplayName("2.5. FetchType.EAGERとFetchType.LAZYの違い")
    void testEager() {
        List<PetEager> pets = em.createQuery("select p from PetEager p", PetEager.class).getResultList();
        System.out.println("=== ↑select * from petsの後、select * from ownersがN回実行される ===");
        System.out.println("=== ↓getTypeのタイミングでselect * from typesがN回実行される ===");
        for (PetEager pet : pets) {
            System.out.println("pet = " + pet + ", owner = " + pet.getOwner() + ", type = " + pet.getType());
        }
    }

    // Pet -> Ownerの参照を@ManyToOne(fetch = FetchType.EAGER) に変更するとgetResultListのタイミングでOwnerが取得される。
    // Eagerであっても、JOIN FETCHを指定しない限り一発のSQLでは取得されない。
    // この定義の仕方だと、Petだけが取れれば良くてOwnerは不要なケースでも必ずOwnerが取得されてしまうので注意。

    //// JOIN - 結合したエンティティをWHERE句のみで利用する

    // `@Transactional` を付与していないので、`org.hibernate.LazyInitializationException` が発生する
    @Test
    @DisplayName("2.6. 無印のJOINをしてもSELECT句に結合したテーブルは含まれない")
    void testJoin() {
        List<Pet> pets = em.createQuery("SELECT p FROM Pet p INNER JOIN p.owner", Pet.class).getResultList();
        assertThatThrownBy(() -> {
            for (Pet pet : pets) {
                System.out.println("pet = " + pet);
            }
        }).isInstanceOf(LazyInitializationException.class);
    }

    @Test
    @DisplayName("2.7. 無印のJOINは結合先のエンティティをWHERE句のみで利用するケースで使う")
    void testJoinForWhere() {
        String jpql = "SELECT p FROM Pet p INNER JOIN p.owner WHERE p.owner.firstName = :firstName";
        List<Pet> pets = em.createQuery(jpql, Pet.class).setParameter("firstName", "George").getResultList();
        for (Pet pet : pets) {
            System.out.println("pet = " + pet);
        }
    }

    //// JOIN ON - エンティティで関連を張っていないテーブルを取得

    @Test
    @DisplayName("2.8. JOIN ONを利用するとリレーションを張っていないテーブルも取得できる")
    void testJoinOn() {
        String jpql = "SELECT p, o FROM Pet p INNER JOIN Owner o ON o.id = p.owner.id";
        List<Tuple> tuples = em.createQuery(jpql, Tuple.class).getResultList();
        for (Tuple tuple : tuples) {
            System.out.println("pet = " + tuple.get(0, Pet.class) + ", owner = " + tuple.get(1, Owner.class));
        }
    }

    // OneToOneは基本的にManyToOneと同じ。TODO: Lazyにならないパターンについて補足

}
