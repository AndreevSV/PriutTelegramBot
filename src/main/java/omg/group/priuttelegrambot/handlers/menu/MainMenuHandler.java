package omg.group.priuttelegrambot.handlers.menu;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

public interface MainMenuHandler {
    void executeStartMenuButton(Update update);

    void noSuchCommandSendMessage(Update update);

    void executeSendRationButton(Long chatId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void executeSendFeelingButton(Long chatId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void executeSendChangesButton(Long chatId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void executeSendPhotoButton(Long chatId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void executeSendContactsButton(Long chatId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void executeCallVolunteerButton(Long chatId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void reportAlreadySent(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void photoAlreadySent(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void rationAlreadySent(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void feelingAlreadySent(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void changesAlreadySent(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void chattingAlready(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);
}
