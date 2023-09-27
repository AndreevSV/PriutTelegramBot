package omg.group.priuttelegrambot.service.reports;

import omg.group.priuttelegrambot.dto.pets.CatDto;

public interface ReportsCatsService {
    void createNewReportCat(CatDto petDto);
}
