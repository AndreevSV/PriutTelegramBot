package omg.group.priuttelegrambot.controller.animals.impl;

import omg.group.priuttelegrambot.dto.animals.CatDto;
import omg.group.priuttelegrambot.service.CatsService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/animals/cats")
public class CatsController {

    private final CatsService catsService;

    public CatsController(CatsService catsService) {
        this.catsService = catsService;
    }

    @PostMapping
    public ResponseEntity<HttpStatusCode> addCat(@RequestBody CatDto catDto) {
        return ResponseEntity.ok().body(catsService.save(catDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<CatDto>> findCatById(@PathVariable Long id) {
        return ResponseEntity.ok().body(catsService.findById(id));
    }


}