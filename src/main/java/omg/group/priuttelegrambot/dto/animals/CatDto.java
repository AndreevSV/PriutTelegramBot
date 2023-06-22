package omg.group.priuttelegrambot.dto.animals;

import lombok.Data;
import lombok.EqualsAndHashCode;
import omg.group.priuttelegrambot.entity.animals.enimalsenum.CatsBreed;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;

@EqualsAndHashCode(callSuper = true)
@Data
public class CatDto extends AnimalDto {
    private CatsBreed breed;
    private OwnerCat volunteer;
    private OwnerCat owner;


}
