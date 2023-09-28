package omg.group.priuttelegrambot.controllers.reports;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.dto.reports.ReportsCatsDto;
import omg.group.priuttelegrambot.dto.reports.ReportsDogsDto;
import omg.group.priuttelegrambot.service.reports.ReportsDogsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports/dogs")
@RequiredArgsConstructor
@Tag(name = "Контроллер отчетов собак")
public class ReportsDogsController {

    private final ReportsDogsService reportsDogsService;

    @Operation(summary = "Получение всех отчетов на сегодня")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ReportsCatsDto.class)))}),
            @ApiResponse(responseCode = "400", description = "Параметры запроса отсутствуют или имеют некорректный формат", content = @Content),
            @ApiResponse(responseCode = "404", description = "Отчеты не найдены", content = @Content),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка, не зависящая от вызывающей стороны", content = @Content)
    })
    @GetMapping("/today")
    public ResponseEntity<List<ReportsDogsDto>> getReportsForToday() {
        return ResponseEntity.ok(reportsDogsService.getReportsForToday());
    }

    @Operation(summary = "Получение всех отчетов на конкретную дату")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ReportsCatsDto.class)))}),
            @ApiResponse(responseCode = "400", description = "Параметры запроса отсутствуют или имеют некорректный формат", content = @Content),
            @ApiResponse(responseCode = "404", description = "Отчеты не найдены", content = @Content),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка, не зависящая от вызывающей стороны", content = @Content)
    })
    @GetMapping(params = "type=by-date")
    public ResponseEntity<List<ReportsDogsDto>> getReportsForDate(@RequestParam LocalDate localDate) {
        return ResponseEntity.ok(reportsDogsService.getReportsForDate(localDate));
    }

    @Operation(summary = "Получение всех незаполненных отчетов на конкретную дату")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ReportsCatsDto.class)))}),
            @ApiResponse(responseCode = "400", description = "Параметры запроса отсутствуют или имеют некорректный формат", content = @Content),
            @ApiResponse(responseCode = "404", description = "Отчеты не найдены", content = @Content),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка, не зависящая от вызывающей стороны", content = @Content)
    })
    @GetMapping(params = "type=not-fullfilled")
    public ResponseEntity<List<ReportsDogsDto>> getReportsForDateWithNotAllReportFullfilled(@RequestParam LocalDate localDate) {
        return ResponseEntity.ok(reportsDogsService.getReportsForDate(localDate));
    }

}
