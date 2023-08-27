package omg.group.priuttelegrambot.handlers.reports;

import com.pengrad.telegrambot.model.Update;
import omg.group.priuttelegrambot.entity.reports.ReportCat;

public interface ReportsCatsHandler {

    ReportCat isReportExist(Update update);

    boolean receivePhoto(Update update);

    boolean receiveRation(Update update);

    boolean receiveFeeling(Update update);

    boolean receiveChanges(Update update);

    ReportCat isReportCompleted(ReportCat report);

    ReportCat returnReportFromUpdate(Update update);

    ReportCat isPhoto(ReportCat report);

    ReportCat isRation(ReportCat report);

    ReportCat isFeeling(ReportCat report);

    ReportCat isChanges(ReportCat report);
}
