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
@RequestMapping("/clients")
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
                    name = "telephone", example = "81231234567"
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
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны"

            )
    })
    public ResponseEntity<Clients> getClientsData(@RequestParam(required = false) String surname,
                                                  @RequestParam(required = false) long telephone,
                                                  @RequestParam(required = false) String username,
                                                  @RequestParam(required = false) long id
    ) {

        if (surname != null && !surname.isBlank()) {
            return ResponseEntity.ok(clientsService.findClientsBySurname(surname));
        }
        if (telephone == 0) {
            return ResponseEntity.ok(clientsService.findClientsByTelephone(telephone));
        }
        if (username != null && !username.isBlank()) {
            return ResponseEntity.ok(clientsService.findClientsByUsername(username));
        }
        return (ResponseEntity<List<Clients>>) ResponseEntity.ok(clientsService.getAllClients());
    }

    @PostMapping("/add")
    public String addClient(@RequestBody Clients clients);
}
