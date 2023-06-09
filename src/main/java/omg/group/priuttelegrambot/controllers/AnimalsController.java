package omg.group.priuttelegrambot.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/animals")
@RequiredArgsConstructor
@Tag(name = "Клиентская база", description = "CRUD-операции для работы с данными животных")
public class AnimalsController {
}
