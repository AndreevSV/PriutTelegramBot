package omg.group.priuttelegrambot.dto.addresses;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;

import java.util.List;

@SuperBuilder(toBuilder = true)
@Getter
public class AddressOwnerCatDto extends AddressDto{

    List<OwnerCatDto> ownersDtoList;
}
