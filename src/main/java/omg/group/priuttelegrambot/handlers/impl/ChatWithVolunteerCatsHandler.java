package omg.group.priuttelegrambot.handlers.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.service.OwnersCatsService;
import org.springframework.stereotype.Service;

@Service
public class ChatWithVolunteerCatsHandler {

    private final OwnersCatsService ownersCatsService;

    private final TelegramBot telegramBot;

    public ChatWithVolunteerCatsHandler(OwnersCatsService ownersCatsService, TelegramBot telegramBot) {
        this.ownersCatsService = ownersCatsService;
        this.telegramBot = telegramBot;
    }

    /**
     * Метод отправляет запрос свободному Волонтеру на необходимость переписки.
     * Волонтер отвечает пользователю по нажатию кнопки "Ответить"
     */
    private void callCatsVolunteer() {

        OwnerCatDto catsVolunteer = ownersCatsService.findCatsVolunteer();

        Long chatId = catsVolunteer.getChatId();

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Ответить").callbackData("/answer"));

        SendMessage sendMessage = new SendMessage(chatId, """
                Клиенту требуется консультация.
                Пожалуйста, свяжитесь с ним в ближайшее время.
                """).replyMarkup(inlineKeyboardMarkup);

        telegramBot.execute(sendMessage);
    }

    /**
     * Метод отправляет запрос свободному Волонтеру на необходимость переписки.
     * Волонтер отвечает пользователю по нажатию кнопки "Ответить"
     */
//    private void callDogsVolunteer() {
//
//        OwnerDogDto dogsVolunteer = ownersDogsService.findDogsVolunteer();
//
//        Long chatId = dogsVolunteer.getChatId();
//
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Ответить").callbackData("/answer"));
//
//        SendMessage sendMessage = new SendMessage(chatId, """
//                Клиенту требуется консультация.
//                Пожалуйста, свяжитесь с ним в ближайшее время.
//                """).replyMarkup(inlineKeyboardMarkup);
//
//        telegramBot.execute(sendMessage);
//    }

    private void sendQuestionFromOwnerToVolunteer(String text) {


    }

    private void forwardQuestionFromOwnerToVolunteer(String text) {

//        String text = update.chosenInlineResult().


    }
}
