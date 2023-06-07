package omg.group.priuttelegrambot.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.entity.animals.AnimalType;
import omg.group.priuttelegrambot.entity.animals.CatsBreed;
import omg.group.priuttelegrambot.entity.clients.ClientsCats;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
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
    private ClientsCats volunteer;
    private ClientsCats clientCat;
}
