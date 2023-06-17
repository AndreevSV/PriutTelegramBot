package omg.group.priuttelegrambot.dto.owners;

import lombok.Data;
import lombok.EqualsAndHashCode;
import omg.group.priuttelegrambot.entity.addresses.AddressesOwnersDogs;

@EqualsAndHashCode(callSuper = true)
@Data
public class OwnerDogDto extends OwnerDto {

    private Long dogId; // !!!!!!!!!!!!!!!!!!

    private AddressesOwnersDogs address;
}
