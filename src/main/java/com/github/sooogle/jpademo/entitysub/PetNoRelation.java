package com.github.sooogle.jpademo.entitysub;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pets")
public class PetNoRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "type_id")
    private Integer typeId;

    @Column(name = "owner_id")
    private Integer ownerId;

    @Override
    public String toString() {
        return "Pet{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }

}
