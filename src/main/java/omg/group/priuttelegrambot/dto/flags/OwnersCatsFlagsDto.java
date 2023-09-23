package omg.group.priuttelegrambot.dto.flags;

import lombok.*;
import lombok.experimental.SuperBuilder;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.dto.pets.CatDto;

@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class OwnersCatsFlagsDto extends FlagsDto {
    private OwnerCatDto ownerDto;
    private OwnerCatDto volunteerDto;
    private CatDto catDto;
}
