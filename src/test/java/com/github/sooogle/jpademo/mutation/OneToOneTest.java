package com.github.sooogle.jpademo.mutation;

import com.github.sooogle.jpademo.entity.Owner;
import com.github.sooogle.jpademo.entity.Pet;
import com.github.sooogle.jpademo.entity.PetDetail;
import com.github.sooogle.jpademo.entity.Type;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OneToOneTest {

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("子側（外部キーを持っている側）はLazyが効く")
    void testLazyWorks() {
        List<PetDetail> details = em.createQuery("SELECT pd FROM PetDetail pd", PetDetail.class).getResultList();
        for (PetDetail detail : details) {
            System.out.println("detail = " + detail);
        }
    }

    @Test
    @DisplayName("OneToOneの関連に@MapsIdを付与すると、関連エンティティとPKを共有する")
    @Transactional
    void testMapsId() {
        var pet = new Pet();
        pet.setName("Tama");
        pet.setType(em.getReference(Type.class, 1));
        pet.setOwner(em.getReference(Owner.class, 1));
        var petDetail = new PetDetail();
        petDetail.setPet(pet);
        petDetail.setBirthDate(LocalDate.now().minusYears(3));
        em.persist(petDetail);
        em.flush();
        assertThat(pet.getId()).isEqualTo(petDetail.getId());
    }

}
