package omg.group.priuttelegrambot.handlers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import omg.group.priuttelegrambot.dto.reports.ReportsCatsDto;

public interface AnimalHandler {

    InlineKeyboardMarkup formPriutMainMenuButton();

    InlineKeyboardMarkup formInlineKeyboardForInfoMenuButton();

    InlineKeyboardMarkup formInlineKeyboardForTakeMenuButton();

    InlineKeyboardMarkup formInlineKeyboardForSendReportButton();

    void executeButtonOrCommand(Update update, InlineKeyboardMarkup inlineKeyboardMarkup);

    void newOwnerRegister();

    ReportsCatsDto receivePhoto(Update update);

    ReportsCatsDto receiveRation(Update update);

    ReportsCatsDto receiveFeeling(Update update);

    ReportsCatsDto receiveChanges(Update update);
}
