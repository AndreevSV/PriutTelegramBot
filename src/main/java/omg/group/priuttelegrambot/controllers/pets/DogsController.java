package omg.group.priuttelegrambot.controllers.pets;

import io.swagger.v3.oas.annotations.tags.Tag;
import omg.group.priuttelegrambot.dto.pets.DogDto;
import omg.group.priuttelegrambot.entity.pets.petsenum.DogsBreed;
import omg.group.priuttelegrambot.entity.pets.petsenum.Sex;
import omg.group.priuttelegrambot.service.pets.DogsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/animals/dogs")
@Tag(name = "Контроллер для работы с собаками", description = "CRUD-операции для работы с собаками")
public class DogsController {

    private final DogsService dogsService;

    public DogsController(DogsService dogsService) {
        this.dogsService = dogsService;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> add(@RequestBody DogDto dogDto) {
        dogsService.add(dogDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateById(@PathVariable Long id, @RequestBody DogDto dogDto) {
        dogsService.updateById(id, dogDto);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<DogDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(dogsService.findById(id));
    }


    @GetMapping
    public ResponseEntity<List<DogDto>> findByIdSexNicknameBreed(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Sex sex,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) DogsBreed breed
    ) {

        if (id != null) {
            return ResponseEntity.ok().body(dogsService.findByIdContaining(id));
        }
        if (sex != null) {
            return ResponseEntity.ok(dogsService.findBySex(sex));
        }
        if (nickname != null && !nickname.isBlank()) {
            return ResponseEntity.ok(dogsService.findByNickname(nickname));
        }
        if (breed != null) {
            return ResponseEntity.ok(dogsService.findByBreed(breed));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/findbybirthday")
    public ResponseEntity<List<DogDto>> findByBirthdayBetweenStartDateAndEndDate(@RequestParam Date startDate, @RequestParam Date endDate) {
        if (startDate != null && endDate != null) {
            return ResponseEntity.ok().body(dogsService.findByBirthdayBetweenDates(startDate, endDate));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping("/all")
    public ResponseEntity<List<DogDto>> getAll() {
        return ResponseEntity.ok(dogsService.getAll());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        dogsService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
