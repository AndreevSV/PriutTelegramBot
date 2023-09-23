package omg.group.priuttelegrambot.service.reports.impl;

import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.dto.owners.OwnerCatMapper;
import omg.group.priuttelegrambot.dto.pets.CatDto;
import omg.group.priuttelegrambot.dto.pets.CatsMapper;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.entity.pets.Cat;
import omg.group.priuttelegrambot.entity.reports.ReportCat;
import omg.group.priuttelegrambot.repository.reports.ReportsCatsRepository;
import omg.group.priuttelegrambot.service.reports.ReportsCatsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportsCatsServiceImpl implements ReportsCatsService {

    private final ReportsCatsRepository reportsCatsRepository;

    public ReportsCatsServiceImpl(ReportsCatsRepository reportsCatsRepository) {
        this.reportsCatsRepository = reportsCatsRepository;
    }

    @Override
    public void createNewReportsCat(OwnerCatDto ownerDto, CatDto petDto, LocalDate startDate) {
        int PROBATION_PERIOD_DAYS = 14;
        LocalDate dateOfLastReport = startDate.plusDays(PROBATION_PERIOD_DAYS);
        OwnerCat owner = OwnerCatMapper.toEntity(ownerDto);
        Cat pet = CatsMapper.toEntity(petDto);

        List<ReportCat> reports = new ArrayList<>();

        for (int i = 0; i < PROBATION_PERIOD_DAYS - 1; i++) {
            ReportCat report = new ReportCat();
            report.setOwner(owner);
            report.setPet(pet);
            report.setCreatedAt(LocalDateTime.now());
            report.setDateOfReport(startDate.plusDays(i));
            report.setDateOfLastReport(dateOfLastReport);

            reports.add(report);
        }
        reportsCatsRepository.saveAllAndFlush(reports);
    }

}