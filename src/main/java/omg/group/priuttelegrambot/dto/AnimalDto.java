package omg.group.priuttelegrambot.dto;

import lombok.Data;
import omg.group.priuttelegrambot.entity.animals.AnimalType;
import omg.group.priuttelegrambot.entity.animals.CatsBreed;
import omg.group.priuttelegrambot.entity.owners.Owner;

import java.time.LocalDateTime;

@Data
public class AnimalDto {
        private Long id;
        private AnimalType animalType;
        private String nickName;
        private LocalDateTime birthday;
        private CatsBreed breed;
        private Boolean disabilities;
        private String description;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime dateOutcome;
        private String photoPath;
        Owner volunteer;
        Owner owner;
}
