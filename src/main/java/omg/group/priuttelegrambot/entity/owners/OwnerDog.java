package omg.group.priuttelegrambot.entity.owners;

import jakarta.persistence.*;
import lombok.*;
import omg.group.priuttelegrambot.entity.addresses.AddressesOwnersDogs;
import omg.group.priuttelegrambot.entity.pets.Dog;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "clients_dogs")
public class OwnerDog extends Owner {

    @ManyToOne
    @JoinColumn(name = "address", referencedColumnName = "id")
    private AddressesOwnersDogs address;

    @ManyToOne
    @JoinColumn(name = "volunteer_id", referencedColumnName = "id")
    private OwnerDog volunteer;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
//    @ToString.Exclude
    private List<Dog> dogs;



}
