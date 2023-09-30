package omg.group.priuttelegrambot.handlers.menu;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

public interface DogsMenuHandler {
    InlineKeyboardMarkup formPriutMainMenuButton();

    InlineKeyboardMarkup formInlineKeyboardForInfoMenuButton();

    InlineKeyboardMarkup formInlineKeyboardForTakeMenuButton();

    InlineKeyboardMarkup formInlineKeyboardForSendReportButton();

    void executeButtonOrCommand(Update update, InlineKeyboardMarkup inlineKeyboardMarkup);

}
