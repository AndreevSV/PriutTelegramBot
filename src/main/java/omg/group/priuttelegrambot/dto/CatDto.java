package omg.group.priuttelegrambot.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import omg.group.priuttelegrambot.entity.animals.CatsBreed;

@EqualsAndHashCode(callSuper = true)
@Data
public class CatDto extends AnimalDto {
    private CatsBreed breed;
}
