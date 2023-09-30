package omg.group.priuttelegrambot.handlers.menu;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

public interface MainMenuHandler {
    void executeStartMenuButton(Update update);

    void noSuchCommandSendMessage(Update update);

    void noReportExistMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void executeSendRationButtonMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void executeSendFeelingButtonMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void executeSendChangesButtonMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void executeSendPhotoButtonMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void executeSendContactsButtonMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void executeCallVolunteerButtonMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void reportAlreadySentMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void photoAlreadySentMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void photoDublicateSentMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void photoSavedOkMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void notPhotoMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void rationAlreadySentMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void rationSavedOkMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void feelingAlreadySentMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void feelingSavedOkMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void changesAlreadySentMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void changesSavedOkMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void chatAlreadySetMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void telephoneAlreadySetMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);

    //    void waitingForContactMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);
    void waitingForContactMessage(Long chatId, InlineKeyboardMarkup inlineKeyboardMarkup);

//    void contactSavedOkMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup);
    void contactSavedOkMessage(Long chatId, InlineKeyboardMarkup inlineKeyboardMarkup);


    void volunteerOpenedChatMessage(Long ownerChatId, Long volunteerChatId);

    void ownerClosedChatMessage(Long ownerChatId, Long volunteerChatId);

    void volunteerClosedChatMessage(Long volunteerChatId, Long ownerChatId);

    void noFreeVolunteerAvailableMessage(Long ownerChatId);

    void inquiryToVolunteerForChat(Long volunteerChatId, Long ownerChatId);

    void chatAlreadySetToOwnerMessage(Long ownerChatId);

    void chatForwardMessageWithCloseButton(Long chatId, Update update);

    void noPetMessage(Long ownerChatId, InlineKeyboardMarkup inlineKeyboardMarkup);

    void noOwnOfPetRegisteredMessage(Long ownerChatId, InlineKeyboardMarkup inlineKeyboardMarkup);
}
