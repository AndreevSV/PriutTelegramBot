package omg.group.priuttelegrambot.dto.owners;

import lombok.Data;
import lombok.EqualsAndHashCode;
import omg.group.priuttelegrambot.dto.pets.CatDto;
import omg.group.priuttelegrambot.entity.addresses.AddressesOwnersCats;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class OwnerCatDto extends OwnerDto {
    private OwnerCatDto volunteerDto;
    private AddressesOwnersCats address; // TODO: Переделать на dto
    private List<CatDto> catsDto;
}

