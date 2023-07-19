package omg.group.priuttelegrambot.controller.pets.impl;

import io.swagger.v3.oas.annotations.tags.Tag;
import omg.group.priuttelegrambot.dto.pets.CatDto;
import omg.group.priuttelegrambot.entity.pets.petsenum.CatsBreed;
import omg.group.priuttelegrambot.entity.pets.petsenum.Sex;
import omg.group.priuttelegrambot.service.CatsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/animals/cats")
@Tag(name = "Контроллер для работы с кошками", description = "CRUD-операции для работы с кошками")
public class CatsController {

    private final CatsService catsService;

    public CatsController(CatsService catsService) {
        this.catsService = catsService;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> add(@RequestBody CatDto catDto) {
        catsService.add(catDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateById(@PathVariable Long id, @RequestBody CatDto catDto) {
        catsService.updateById(id, catDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<CatDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(catsService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<CatDto>> findByIdSexNicknameBreed(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Sex sex,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) CatsBreed breed) {
        if (id != null) {
            return ResponseEntity.ok().body(catsService.findById(id));
        }
        if (sex != null) {
            return ResponseEntity.ok(catsService.findBySex(sex));
        }
        if (nickname != null && !nickname.isBlank()) {
            return ResponseEntity.ok(catsService.findByNickname(nickname));
        }
        if (breed != null) {
            return ResponseEntity.ok(catsService.findByBreed(breed));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/findbybirthday")
    public ResponseEntity<List<CatDto>> findByBirthdayBetweenStartDateAndEndDate(@RequestParam Date startDate, @RequestParam Date endDate) {
        if (startDate != null && endDate != null) {
            return ResponseEntity.ok().body(catsService.findByBirthdayBetweenDates(startDate, endDate));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping("/all")
    public ResponseEntity<List<CatDto>> getAll() {
        return ResponseEntity.ok(catsService.getAll());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        catsService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}