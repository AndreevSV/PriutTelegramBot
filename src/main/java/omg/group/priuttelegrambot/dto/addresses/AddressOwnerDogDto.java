package omg.group.priuttelegrambot.dto.addresses;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;

import java.util.List;

@SuperBuilder(toBuilder = true)
@Getter
public class AddressOwnerDogDto extends AddressDto {
    List<OwnerDogDto> ownersDtoList;
}
