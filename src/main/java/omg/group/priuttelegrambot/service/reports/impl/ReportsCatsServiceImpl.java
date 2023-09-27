package omg.group.priuttelegrambot.service.reports.impl;

import omg.group.priuttelegrambot.dto.pets.CatDto;
import omg.group.priuttelegrambot.dto.pets.CatsMapper;
import omg.group.priuttelegrambot.entity.pets.Cat;
import omg.group.priuttelegrambot.entity.reports.ReportCat;
import omg.group.priuttelegrambot.repository.reports.ReportsCatsRepository;
import omg.group.priuttelegrambot.service.reports.ReportsCatsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ReportsCatsServiceImpl implements ReportsCatsService {

    private final ReportsCatsRepository reportsCatsRepository;

    public ReportsCatsServiceImpl(ReportsCatsRepository reportsCatsRepository) {
        this.reportsCatsRepository = reportsCatsRepository;
    }

    @Override
    @Transactional
    public void createNewReportCat(CatDto petDto) {
        Cat pet = CatsMapper.toEntity(petDto);
        ReportCat report = new ReportCat();
        report.setOwner(pet.getOwner());
        report.setPet(pet);
        report.setCreatedAt(LocalDateTime.now());
        report.setDateOfReport(LocalDate.now());
        reportsCatsRepository.save(report);
    }
}