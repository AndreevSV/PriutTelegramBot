package omg.group.priuttelegrambot.controller.owners.impl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.service.owners.OwnersDogsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owners/ownersdogs")
@Tag(name = "База клиентов приюта собак", description = "CRUD-операции для работы с данными клиентов приюта собак")
public class OwnersDogsController {

    private final OwnersDogsService ownersDogsService;

    @PostMapping
    @Operation(
            summary = "Добавление клиента в клиентскую базу",
            description = "Клиент добавляется путем заполнения полей json-файла"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Клиент успешно добавлен в клиентскую базу",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = OwnerDog.class))
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
    public ResponseEntity<HttpStatus> add(@RequestBody OwnerDogDto ownerDto) {
        ownersDogsService.add(ownerDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновление данных клиента",
            description = "Данные клиента обновляются путем введения его идентификатора и изменения значения " +
                    "одного или нескольких полей в json-файле"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные клиента успешно обновлены",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = OwnerDog.class))
                            )
                    }
            )
    })
    public ResponseEntity<HttpStatus> updateById(@PathVariable Long id, @RequestBody OwnerDogDto ownerDto) {
        ownersDogsService.updateById(id, ownerDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(
            summary = "Получение данных о клиенте приюта кошек",
            description =
                    "Информацию о клиенте можно получить путем ввода одного или нескольких " +
                            "параметров - фамилии, телефона, логина в Telegram и/или идентификатора"
    )
    @Parameters(value = {
            @Parameter(
                    name = "surname", example = "Иванов"
            ),
            @Parameter(
                    name = "telephone", example = "+71231234567"
            ),
            @Parameter(
                    name = "username", example = "leleka"
            ),
            @Parameter(
                    name = "id", example = "целое число, большее либо равное 0, например, 5"
            ),
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Информация о клиенте по заданным параметрам получена",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = OwnerDog.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат"

            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Клиент с такими данными отсутствует в клиентской базе приюта"

            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны"

            )
    })
    public ResponseEntity<List<OwnerDogDto>> findByIdUsernameSurnameTelephone(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String telephone) {

        if (id != null) {
            return ResponseEntity.ok().body(ownersDogsService.findById(id));
        }
        if (username != null && !username.isBlank()) {
            return ResponseEntity.ok(ownersDogsService.findByUsername(username));
        }
        if (surname != null && !surname.isBlank()) {
            return ResponseEntity.ok(ownersDogsService.findBySurname(surname));
        }
        if (telephone != null && !telephone.isBlank()) {
            return ResponseEntity.ok(ownersDogsService.findByTelephone(telephone));
        } else {
            throw new NullPointerException("Клиент с такими параметрами не найден");
        }
    }

    @RequestMapping("/all")
    public ResponseEntity<List<OwnerDogDto>> getAll() {
        return ResponseEntity.ok(ownersDogsService.getAll());
    }


    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление клиента из клиентской базы",
            description = "Клиент удаляется из базы клиентов путем введения его идентификатора"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Клиент успешно удален из клиентской базы",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = OwnerDog.class))
                            )
                    }
            )
    })
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        ownersDogsService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/volunteer/{id}")
    @Operation(
            summary = "Установление сотруднику статуса Волонтера и назначение ему курируемых животных",
            description = "Сотрудник ищется по id и ему передается список id курируемых животных json-файла"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Статус на статус Волонтер успешно обновлен и ему назначены животные",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = OwnerDog.class))
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
    public ResponseEntity<HttpStatus> setVolunteer(@PathVariable Long id, @RequestBody List<Long> dogsIds) {
        ownersDogsService.setVolunteer(id, dogsIds);
        return ResponseEntity.ok().build();
    }

//    @Operation(
//            summary = "Установление животному хозяина и назначение хозяину испытательный срок",
//            description = "Сотрудник ищется по id и ему передается список id курируемых животных json-файла"
//    )
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Статус на статус Волонтер успешно обновлен и ему назначены животные",
//                    content = {
//                            @Content(
//                                    mediaType = "application/json",
//                                    array = @ArraySchema(schema = @Schema(implementation = OwnerCat.class))
//                            )
//                    }
//            ),
//            @ApiResponse(
//                    responseCode = "400",
//                    description = "Параметры запроса отсутствуют или имеют некорректный формат"
//
//            ),
//            @ApiResponse(
//                    responseCode = "500",
//                    description = "Произошла ошибка, не зависящая от вызывающей стороны"
//            )
//    })
//    @PutMapping("/owner/{id}")
//    public ResponseEntity<HttpStatus> setAnimalToOwnerAndSetStartOfProbationPeriod(@PathVariable Long id, @RequestBody List<Long> catsIds) {
//        ownersCatsService.setVolunteer(id, catsIds);
//        return ResponseEntity.ok().build();
//    }

}


