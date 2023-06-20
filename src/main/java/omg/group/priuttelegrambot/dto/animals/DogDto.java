package omg.group.priuttelegrambot.dto.animals;

import lombok.Data;
import lombok.EqualsAndHashCode;
import omg.group.priuttelegrambot.entity.animals.enimalsenum.DogsBreed;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;

@EqualsAndHashCode(callSuper = true)
@Data
public class DogDto extends AnimalDto {
    private DogsBreed breed;
    private OwnerDog volunteer;
    private OwnerDog owner;
}


