package omg.group.priuttelegrambot.controllers.owners;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.service.owners.OwnersCatsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owners/ownerscats")
@Tag(name = "База клиентов приюта кошек", description = "CRUD-операции для работы с данными клиентов приюта кошек")
public class OwnersCatsController {

    private final OwnersCatsService ownersCatsService;

    @PostMapping
    @Operation(summary = "Добавление клиента в клиентскую базу", description = "Клиент добавляется путем заполнения полей json-файла")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Клиент успешно добавлен в клиентскую базу", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OwnerCat.class))}),
            @ApiResponse(responseCode = "400", description = "Параметры запроса отсутствуют или имеют некорректный формат"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка, не зависящая от вызывающей стороны")})
    public ResponseEntity<HttpStatus> add(@RequestBody OwnerCatDto ownerCatDto) {
        ownersCatsService.add(ownerCatDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновление данных клиента", description = "Данные клиента обновляются путем введения его идентификатора и изменения значения одного или нескольких полей в json-файле")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные клиента успешно обновлены", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OwnerCat.class))})
    })
    public ResponseEntity<HttpStatus> updateById(@PathVariable Long id, @RequestBody OwnerCatDto ownerCatDto) {
        ownersCatsService.updateById(id, ownerCatDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "Получение данных о клиенте приюта кошек", description = "Информацию о клиенте можно получить путем ввода одного или нескольких параметров - фамилии, телефона, логина в Telegram и/или идентификатора")
    @Parameters(value = {
            @Parameter(name = "surname", example = "Иванов"),
            @Parameter(name = "telephone", example = "+71231234567"),
            @Parameter(name = "username", example = "leleka"),
            @Parameter(name = "id", example = "целое число, большее либо равное 0, например, 5"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о клиенте по заданным параметрам получена", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = OwnerCat.class))}),
            @ApiResponse(responseCode = "400", description = "Параметры запроса отсутствуют или имеют некорректный формат"),
            @ApiResponse(responseCode = "404", description = "Клиент с такими данными отсутствует в клиентской базе приюта"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка, не зависящая от вызывающей стороны")
    })
    public ResponseEntity<List<OwnerCatDto>> findByIdUsernameSurnameTelephone(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String telephone) {

        if (id != null) {
            return ResponseEntity.ok().body(Collections.singletonList(ownersCatsService.findById(id)));
        }
        if (username != null && !username.isBlank()) {
            return ResponseEntity.ok(ownersCatsService.findByUsername(username));
        }
        if (surname != null && !surname.isBlank()) {
            return ResponseEntity.ok(ownersCatsService.findBySurname(surname));
        }
        if (telephone != null && !telephone.isBlank()) {
            return ResponseEntity.ok(ownersCatsService.findByPhoneNumber(telephone));
        } else {
            throw new NullPointerException("Клиент с такими параметрами не найден");
        }
    }

    @RequestMapping("/all")
    public ResponseEntity<List<OwnerCatDto>> getAll() {
        return ResponseEntity.ok(ownersCatsService.getAll());
    }

    @Operation(summary = "Удаление клиента из клиентской базы", description = "Клиент удаляется из базы клиентов путем введения его идентификатора")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Клиент успешно удален из клиентской базы", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OwnerCat.class))})
            //TODO: add responses
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        ownersCatsService.deleteById(id);
        return ResponseEntity.ok().build();
    }


//    @Operation(summary = "Установление сотруднику статуса Волонтера и назначение ему курируемых животных", description = "Сотрудник ищется по id и ему передается список id курируемых животных json-файла")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Статус на статус Волонтер успешно обновлен и ему назначены животные", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OwnerCat.class))}),
//            @ApiResponse(responseCode = "400", description = "Параметры запроса отсутствуют или имеют некорректный формат"),
//            @ApiResponse(responseCode = "500", description = "Произошла ошибка, не зависящая от вызывающей стороны")
//    })
//    @PutMapping("/volunteer/{id}")
//    public ResponseEntity<HttpStatus> setVolunteer(@PathVariable Long id, @RequestBody List<Long> catsIds) {
//        ownersCatsService.setVolunteer(id, catsIds);
//        return ResponseEntity.ok().build();
//    }

    @Operation(summary = "Передача клиенту животных и назначение первого испытательного срока", description = "Сотрудник ищется по id и ему передается список id курируемых животных json-файла")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Сотрудник найден и ему назначены животные", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OwnerCat.class))}),
            @ApiResponse(responseCode = "400", description = "Параметры запроса отсутствуют или имеют некорректный формат", content = @Content),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка, не зависящая от вызывающей стороны")})
    @PutMapping("/owner/{id}")
    public ResponseEntity<HttpStatus> setAnimalsToOwnerAndSetStartOfProbationPeriod(@PathVariable Long id, @RequestBody List<Long> catsIds, LocalDate dateStart) {
        ownersCatsService.setPetsToOwnerAndSetStartOfProbationPeriod(id, catsIds, dateStart);
        return ResponseEntity.ok().build();
    }

}
