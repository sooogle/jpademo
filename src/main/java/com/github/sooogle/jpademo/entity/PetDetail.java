package com.github.sooogle.jpademo.entity;

import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pet_details")
public class PetDetail {

    @Id
    @Column(name = "pet_id")
    private Integer id;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @Override
    public String toString() {
        return "PetDetail{" +
            "id=" + id +
            ", birthDate=" + birthDate +
            '}';
    }

}
