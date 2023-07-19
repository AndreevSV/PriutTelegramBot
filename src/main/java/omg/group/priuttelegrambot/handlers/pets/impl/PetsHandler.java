package omg.group.priuttelegrambot.handlers.pets.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetsHandler {

    private final TelegramBot telegramBot;

    public void executeStartMenuButton(Update update) {
        Long chatId = 0L;
        String userName = "";

        if (update.message() != null) {
            chatId = update.message().chat().id();
            userName = update.message().from().username();
        } else if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
            userName = update.callbackQuery().from().username();
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        inlineKeyboardMarkup.addRow(
                new InlineKeyboardButton("Приют для кошек").callbackData("/cat"),
                new InlineKeyboardButton("Приют для собак").callbackData("/dog"));

        SendMessage sendMessage = new SendMessage(chatId, String.format("""
                Привет %s
                Вы запустили бот
                *приютов собак и кошек*
                Выберите необходимый приют ниже:
                """, userName))
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup);

        telegramBot.execute(sendMessage);
    }

    public void noSuchCommandSendMessage(Update update) {
        Long chatId = 0L;

        if (update.message() != null) {
            chatId = update.message().chat().id();
        } else if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
        }

        SendMessage sendMessage = new SendMessage(chatId, """
            Нет такой команды.
            Попробуйте воспользоваться кнопками меню""");

        telegramBot.execute(sendMessage);
    }

}
