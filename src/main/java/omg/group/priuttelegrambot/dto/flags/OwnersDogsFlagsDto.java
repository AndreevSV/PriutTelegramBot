package omg.group.priuttelegrambot.dto.flags;

import lombok.Data;
import lombok.EqualsAndHashCode;
import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;
import omg.group.priuttelegrambot.dto.pets.DogDto;

@Data
@EqualsAndHashCode(callSuper = true)
public class OwnersDogsFlagsDto extends FlagsDto {
    private OwnerDogDto ownerDto;
    private OwnerDogDto volunteerDto;
    private DogDto dogDto;
}
