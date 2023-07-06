package omg.group.priuttelegrambot.handlers.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtherHandlers {
    private Long chatId;
    private int messageId;
    private String command;
    private String userName;
    private String firstName;
    private String lastName;
    private final TelegramBot telegramBot;

    public void extractDataFromUpdate(Update update) {

        if (update.message() != null) {
            chatId = update.message().chat().id();
            messageId = update.message().messageId();
            command = update.message().text();
            userName = update.message().from().username();
            firstName = update.message().from().firstName();
            lastName = update.message().from().lastName();
        } else if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
            messageId = update.callbackQuery().message().messageId();
            command = update.callbackQuery().data();
            userName = update.callbackQuery().from().username();
            firstName = update.callbackQuery().from().firstName();
            lastName = update.callbackQuery().from().lastName();
        }
    }
    public void executeStartMenuButton(Update update) {

        extractDataFromUpdate(update);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        inlineKeyboardMarkup.addRow(
                new InlineKeyboardButton("Приют для кошек").callbackData("/cat"),
                new InlineKeyboardButton("Приют для собак").callbackData("/dog"));

        SendMessage sendMessage = new SendMessage(chatId, String.format("""
                Привет %s
                Вы запустили бот *приютов собак и кошек*
                Выберите необходимый приют ниже:
                """, userName)).parseMode(ParseMode.Markdown).replyMarkup(inlineKeyboardMarkup);

        telegramBot.execute(sendMessage);
    }

//    public void setReplyKeyboardMarkup(Update update) {
//
//        extractDataFromUpdate(update);
//
//        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(new KeyboardButton("/start"));
//
//        replyKeyboardMarkup.resizeKeyboard(true);
//        replyKeyboardMarkup.oneTimeKeyboard(true);
//
//        SendMessage sendMessage = new SendMessage(chatId, "Нажмите /start");
//        sendMessage.replyMarkup(replyKeyboardMarkup);
//
//        telegramBot.execute(sendMessage);
//
//    }

    public void noSuchCommandSendMessage(Update update) {
        extractDataFromUpdate(update);

        SendMessage sendMessage = new SendMessage(chatId, """
            Нет такой команды.
            Попробуйте воспользоваться кнопками меню""");

        telegramBot.execute(sendMessage);
    }

}
