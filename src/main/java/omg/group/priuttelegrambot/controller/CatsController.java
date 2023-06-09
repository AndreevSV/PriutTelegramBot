package omg.group.priuttelegrambot.controller;

import omg.group.priuttelegrambot.dto.AnimalDto;
import omg.group.priuttelegrambot.dto.CatDto;
import omg.group.priuttelegrambot.service.CatsService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cats")
public class CatsController {

    private final CatsService catsService;

    public CatsController(CatsService catsService) {
        this.catsService = catsService;
    }

    @PostMapping
    public ResponseEntity<HttpStatusCode> addCat(@RequestBody CatDto catDto) {
        return ResponseEntity.ok().body(catsService.save(catDto));
    }

}