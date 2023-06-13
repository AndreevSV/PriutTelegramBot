package omg.group.priuttelegrambot.entity.animals;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.entity.animals.enimalsenum.DogsBreed;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
@Entity
@Table(name = "dogs")
public class Dog extends Animal {

    @Column(name = "breed")
    private DogsBreed breed;

    @OneToOne
    @JoinColumn(name = "id_volunteer", referencedColumnName = "id")
    private OwnerDog volunteer;

    @ManyToOne
    @JoinColumn(name = "clients_dogs_id", referencedColumnName = "id")
    private OwnerDog ownerDog;

}
