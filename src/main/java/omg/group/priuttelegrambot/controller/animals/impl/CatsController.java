package omg.group.priuttelegrambot.controller.animals.impl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import omg.group.priuttelegrambot.dto.animals.CatDto;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.entity.animals.enimalsenum.Sex;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.service.CatsService;
import omg.group.priuttelegrambot.service.impl.CatsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/animals/cats")
@Tag(name = "Контроллер для работы с кошками", description = "CRUD-операции для работы с кошками")
public class CatsController {

    private final CatsService catsService;

    public CatsController(CatsServiceImpl catsService) {
        this.catsService = catsService;
    }


    @PostMapping
    public ResponseEntity<HttpStatus> addCat(@RequestBody CatDto catDto) {
        return ResponseEntity.ok().body(catsService.add(catDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateById(@PathVariable Long id, @RequestBody CatDto catDto) {
        return ResponseEntity.ok().body(catsService.updateById(id, catDto));
    }


    @GetMapping("/{id}")
    public ResponseEntity<List<CatDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(catsService.findById(id));
    }







    List<CatDto> findBySex(Sex sex);

    List<CatDto> findByNickname(String nickname);

    List<CatDto> findByBreed(String breed);

    List<CatDto> findByBirthdayBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    List<CatDto> getAll();

    HttpStatus deleteById(Long id);






    @GetMapping
    public ResponseEntity<List<OwnerCatDto>> findByIdUsernameSurnameTelephone(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String telephone) {

        if (id != null) {
            return ResponseEntity.ok().body(ownersCatsService.findById(id));
        }
        if (username != null && !username.isBlank()) {
            return ResponseEntity.ok(ownersCatsService.findByUsername(username));
        }
        if (surname != null && !surname.isBlank()) {
            return ResponseEntity.ok(ownersCatsService.findBySurname(surname));
        }
        if (telephone != null && !telephone.isBlank()) {
            return ResponseEntity.ok(ownersCatsService.findByTelephone(telephone));
        } else {
            throw new NullPointerException("Клиент с такими параметрами не найден");
        }
    }

    @RequestMapping("/all")
    public ResponseEntity<List<OwnerCatDto>> getAll() {
        return ResponseEntity.ok(ownersCatsService.getAll());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        return ResponseEntity.ok().body(ownersCatsService.deleteById(id));
    }




}