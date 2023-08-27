package omg.group.priuttelegrambot.handlers.reports;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import omg.group.priuttelegrambot.entity.pets.Dog;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.entity.reports.ReportDog;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface ReportsDogsHandler {
    ReportDog isReportExist(Update update);
    boolean receivePhoto(Update update);
    boolean receiveRation(Update update);
    boolean receiveFeeling(Update update);
    boolean receiveChanges(Update update);
    ReportDog isReportCompleted(ReportDog report);

    ReportDog returnReportFromUpdate(Update update);

    ReportDog isPhoto(ReportDog report);
    ReportDog isRation(ReportDog report);
    ReportDog isFeeling(ReportDog report);
    ReportDog isChanges(ReportDog report);

}
