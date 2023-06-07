package omg.group.priuttelegrambot.controller;

import omg.group.priuttelegrambot.dto.AnimalDto;
import omg.group.priuttelegrambot.entity.animals.AnimalType;
import omg.group.priuttelegrambot.entity.animals.DogsBreed;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/animal")
public class AnimalsController {

    @PostMapping
    public ResponseEntity<HttpStatusCode> addAnimal(@RequestBody AnimalDto animalDto)
    {
        return ResponseEntity.ok().body(HttpStatusCode.valueOf(200));
    }

}
