package omg.group.priuttelegrambot.controller;

import omg.group.priuttelegrambot.entity.animals.Animal;
import omg.group.priuttelegrambot.entity.animals.AnimalType;
import omg.group.priuttelegrambot.entity.animals.Cat;
import omg.group.priuttelegrambot.entity.animals.CatsBreed;
import omg.group.priuttelegrambot.service.AnimalService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/animal")
public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @PostMapping
    public ResponseEntity<HttpStatusCode> addCat(@RequestParam AnimalType animalType,
                                                 @RequestParam String nickName
                                                 ) {
        Cat cat = new Cat();
        cat.setAnimalType(animalType);
        cat.setNickName(nickName);
        cat.setCreatedAt(LocalDateTime.now());

        animalService.save(cat);
        return ResponseEntity.ok().build();
    }

}
