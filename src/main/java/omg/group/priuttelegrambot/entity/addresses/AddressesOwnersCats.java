package omg.group.priuttelegrambot.entity.addresses;


import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "addresses_clients_cats")
public class AddressesOwnersCats extends Addresses {

    @OneToMany(mappedBy = "address")
    private List<OwnerCat> clientsCats;

}
