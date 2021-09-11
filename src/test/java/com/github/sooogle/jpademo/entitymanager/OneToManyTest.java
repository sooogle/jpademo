package com.github.sooogle.jpademo.entitymanager;

import com.github.sooogle.jpademo.entity.Owner;
import com.github.sooogle.jpademo.entity.Pet;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class OneToManyTest {

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("1. 関連エンティティのcreate")
    void testCreate() {
        Owner owner = new Owner(); // <1>
        owner.setFirstName("Taro");
        owner.setLastName("Tanaka");
        Pet pet1 = new Pet();
        pet1.setName("Tama");
        pet1.setTypeId(1);
        pet1.setOwner(owner); // <2>
        Pet pet2 = new Pet();
        pet2.setName("Pochi");
        pet2.setTypeId(2);
        pet2.setOwner(owner);
        owner.setPets(List.of(pet1, pet2)); // <3>
        em.persist(owner); // <4>
    }

    // 1. Ownerエンティティを初期化
    // 2. PetエンティティからOwnerエンティティを参照
    // 3. Ownerのpetリストにエンティティを詰める
    // 4. Petエンティティをpersist

    @Test
    @DisplayName("2. 関連エンティティのupdate")
    void testUpdate() {
        Owner owner = em.find(Owner.class, 1);
        Pet pet = owner.getPets().get(0);
        pet.setTypeId(2);
        em.flush();
    }

    @Test
    @DisplayName("3. 関連エンティティの全削除")
    void testDeleteAll() {
        Owner owner = em.find(Owner.class, 10);
        owner.getPets().clear();
        em.flush();
    }

    @Test
    @DisplayName("4. 関連エンティティの削除")
    void testDeleteWhere() {
        Owner owner = em.find(Owner.class, 10);
        owner.getPets().removeIf(x -> x.getTypeId().equals(1));
        em.flush();
    }
}
