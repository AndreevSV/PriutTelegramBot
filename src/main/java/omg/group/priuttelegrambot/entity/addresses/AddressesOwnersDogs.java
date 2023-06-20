package omg.group.priuttelegrambot.entity.addresses;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "addresses_clients_dogs")
public class AddressesOwnersDogs extends Addresses{

    @OneToMany(mappedBy = "address")
    private Collection<OwnerDog> clientsDogs;
}
