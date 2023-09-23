package omg.group.priuttelegrambot.dto.owners;

import lombok.Data;
import lombok.EqualsAndHashCode;
import omg.group.priuttelegrambot.dto.pets.DogDto;
import omg.group.priuttelegrambot.entity.addresses.AddressesOwnersDogs;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class OwnerDogDto extends OwnerDto {
    private OwnerDogDto volunteerDto;
    private AddressesOwnersDogs address; //TODO: переделать на Dto
    private List<DogDto> dogsDto;

}
