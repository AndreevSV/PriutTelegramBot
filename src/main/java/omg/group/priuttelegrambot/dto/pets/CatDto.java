package omg.group.priuttelegrambot.dto.pets;

import lombok.Data;
import lombok.EqualsAndHashCode;
import omg.group.priuttelegrambot.entity.pets.petsenum.CatsBreed;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;

@EqualsAndHashCode(callSuper = true)
@Data
public class CatDto extends PetsDto {
    private CatsBreed breed;
    private OwnerCat volunteer;
    private OwnerCat owner;
}
