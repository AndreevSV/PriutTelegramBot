package omg.group.priuttelegrambot.handlers.reports;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import omg.group.priuttelegrambot.dto.reports.ReportsDogsDto;
import omg.group.priuttelegrambot.entity.pets.Dog;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.entity.reports.ReportDog;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface ReportsDogsHandler {
    ReportsDogsDto isReportExist(Update update);
    boolean receivePhoto(Update update);
    boolean receiveRation(Update update);
    boolean receiveFeeling(Update update);
    boolean receiveChanges(Update update);
    ReportsDogsDto isReportCompleted(ReportsDogsDto reportDto);
    ReportsDogsDto returnReportFromUpdate(Update update);
    ReportsDogsDto isPhoto(ReportsDogsDto reportDto);
    ReportsDogsDto isRation(ReportsDogsDto reportDto);
    ReportsDogsDto isFeeling(ReportsDogsDto reportDto);
    ReportsDogsDto isChanges(ReportsDogsDto reportDto);

}
