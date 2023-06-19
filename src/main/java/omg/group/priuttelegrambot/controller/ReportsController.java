//package omg.group.priuttelegrambot.controller;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import omg.group.priuttelegrambot.entity.report.Report;
//import omg.group.priuttelegrambot.listener.TelegramBotUpdatesListener;
//import omg.group.priuttelegrambot.service.ReportService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/reports")
//@RequiredArgsConstructor
//@Tag(name = "Контроллер для работы с отчетами владельцев о содержании животных в процессе испытательного срока",
//        description = "Получение отчетов владельцев животных в период испытательного срока")
//public class ReportsController {
//
//
//    private final ReportService reportService;
//    private final TelegramBotUpdatesListener telegramBotUpdatesListener;
//
//    private final String fileType = "image/jpeg";
//
//    @Autowired
//    public ReportsController(TelegramBotUpdatesListener telegramBotUpdatesListener, ReportService reportService) {
//        this.telegramBotUpdatesListener = telegramBotUpdatesListener;
//        this.reportService = reportService;
//    }
//
//    @GetMapping("/{id}")
//    @Operation(
//            summary = "Просмотр отчетов по id",
//            description = "Данные об отчете можно получить путем ввода его идентификатора - целого числа, " +
//                    "большего либо равного 0"
//    )
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Отчет с заданным идентификатором успешно найден",
//                    content = {
//                            @Content(
//                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
//                                    schema = @Schema(implementation = Report.class)))
//    }
//    )
//}),
//
//@ApiResponse(
//        responseCode = "404",
//        description = "Отчет с таким id отсутствует в базе приюта"
//),
//@ApiResponse(
//        responseCode = "500",
//        description = "Произошла ошибка, не зависящая от вызывающей стороны"
//)
//})
//
//public ReportdownloadReport(@Parameter(description = "report id") @PathVariable Long id){
//        return this.reportService.findById(id);
//        }
//
//@DeleteMapping("/{id}")
//@Operation(summary = "Удаление отчетов по id",
//        responses = {
//                @ApiResponse(
//                        responseCode = "200",
//                        description = "Удаленный отчет",
//                        content = @Content(
//                                mediaType = MediaType.APPLICATION_JSON_VALUE,
//                                schema = @Schema(implementation = Report.class)
//                        )
//                )
//
//        }
//
//
//        public void remove(@Parameter(description = "report id") @PathVariable Long id) {
//        this.reportService.remove(id);
//        }
//
//@GetMapping("/getAll")
//@Operation(summary = "Просмотр всех отчетов",
//        responses = {
//                @ApiResponse(
//                        responseCode = "200",
//                        description = "Все отчеты, либо отчеты определенного пользователя по chat_id",
//                        content = @Content(
//                                mediaType = MediaType.APPLICATION_JSON_VALUE,
//                                schema = @Schema(implementation = Report.class)
//                        )
//                )
//        }
//)
//
//public ResponseEntity<Collection<Report>>getAll(){
//        return ResponseEntity.ok(this.reportService.getAll());
//        }
//
//@GetMapping("/{id}/photo-from-db")
//@Operation(summary = "Просмотр фото по id отчета",
//        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                description = "Фото, найденное по id отчета")
//        })
//
//
//public ResponseEntity<byte[]>downloadPhotoFromDB(@Parameter(description = "report id") @PathVariable Long id){
//        Report report=this.reportService.findById(id);
//
//        HttpHeaders headers=new HttpHeaders();
//        headers.setContentType(MediaType.parseMediaType(fileType));
//        headers.setContentLength(report.getData().length);
//
//        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(report.getData());
//        }
//@GetMapping("/message-to-person")
//@Operation(summary = "Отправить сообщение пользователю",
//        responses = {
//                @ApiResponse(
//                        responseCode = "200",
//                        description = "Сообщение определенному пользователю" +
//                                "Например сообщение о том, что необходимо правильно заполнять форму отчета. Либо связаться с волонтерами по номеру",
//                        content = @Content(
//                                mediaType = MediaType.APPLICATION_JSON_VALUE,
//                                schema = @Schema(implementation = Report.class)
//                        )
//                )
//        })
//
//public void sendMessageToPerson(@Parameter(description = "id чата с пользователем", example = "12345670")
//@RequestParam Long chat_Id,
//@Parameter(description = "Ваше сообщение")
//@RequestParam(String message){
//        this.telegramBotUpdatesListener.sendMessage(chat_Id,message);
//        })
//

