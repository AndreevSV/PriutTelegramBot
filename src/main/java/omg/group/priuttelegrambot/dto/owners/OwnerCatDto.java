package omg.group.priuttelegrambot.dto.owners;

import lombok.Data;
import lombok.EqualsAndHashCode;
import omg.group.priuttelegrambot.entity.addresses.AddressesOwnersCats;
import omg.group.priuttelegrambot.entity.pets.Cat;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class OwnerCatDto extends OwnerDto {

    private OwnerCat volunteer;

    private AddressesOwnersCats address;

    private List<Cat> cats;

}
