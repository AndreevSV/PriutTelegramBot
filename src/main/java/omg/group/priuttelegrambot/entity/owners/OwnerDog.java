package omg.group.priuttelegrambot.entity.owners;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.entity.animals.Dog;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
@Entity
@Table(name = "clients_dogs")
public class OwnerDog extends Owner {

    @Column(name = "dog_id")
    private Long dogId;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "id")
    private OwnerDog volunteer;

    @OneToMany(mappedBy = "ownerDog")
    private Collection<Dog> dogs;


}
