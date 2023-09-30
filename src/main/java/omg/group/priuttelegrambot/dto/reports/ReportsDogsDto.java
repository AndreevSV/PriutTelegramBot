package omg.group.priuttelegrambot.dto.reports;

import lombok.Data;
import lombok.EqualsAndHashCode;
import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;
import omg.group.priuttelegrambot.dto.pets.DogDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReportsDogsDto extends ReportsDto {
    private OwnerDogDto ownerDto;
    private DogDto petDto;
}
