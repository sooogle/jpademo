package com.github.sooogle.jpademo.querydsl;

import com.github.sooogle.jpademo.entity.Owner;
import com.github.sooogle.jpademo.entity.Pet;
import com.github.sooogle.jpademo.entity.QOwner;
import com.github.sooogle.jpademo.entity.QPet;
import com.github.sooogle.jpademo.entity.QVisit;
import com.github.sooogle.jpademo.entity.Visit;
import com.github.sooogle.jpademo.entitysub.PetNoRelation;
import com.github.sooogle.jpademo.entitysub.QPetNoRelation;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ManyToOneTest {

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("ManyToOneのJOIN FETCH")
    void testDistinct() {
        JPAQuery<Pet> query = new JPAQuery<>(em);
        QPet p = QPet.pet;
        // SELECT p
        // FROM Pet p
        // INNER JOIN FETCH p.owner
        List<Pet> pets = query.select(p)
            .from(p)
            .innerJoin(p.owner).fetchJoin()
            .fetch();
        for (Pet pet : pets) {
            System.out.println("pet = " + pet + ", owner = " + pet.getOwner());
        }
    }

    @Test
    @DisplayName("ネストしたJOIN FETCH")
    void testNestedJoinFetch() {
        JPAQuery<Pet> query = new JPAQuery<>(em);
        QVisit v = QVisit.visit;
        QPet p = QPet.pet;
        // SELECT v
        // FROM Visit v
        // INNER JOIN FETCH v.pet p
        // INNER JOIN FETCH p.owner
        List<Visit> visits = query.select(v)
            .from(v)
            .innerJoin(v.pet, p).fetchJoin()
            .innerJoin(p.owner).fetchJoin()
            .fetch();
        for (Visit visit : visits) {
            System.out.println(
                "visit = " + visit + ", pet = " + visit.getPet() + ", owner = " + visit.getPet().getOwner());
        }
    }

    @Test
    @DisplayName("ManyToOneのJOIN")
    void testJoin() {
        JPAQuery<Tuple> query = new JPAQuery<>(em);
        QPet p = QPet.pet;
        // SELECT p
        // FROM Pet p
        // INNER JOIN p.owner
        // WHERE p.owner.firstName = :firstName
        List<Pet> pets = query.select(p)
            .from(p)
            .innerJoin(p.owner)
            .where(p.owner.firstName.eq("George"))
            .fetch();
        for (Pet pet : pets) {
            System.out.println("pet = " + pet);
        }
    }

    @Test
    @DisplayName("ManyToOneのJOIN ON")
    void testJoinOn() {
        JPAQuery<Tuple> query = new JPAQuery<>(em);
        QPetNoRelation p = QPetNoRelation.petNoRelation;
        QOwner o = QOwner.owner;
        // SELECT p, o
        // FROM PetNoRelation p
        // INNER JOIN Owner o ON o.id = p.ownerId
        List<Tuple> tuples = query.select(p, o)
            .from(p)
            .innerJoin(o).on(o.id.eq(p.ownerId))
            .fetch();
        for (Tuple tuple : tuples) {
            System.out.println("pet = " + tuple.get(0, PetNoRelation.class) + ", owner = " + tuple.get(1, Owner.class));
        }
    }
}
