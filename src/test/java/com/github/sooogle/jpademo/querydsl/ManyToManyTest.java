package com.github.sooogle.jpademo.querydsl;

import com.github.sooogle.jpademo.entity.QSpecialty;
import com.github.sooogle.jpademo.entity.QVet;
import com.github.sooogle.jpademo.entity.Vet;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ManyToManyTest {

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("ManyToManyの関連エンティティを検索条件にする")
    void testWhereExists() {
        // id = 2のSpecialtyを持つVet (獣医) を検索
        JPQLQueryFactory query = new JPAQueryFactory(em);
        QVet v = QVet.vet;
        QSpecialty s = QSpecialty.specialty;
        // SELECT v
        // FROM Vet v
        // WHERE EXISTS (
        //   SELECT 1
        //   FROM Specialty s
        //   WHERE s MEMBER OF v.specialities
        //     AND s.id = 2
        // )
        List<Vet> vets = query.selectFrom(v)
            .where(
                JPAExpressions.selectOne()
                    .from(s)
                    .where(
                        v.specialities.contains(s),
                        s.id.eq(2)
                    )
                    .exists()
            )
            .fetch();
        for (Vet vet : vets) {
            System.out.println("vet = " + vet);
        }
    }

}
