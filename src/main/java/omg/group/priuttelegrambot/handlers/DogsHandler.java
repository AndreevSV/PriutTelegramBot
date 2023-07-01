package omg.group.priuttelegrambot.handlers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import omg.group.priuttelegrambot.dto.reports.ReportsDogsDto;
import omg.group.priuttelegrambot.entity.report.ReportDogBoolean;

public interface DogsHandler {

        InlineKeyboardMarkup formPriutMainMenuButton();

        InlineKeyboardMarkup formInlineKeyboardForInfoMenuButton();

        InlineKeyboardMarkup formInlineKeyboardForTakeMenuButton();

        InlineKeyboardMarkup formInlineKeyboardForSendReportButton();

        void executeButtonOrCommand(Update update, InlineKeyboardMarkup inlineKeyboardMarkup);

        void newOwnerRegister();

        ReportDogBoolean receivePhoto(Update update);

    ReportsDogsDto receiveRation(Update update);

    ReportsDogsDto receiveFeeling(Update update);

    ReportsDogsDto receiveChanges(Update update);

}
