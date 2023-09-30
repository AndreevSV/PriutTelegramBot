package omg.group.priuttelegrambot.service.reports;

import omg.group.priuttelegrambot.dto.pets.DogDto;
import omg.group.priuttelegrambot.dto.reports.ReportsDogsDto;

import java.time.LocalDate;
import java.util.List;

public interface ReportsDogsService {
    void createNewReportsDog(DogDto petDto);

    List<ReportsDogsDto> getReportsForToday();

    List<ReportsDogsDto> getReportsForDate(LocalDate localDate);

    List<ReportsDogsDto> getReportsForDateWithNotAllReportFullfilled(LocalDate localDate);
}
