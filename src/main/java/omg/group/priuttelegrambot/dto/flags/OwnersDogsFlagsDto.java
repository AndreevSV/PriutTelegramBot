package omg.group.priuttelegrambot.dto.flags;

import lombok.*;
import lombok.experimental.SuperBuilder;
import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;
import omg.group.priuttelegrambot.dto.pets.DogDto;

@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class OwnersDogsFlagsDto extends FlagsDto {
    private OwnerDogDto ownerDto;
    private OwnerDogDto volunteerDto;
    private DogDto dogDto;
}
