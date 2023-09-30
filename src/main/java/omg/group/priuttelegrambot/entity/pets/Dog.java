package omg.group.priuttelegrambot.entity.pets;

import jakarta.persistence.*;
import lombok.*;
import omg.group.priuttelegrambot.entity.flags.OwnersDogsFlags;
import omg.group.priuttelegrambot.entity.pets.petsenum.DogsBreed;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "dogs")
public class Dog extends Pet {

    @Column(name = "breed")
    @Enumerated(EnumType.ORDINAL)
    private DogsBreed breed;

    @OneToOne
    @JoinColumn(name = "id_volunteer", referencedColumnName = "id")
    private OwnerDog volunteer;

    @ManyToOne
    @JoinColumn(name = "clients_dogs_id", referencedColumnName = "id")
    private OwnerDog owner;

    @OneToMany(mappedBy = "dog", cascade = CascadeType.ALL)
    private List<OwnersDogsFlags> flags;

}
