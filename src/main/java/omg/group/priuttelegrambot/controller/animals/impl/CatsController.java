package omg.group.priuttelegrambot.controller.animals.impl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import omg.group.priuttelegrambot.dto.animals.CatDto;
import omg.group.priuttelegrambot.entity.animals.Cat;
import omg.group.priuttelegrambot.entity.animals.enimalsenum.CatsBreed;
import omg.group.priuttelegrambot.entity.animals.enimalsenum.Sex;
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
    @Operation(
            summary = "Добавление кошки в базу данных приюта",
            description = "Кошка добавляется путем заполнения полей json-файла"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Кошка успешно добавлена в базу данных приюта",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CatDto.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат"

            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны"
            )
    })
    public ResponseEntity<HttpStatus> add(@RequestBody CatDto catDto) {
        return ResponseEntity.ok().body(catsService.add(catDto));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновление данных кошки",
            description = "Данные кошки обновляются путем введения ее идентификатора и изменения значения " +
                    "одного или нескольких полей в json-файле"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные кошки успешно обновлены",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CatDto.class))
                            )
                    }
            )
    })
    public ResponseEntity<HttpStatus> updateById(
            @PathVariable Long id,
            @RequestBody CatDto catDto) {
        return ResponseEntity.ok().body(catsService.updateById(id, catDto));
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Поиск кошки по ее идентификатору",
            description = "Данные о кошке можно получить путем ввода ее идентификатора - целого числа, большего либо равного 0"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные о кошке с заданным идентификатором успешно найдены",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CatDto.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Кошка с таким id отсутствует в базе приюта"

            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны"
            )
    })
    public ResponseEntity<List<CatDto>> findById(
            @PathVariable Long id) {
        return ResponseEntity.ok().body(catsService.findById(id));
    }

    @GetMapping
    @Operation(
            summary = "Поиск кошки по одному или нескольким параметрам",
            description = "Данные о кошке можно получить путем ввода ее идентификатора - целого числа, большего либо равного 0, и/или " +
                    "пола кошки, и/или ее клички, и/или ее породы"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные о кошке с заданными параметрами успешно найдены",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CatDto.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Кошка с таким(и) параметром (параметрами) не найдена в базе данных приюта"

            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны"
            )
    })
    public ResponseEntity<List<CatDto>> findByIdSexNicknameBreed(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Sex sex,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) CatsBreed breed
    ) {

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
            throw new NullPointerException("Кошка с такими параметрами не найдена");
        }
    }

    @GetMapping("/findbybirthday")
    @Operation(
            summary = "Поиск кошки по дате рождения из заданного промежутка времени",
            description = "Данные о кошке можно получить путем ввода двух обязательных параметров - начальной даты и " +
                    "конечной даты ее предполагаемого дня рождения"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные о кошке, рожденной в заданном промежутке времени, найдены",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CatDto.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "В базе данных приюта отсутствует кошка, рожденная в заданном промежутке времени"

            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны"
            )
    })
    public ResponseEntity<List<CatDto>> findByBirthdayBetweenStartDateAndEndDate(
            @RequestParam(required = true) Date startDate,
            @RequestParam(required = true) Date endDate) {
        if (startDate != null && endDate != null) {
            return ResponseEntity.ok().body(
                    catsService.findByBirthdayBetweenDates(startDate, endDate));
        } else {
            throw new NullPointerException("Кошки, рожденной в данном промежутке времени, не найдено");
        }
    }

    @GetMapping("/all")
    @Operation(
            summary = "Получение списка всех кошек",
            description = "Для получения списка всех кошек ничего не надо вводить"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список всех кошек приюта получен",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CatDto.class))
                            )
                    }
            )
    })
    public ResponseEntity<List<CatDto>> getAll() {
        return ResponseEntity.ok(catsService.getAll());
    }


    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление кошки из базы данных приюта",
            description = "Кошка удаляется из базы данных приюта путем введения ее идентификатора"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Кошка успешно удалена из базы данных приюта",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CatDto.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "В базе данных приюта отсутствует кошка с таким id"

            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны"
            )
    })
    public ResponseEntity<HttpStatus> deleteById(
            @PathVariable Long id) {
        return ResponseEntity.ok().body(catsService.deleteById(id));
    }

}