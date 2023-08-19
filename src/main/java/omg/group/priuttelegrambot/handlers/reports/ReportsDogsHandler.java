package omg.group.priuttelegrambot.handlers.reports;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import omg.group.priuttelegrambot.entity.pets.Dog;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ReportsDogsHandler {
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
}
