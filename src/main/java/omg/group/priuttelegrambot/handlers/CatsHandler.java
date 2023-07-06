package omg.group.priuttelegrambot.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    void receivePhoto(Update update);

    void receiveRation(Update update);

    void receiveFeeling(Update update);

    void receiveChanges(Update update);

    Boolean isProbationStarted(Update update);
}
