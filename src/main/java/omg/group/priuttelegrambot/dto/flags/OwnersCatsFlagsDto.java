package omg.group.priuttelegrambot.dto.flags;

import lombok.Data;
import lombok.EqualsAndHashCode;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.dto.pets.CatDto;

@Data
@EqualsAndHashCode(callSuper = true)
public class OwnersCatsFlagsDto extends FlagsDto {
    private OwnerCatDto ownerDto;
    private OwnerCatDto volunteerDto;
    private CatDto catDto;
}
