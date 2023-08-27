package omg.group.priuttelegrambot.handlers.contacts.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import omg.group.priuttelegrambot.handlers.contacts.ContactsHandler;
import omg.group.priuttelegrambot.handlers.updates.OwnUpdatesHandler;
import org.springframework.stereotype.Service;

@Service
public class ContactsHandlerImpl implements ContactsHandler {

    private final TelegramBot telegramBot;
    private final OwnUpdatesHandler ownUpdatesHandler;

    public ContactsHandlerImpl(TelegramBot telegramBot, OwnUpdatesHandler ownUpdatesHandler) {
        this.telegramBot = telegramBot;
        this.ownUpdatesHandler = ownUpdatesHandler;
    }

    @Override
    public void askForContact(Update update) {

        Long chatId = ownUpdatesHandler.extractChatIdFromUpdate(update);
        int messageId = ownUpdatesHandler.extractMessageIdFromUpdate(update);

        KeyboardButton keyboardButton = new KeyboardButton("Поделиться контактом").requestContact(true);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(keyboardButton)
                .oneTimeKeyboard(true)
                .resizeKeyboard(true);

        SendMessage sendMessage = new SendMessage(chatId, """
            Вы нажали кнопку *Оставить контактные данные*.
            Для подтверждения вы должны нажать на специальную кнопку *Поделиться контактом* ниже под клавиатурой.
            После подтверждения нам будет доступен Ваш *номер телефона* для связи с Вами.
            Пожалуйста, подтвердите, что вы готовы предоставить нам эти данные.
            """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(keyboardMarkup);

        telegramBot.execute(sendMessage);
    }

}
