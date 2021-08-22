package com.github.sooogle.jpademo.entitysub;

import com.github.sooogle.jpademo.entity.Owner;

public class OwnerAndCountDTO {

    private final Owner owner;

    private final Long count;

    public OwnerAndCountDTO(Owner owner, Long count) {
        this.owner = owner;
        this.count = count;
    }

    public Owner getOwner() {
        return owner;
    }

    public Long getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "OwnerAndCountDTO{" +
            "owner=" + owner +
            ", count=" + count +
            '}';
    }

}
