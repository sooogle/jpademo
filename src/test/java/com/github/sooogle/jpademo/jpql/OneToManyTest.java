package com.github.sooogle.jpademo.jpql;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.sooogle.jpademo.entity.Owner;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class OneToManyTest {

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("1-1. OneToManyの関連エンティティを単にJOIN FETCHすると、子側のレコードの数だけ親側のレコードが増幅する")
    void testJoinFetch() {
        String jpql = "select o from Owner o inner join fetch o.pets";
        List<Owner> owners = em.createQuery(jpql, Owner.class).getResultList();
        for (Owner owner : owners) {
            System.out.println("owner = " + owner + " pets = " + owner.getPets());
        }
        assertThat(owners).hasSize(owners.stream().distinct().mapToInt(owner -> owner.getPets().size()).sum());
    }

    @Test
    @DisplayName("1-2. OneToManyの関連エンティティをJOIN FETCHする場合、DISTINCTすると親エンティティ単位で集約される")
    void testJoinFetchDistinct() {
        String jpql = "select distinct o from Owner o inner join fetch o.pets";
        List<Owner> owners = em.createQuery(jpql, Owner.class).getResultList();
        for (Owner owner : owners) {
            System.out.println("owner = " + owner + " pets = " + owner.getPets());
        }
        assertThat(owners).hasSize((int) owners.stream().distinct().count());
    }

    // 注1. OneToManyまたはManyToManyのエンティティをJOINするとsetMaxResultsしてもSQLれべるでlimit指定されない

    // 注2. OneToManyに対するjoinは2回以上行えない（JOIN結果がデカルト積となるため）
    // Hibernateでは `org.hibernate.loader.MultipleBagFetchException` が発生する
    // https://stackoverflow.com/questions/30088649/how-to-use-multiple-join-fetch-in-one-jpql-query/30093606#30093606

    @Test
    @DisplayName("2-1. OneToManyの関連エンティティを検索条件にする（EXISTSを利用）")
    void testWhereExists() {
        // typeId=1のPet（ネコ）を飼っているOwnerを検索
        String jpql = "select o from Owner o where exists (select 1 from Pet p where p.owner = o and p.type.id = :typeId)";
        List<Owner> owners = em.createQuery(jpql, Owner.class).setParameter("typeId", 1).getResultList();
        for (Owner owner : owners) {
            System.out.println("owner = " + owner);
        }
    }

    @Test
    @DisplayName("2-2. OneToManyの関連エンティティを検索条件にする（INを利用）")
    void testWhereIn() {
        // typeId=1のPet（ネコ）を飼っているOwnerを検索
        String jpql = "select o from Owner o where o.id in (select p.owner.id from Pet p where p.type.id = :typeId)";
        List<Owner> owners = em.createQuery(jpql, Owner.class).setParameter("typeId", 1).getResultList();
        for (Owner owner : owners) {
            System.out.println("owner = " + owner);
        }
    }

}
