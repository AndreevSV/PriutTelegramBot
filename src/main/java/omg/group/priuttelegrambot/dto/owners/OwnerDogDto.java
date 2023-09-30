package omg.group.priuttelegrambot.dto.owners;

import lombok.Data;
import lombok.EqualsAndHashCode;
import omg.group.priuttelegrambot.dto.pets.DogDto;
import omg.group.priuttelegrambot.entity.addresses.AddressOwnerDog;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class OwnerDogDto extends OwnerDto {
    private OwnerDogDto volunteerDto;
    private AddressOwnerDog address;
    private List<DogDto> dogsDto;

}
