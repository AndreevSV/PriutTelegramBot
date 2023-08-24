package omg.group.priuttelegrambot.handlers.reports;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import jakarta.validation.constraints.NotNull;
import omg.group.priuttelegrambot.entity.pets.Cat;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.entity.reports.ReportCat;

import java.util.List;
import java.util.Optional;

public interface ReportsCatsHandler {


    boolean isReportExist(Update update);
    boolean receivePhoto(Update update);
    boolean receiveRation(Update update);
    boolean receiveFeeling(Update update);
    boolean receiveChanges(Update update);
    boolean checkForProbationPeriodSetAndValid(@NotNull Update update);
    boolean isReportCompleted(Update update);
    boolean isPhoto(Update update);
    boolean isRation(Update update);
    boolean isFeeling(Update update);
    boolean isChanges(Update update);
    Optional<ReportCat> returnReportCatOptional(Update update);
}
