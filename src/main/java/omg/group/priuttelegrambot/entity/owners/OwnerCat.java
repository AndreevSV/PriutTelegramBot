package omg.group.priuttelegrambot.entity.owners;

import jakarta.persistence.*;
import lombok.*;
import omg.group.priuttelegrambot.entity.addresses.AddressesOwnersCats;
import omg.group.priuttelegrambot.entity.pets.Cat;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "clients_cats")
public class OwnerCat extends Owner {
    @ManyToOne
    @JoinColumn(name = "address", referencedColumnName = "id")
    private AddressesOwnersCats address;

    @ManyToOne
    @JoinColumn(name = "volunteer_id", referencedColumnName = "id")
    private OwnerCat volunteer;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @ToString.Exclude
    private List<Cat> cats;


}