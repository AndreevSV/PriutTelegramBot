package omg.group.priuttelegrambot.handlers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import omg.group.priuttelegrambot.dto.reports.ReportsCatsDto;
import omg.group.priuttelegrambot.entity.report.ReportCatBoolean;

public interface CatsHandler {

    InlineKeyboardMarkup formPriutMainMenuButton();

    InlineKeyboardMarkup formInlineKeyboardForInfoMenuButton();

    InlineKeyboardMarkup formInlineKeyboardForTakeMenuButton();

    InlineKeyboardMarkup formInlineKeyboardForSendReportButton();

    void executeButtonOrCommand(Update update, InlineKeyboardMarkup inlineKeyboardMarkup);

    void newOwnerRegister();

    ReportCatBoolean receivePhoto(Update update);

    ReportsCatsDto receiveRation(Update update);

    ReportsCatsDto receiveFeeling(Update update);

    ReportsCatsDto receiveChanges(Update update);
}
