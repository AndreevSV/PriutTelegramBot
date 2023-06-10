package omg.group.priuttelegrambot.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import omg.group.priuttelegrambot.entity.animals.DogsBreed;

@EqualsAndHashCode(callSuper = true)
@Data
public class DogDto extends AnimalDto{
    DogsBreed breed;
}


