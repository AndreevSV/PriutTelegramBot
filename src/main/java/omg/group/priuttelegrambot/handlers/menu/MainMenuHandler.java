package omg.group.priuttelegrambot.handlers.menu;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

public interface MainMenuHandler {
    void executeStartMenuButton(Update update);

    void noSuchCommandSendMessage(Update update);

    void executeSendRationButton(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void executeSendFeelingButton(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void executeSendChangesButton(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void executeSendPhotoButton(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void executeSendContactsButton(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void executeCallVolunteerButton(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void reportAlreadySentMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void photoAlreadySentMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void rationAlreadySentMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void feelingAlreadySentMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void changesAlreadySentMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void chatAlreadySetMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

//    void waitingForContactMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);
    void waitingForContactMessage(Long chatId, InlineKeyboardMarkup inlineKeyboardMarkup);

//    void contactSavedOkMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);
    void contactSavedOkMessage(Long chatId, InlineKeyboardMarkup inlineKeyboardMarkup);


    void volunteerOpenedChatMessage(Long ownerChatId, Long volunteerChatId);

    void ownerClosedChatMessage(Long ownerChatId, Long volunteerChatId);

    void volunteerClosedChatMessage(Long volunteerChatId, Long ownerChatId);

    void noFreeVolunteerAvailableMessage(Long ownerChatId);
}
