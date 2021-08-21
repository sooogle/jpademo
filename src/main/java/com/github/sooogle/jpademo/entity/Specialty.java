package com.github.sooogle.jpademo.entity;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "specialties")
public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToMany
    @JoinTable(name = "vet_specialties",
        joinColumns = @JoinColumn(name = "specialty_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "vet_id", referencedColumnName = "id")
    )
    private List<Vet> vets;

    @Override
    public String toString() {
        return "Specialty{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }

}
