package com.github.sooogle.jpademo.jpql;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.sooogle.jpademo.entity.Owner;
import com.github.sooogle.jpademo.entity.Pet;
import com.github.sooogle.jpademo.entity.PetEager;
import com.github.sooogle.jpademo.entity.Visit;
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
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class ManyToOneTest {

    @Autowired
    EntityManager em;

    // 1. JOIN FETCH 関連エンティティを1つのSQLで取得

    // getOwner実行時にクエリが発行されるのでトランザクションを切る必要がある
    @Transactional(readOnly = true)
    @Test
    @DisplayName("1-1. N+1問題が発生している例")
    void testNPlusOneProblem() {
        List<Pet> pets = em.createQuery("select p from Pet p", Pet.class).getResultList();
        System.out.println("=== ↑petsテーブルへのselect文のみが実行される ===");
        System.out.println("=== ↓getOwnerを実行したタイミングでownersテーブルへのselectが実行される ===");
        for (Pet pet : pets) {
            System.out.println("pet = " + pet + ", owner = " + pet.getOwner());
        }
    }

    @Test
    @DisplayName("1-2. 関連エンティティをJOIN FETCHすると1つのSQLで取得できる")
    void testJoinFetch() {
        List<Pet> pets = em.createQuery("select p from Pet p inner join fetch p.owner ", Pet.class).getResultList();
        for (Pet pet : pets) {
            System.out.println("pet = " + pet + ", owner=" + pet.getOwner());
        }
    }

    @Test
    @DisplayName("1-3. ネストしたJOIN FETCH（誤った例）")
    void testWrongNestedJoinFetch() {
        // Visit -> Pet -> Owner とJOIN FETCHを行う。素直にv.pet.ownerと指定するのはダメ
        String jpql = "select v from Visit v inner join fetch v.pet inner join fetch v.pet.owner";
        assertThatThrownBy(() -> em.createQuery(jpql, Visit.class).getResultList())
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("1-4. ネストしたJOIN FETCHは中間エンティティにエイリアスを付与する")
    void testNestedJoinFetch() {
        // Visit -> Pet -> Owner とJOIN FETCHを行う。Petに対して"p"というエイリアスを付与し、p.ownerでJOIN FETCHする
        String jpql = "select v from Visit v inner join fetch v.pet p inner join fetch p.owner";
        List<Visit> visits = em.createQuery(jpql, Visit.class).getResultList();
        for (Visit visit : visits) {
            System.out.println(
                "visit = " + visit + ", pet = " + visit.getPet() + ", owner = " + visit.getPet().getOwner());
        }
    }

    // getType実行時にクエリが発行されるのでトランザクションを切る必要がある
    @Transactional(readOnly = true)
    @Test
    @DisplayName("1-5. FetchType.EAGERとFetchType.LAZYの違い")
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

    // 2. JOIN 結合したエンティティをWHERE句のみで利用する

    // `@Transactional` を付与していないので、`org.hibernate.LazyInitializationException` が発生する
    @Test
    @DisplayName("2-1. 無印のJOINをしてもSELECT句に結合したテーブルは含まれない")
    void testJoin() {
        List<Pet> pets = em.createQuery("select p from Pet p inner join p.owner", Pet.class).getResultList();
        assertThatThrownBy(() -> System.out.println(pets.get(0).getOwner()))
            .isInstanceOf(LazyInitializationException.class);
    }

    @Test
    @DisplayName("2-2. 無印のJOINは結合先のエンティティをWHERE句のみで利用するケースで使う")
    void testJoinForWhere() {
        String jpql = "select p from Pet p inner join p.owner where p.owner.firstName = :firstName";
        List<Pet> pets = em.createQuery(jpql, Pet.class).setParameter("firstName", "George").getResultList();
        for (Pet pet : pets) {
            System.out.println("pet = " + pet);
        }
    }

    // 3. JOIN ON エンティティで関連を張っていないテーブルを取得

    @Test
    @DisplayName("3. JOIN ONを利用するとリレーションを張っていないテーブルも取得できる")
    void testJoinOn() {
        String jpql = "select p, o from Pet p inner join Owner o on o.id = p.owner.id";
        List<Tuple> tuples = em.createQuery(jpql, Tuple.class).getResultList();
        for (Tuple tuple : tuples) {
            System.out.println("pet = " + tuple.get(0, Pet.class) + ", owner = " + tuple.get(1, Owner.class));
        }
    }

}
