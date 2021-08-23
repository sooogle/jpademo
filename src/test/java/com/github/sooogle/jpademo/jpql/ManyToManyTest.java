package com.github.sooogle.jpademo.jpql;

import com.github.sooogle.jpademo.entity.Vet;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("4. ManyToManyの関連を伴うクエリ")
public class ManyToManyTest {

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("4.1. ManyToManyの関連エンティティを検索条件にする")
    void testWhereExists() {
        // id = 2のSpecialtyを持つVet (獣医) を検索
        String jpql = "SELECT v FROM Vet v WHERE EXISTS (SELECT 1 FROM Specialty s WHERE s MEMBER OF v.specialities AND s.id = :id)";
        List<Vet> vets = em.createQuery(jpql, Vet.class).setParameter("id", 2).getResultList();
        for (Vet vet : vets) {
            System.out.println("vet = " + vet);
        }
    }

}
