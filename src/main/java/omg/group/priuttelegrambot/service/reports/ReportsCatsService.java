package omg.group.priuttelegrambot.service.reports;

import omg.group.priuttelegrambot.dto.pets.CatDto;
import omg.group.priuttelegrambot.dto.reports.ReportsCatsDto;

import java.time.LocalDate;
import java.util.List;

public interface ReportsCatsService {
    void createNewReportCat(CatDto petDto);

    List<ReportsCatsDto> getReportsForToday();

    List<ReportsCatsDto> getReportsForDate(LocalDate localDate);

    List<ReportsCatsDto> getReportsForDateWithNotAllReportFullfilled(LocalDate localDate);
}
