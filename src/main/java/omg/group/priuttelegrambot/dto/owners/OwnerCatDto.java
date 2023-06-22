package omg.group.priuttelegrambot.dto.owners;

import lombok.Data;
import lombok.EqualsAndHashCode;
import omg.group.priuttelegrambot.entity.addresses.AddressesOwnersCats;

@EqualsAndHashCode(callSuper = true)
@Data
public class OwnerCatDto extends OwnerDto {

    private Long catId; // !!!!!!!!!!!!!!!!!!

    private AddressesOwnersCats address;

    private Long chatId;
}
