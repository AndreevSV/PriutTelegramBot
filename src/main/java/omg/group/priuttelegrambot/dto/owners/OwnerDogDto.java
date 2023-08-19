package omg.group.priuttelegrambot.dto.owners;

import lombok.Data;
import lombok.EqualsAndHashCode;
import omg.group.priuttelegrambot.entity.addresses.AddressesOwnersDogs;
import omg.group.priuttelegrambot.entity.pets.Dog;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class OwnerDogDto extends OwnerDto {
    private OwnerDog volunteer;
    private AddressesOwnersDogs address;
    private List<Dog> dogs;

}
