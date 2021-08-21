package com.github.sooogle.jpademo.jpql;

import com.github.sooogle.jpademo.entity.Vet;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ManyToManyTest {

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("ManyToManyの関連エンティティを検索条件にする")
    void testWhereExists() {
        // id = 2のSpecialtyを持つVet (獣医) を検索
        String jpql = "select v from Vet v where exists (select 1 from Specialty s where s member of v.specialities and s.id = :id)";
        List<Vet> vets = em.createQuery(jpql, Vet.class).setParameter("id", 2).getResultList();
        vets.forEach(vet -> System.out.println("vet = " + vet));
    }

}
