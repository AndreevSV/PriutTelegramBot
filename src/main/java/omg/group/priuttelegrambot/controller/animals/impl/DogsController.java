package omg.group.priuttelegrambot.controller.animals.impl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import omg.group.priuttelegrambot.dto.animals.DogDto;
import omg.group.priuttelegrambot.entity.animals.enimalsenum.DogsBreed;
import omg.group.priuttelegrambot.entity.animals.enimalsenum.Sex;
import omg.group.priuttelegrambot.service.DogsService;
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
    @Operation(
            summary = "Добавление собаки в базу данных приюта",
            description = "Собака добавляется путем заполнения полей json-файла"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Собака успешно добавлена в базу данных приюта",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = DogDto.class))
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
    public ResponseEntity<HttpStatus> add(
            @RequestBody DogDto dogDto) {
        return ResponseEntity.ok().body(dogsService.add(dogDto));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновление данных о собаке",
            description = "Данные о собаке обновляются путем введения ее идентификатора и изменения значения " +
                    "одного или нескольких полей в json-файле"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные о собаке успешно обновлены",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = DogDto.class))
                            )
                    }
            )
    })
    public ResponseEntity<HttpStatus> updateById(
            @PathVariable Long id,
            @RequestBody DogDto dogDto) {
        return ResponseEntity.ok().body(dogsService.updateById(id, dogDto));
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Поиск собаки по ее идентификатору",
            description = "Данные о собаке можно получить путем ввода ее идентификатора - целого числа, большего либо равного 0"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные о собаке с заданным идентификатором успешно найдены",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = DogDto.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Собака с таким id отсутствует в базе данных приюта"

            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны"
            )
    })
    public ResponseEntity<List<DogDto>> findById(
            @PathVariable Long id) {
        return ResponseEntity.ok().body(dogsService.findById(id));
    }


    @GetMapping
    @Operation(
            summary = "Поиск собаки по одному или нескольким параметрам",
            description = "Данные о собаке можно получить путем ввода ее идентификатора - целого числа, большего либо равного 0, и/или " +
                    "пола собаки, и/или ее клички, и/или ее породы"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные о собаке с заданными параметрами успешно найдены",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = DogDto.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Собака с таким(и) параметром (параметрами) не найдена в базе данных приюта"

            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны"
            )
    })
    public ResponseEntity<List<DogDto>> findByIdSexNicknameBreed(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Sex sex,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) DogsBreed breed
    ) {

        if (id != null) {
            return ResponseEntity.ok().body(dogsService.findById(id));
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
            throw new NullPointerException("Кошка с такими параметрами не найдена");
        }
    }

    @GetMapping("/findbybirthday")
    @Operation(
            summary = "Поиск собаки по дате рождения из заданного промежутка времени",
            description = "Данные о собаке можно получить путем ввода двух обязательных параметров - начальной даты и " +
                    "конечной даты ее предполагаемого дня рождения"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные о собаке, рожденной в заданном промежутке времени, найдены",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = DogDto.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "В базе данных приюта отсутствует собака, рожденная в заданном промежутке времени"

            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны"
            )
    })
    public ResponseEntity<List<DogDto>> findByBirthdayBetweenStartDateAndEndDate(
            @RequestParam(required = true) Date startDate,
            @RequestParam(required = true) Date endDate) {
        if (startDate != null && endDate != null) {
            return ResponseEntity.ok().body(
                    dogsService.findByBirthdayBetweenDates(startDate, endDate));
        } else {
            throw new NullPointerException("Собаки, рожденной в данном промежутке времени, не найдено");
        }
    }

    @GetMapping("/all")
    @Operation(
            summary = "Получение списка всех собак приюта",
            description = "Для получения списка всех собак ничего не надо вводить"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список всех собак приюта получен",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = DogDto.class))
                            )
                    }
            )
    })
    public ResponseEntity<List<DogDto>> getAll() {
        return ResponseEntity.ok(dogsService.getAll());
    }


    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление собаки из базы данных приюта",
            description = "Собака удаляется из базы данных приюта путем введения ее идентификатора"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Собака успешно удалена из базы данных приюта",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = DogDto.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "В базе данных приюта отсутствует собака с таким id"

            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны"
            )
    })
    public ResponseEntity<HttpStatus> deleteById(
            @PathVariable Long id) {
        return ResponseEntity.ok().body(dogsService.deleteById(id));
    }

}
