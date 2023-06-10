package omg.group.priuttelegrambot.controllers;

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
import omg.group.priuttelegrambot.entity.clients.Clients;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.ClientsService;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Tag(name = "Клиентская база", description = "CRUD-операции для работы с данными клиентов")
public class ClientsController {
    private final ClientsService clientsService;


    @GetMapping
    @Operation(
            summary = "Получение данных о клиенте приюта собак/кошек",
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
                                    array = @ArraySchema(schema = @Schema(implementation = Clients.class))
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
    public ResponseEntity<Object> getClientsData(@RequestParam(required = false) String surname,
                                                 @RequestParam(required = false) String telephone,
                                                 @RequestParam(required = false) String username,
                                                 @RequestParam(required = false) long id
    ) {

        if (surname != null && !surname.isBlank()) {
            return ResponseEntity.ok(clientsService.findClientsBySurname(surname));
        }
        if (telephone != null && !telephone.isBlank()) {
            return ResponseEntity.ok(clientsService.findClientsByTelephone(telephone));
        }
        if (username != null && !username.isBlank()) {
            return ResponseEntity.ok(clientsService.findClientsByUsername(username));
        }
        return ResponseEntity.ok(clientsService.getAllClients());
    }

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
                                    array = @ArraySchema(schema = @Schema(implementation = Clients.class))
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

    public ResponseEntity<Long> addClient(@RequestBody Clients clients) {
        long id = clientsService.addClient(clients);
        return ResponseEntity.ok(id);
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
                                    array = @ArraySchema(schema = @Schema(implementation = Clients.class))
                            )
                    }
            )
    })
    public ResponseEntity<Clients> editClientsData(@PathVariable long id, @RequestBody Clients clients) {
        Clients clients1 = clientsService.updateClient(id, clients);
        if (clients1 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(clients1);
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
                                    array = @ArraySchema(schema = @Schema(implementation = Clients.class))
                            )
                    }
            )
    })
    public ResponseEntity<Void> deleteClient(@PathVariable long id) {
        if (clientsService.deleteClient(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}

