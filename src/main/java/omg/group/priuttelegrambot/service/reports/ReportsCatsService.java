package omg.group.priuttelegrambot.service.reports;

import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.dto.pets.CatDto;

import java.time.LocalDate;

public interface ReportsCatsService {
    void createNewReportsCat(OwnerCatDto ownerDto, CatDto petDto, LocalDate startDate);
}
