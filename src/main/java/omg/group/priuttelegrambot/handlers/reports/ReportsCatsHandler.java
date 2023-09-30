package omg.group.priuttelegrambot.handlers.reports;

import com.pengrad.telegrambot.model.Update;
import omg.group.priuttelegrambot.dto.reports.ReportsCatsDto;

public interface ReportsCatsHandler {

    ReportsCatsDto isReportExist(Update update);

    boolean receivePhoto(Update update);

    boolean receiveRation(Update update);

    boolean receiveFeeling(Update update);

    boolean receiveChanges(Update update);

    ReportsCatsDto isReportCompleted(ReportsCatsDto reportDto);

    ReportsCatsDto returnReportDtoFromUpdate(Update update);

    ReportsCatsDto isPhoto(ReportsCatsDto reportDto);

    ReportsCatsDto isRation(ReportsCatsDto reportDto);

    ReportsCatsDto isFeeling(ReportsCatsDto reportDto);

    ReportsCatsDto isChanges(ReportsCatsDto reportDto);
}
