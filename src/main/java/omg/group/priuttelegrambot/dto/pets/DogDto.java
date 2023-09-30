package omg.group.priuttelegrambot.dto.pets;

import lombok.Data;
import lombok.EqualsAndHashCode;
import omg.group.priuttelegrambot.entity.pets.petsenum.DogsBreed;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;

@EqualsAndHashCode(callSuper = true)
@Data
public class DogDto extends PetsDto {
    private DogsBreed breed;
    private OwnerDog volunteer;
    private OwnerDog owner;
}


