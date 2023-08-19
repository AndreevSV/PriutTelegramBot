package omg.group.priuttelegrambot.handlers.contacts.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
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

        KeyboardButton keyboardButton = new KeyboardButton("Отправить контрактные данные").requestContact(true);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(keyboardButton)
                .oneTimeKeyboard(true)
                .resizeKeyboard(true);

        SendMessage sendMessage = new SendMessage(chatId, """
            Вы нажали кнопку *Оставить контактные данные*.
            Для подтверждения вы долджны нажать на специальную кнопку ниже.
            После подтверждения нам будет доступен Ваш *номер телефона*
            Пожалуйста, подтвердите, что вы готовы предоставить нам эти данные.
            """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(keyboardMarkup);

        telegramBot.execute(sendMessage);
    }

}
