package omg.group.priuttelegrambot.dto.pets;

import lombok.Data;
import omg.group.priuttelegrambot.entity.pets.petsenum.PetType;
import omg.group.priuttelegrambot.entity.pets.petsenum.Sex;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class PetsDto {
        private Long id;
        private PetType animalType;
        private Sex sex;
        private String nickName;
        private Date birthday;
        private Boolean disabilities;
        private String description;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime dateOutcome;
        private String photoPath;
        private Long volunteerId;
        private Long ownerId;
        private Boolean firstProbation;
        private Boolean secondProbation;
        private LocalDate probationStarts;
        private LocalDate probationEnds;
        private Boolean passedFirstProbation;
        private Boolean passedSecondProbation;

}
