package omg.group.priuttelegrambot.entity.owners;

import jakarta.persistence.*;
import lombok.*;
import omg.group.priuttelegrambot.entity.addresses.AddressesOwnersCats;
import omg.group.priuttelegrambot.entity.pets.Cat;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
@Entity
@Table(name = "clients_cats")
public class OwnerCat extends Owner {

    @ManyToOne
    @JoinColumn(name = "address", referencedColumnName = "id")
    private AddressesOwnersCats address;

    @ManyToOne
    @JoinColumn(name = "volunteer_id", referencedColumnName = "id")
    private OwnerCat volunteer;

    @OneToMany(mappedBy = "ownerCat")
    @ToString.Exclude
    private List<Cat> cats;


}