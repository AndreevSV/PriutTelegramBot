package omg.group.priuttelegrambot.dto.animals;

import lombok.Data;
import omg.group.priuttelegrambot.entity.animals.enimalsenum.AnimalType;
import omg.group.priuttelegrambot.entity.animals.enimalsenum.Sex;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class AnimalDto {
        private Long id;
        private AnimalType animalType;
        private Sex sex;
        private String nickName;
        private Date birthday;
        private Boolean disabilities;
        private String description;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime dateOutcome;
        private String photoPath;

}
