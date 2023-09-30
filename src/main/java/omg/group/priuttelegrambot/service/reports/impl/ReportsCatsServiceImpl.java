package omg.group.priuttelegrambot.service.reports.impl;

import omg.group.priuttelegrambot.dto.pets.CatDto;
import omg.group.priuttelegrambot.dto.pets.CatsMapper;
import omg.group.priuttelegrambot.dto.reports.ReportsCatsDto;
import omg.group.priuttelegrambot.dto.reports.ReportsCatsMapper;
import omg.group.priuttelegrambot.entity.pets.Cat;
import omg.group.priuttelegrambot.entity.reports.ReportCat;
import omg.group.priuttelegrambot.exception.NotFoundException;
import omg.group.priuttelegrambot.repository.reports.ReportsCatsRepository;
import omg.group.priuttelegrambot.service.reports.ReportsCatsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<ReportsCatsDto> getReportsForToday() {
        LocalDate currentDay = LocalDate.now();
        List<ReportCat> reports = reportsCatsRepository.findByDateOfReport(currentDay);

        if (reports.isEmpty()) {
            throw new NotFoundException("На сегодня нет ни одного отчета");
        } else {
            return reports.stream().map(ReportsCatsMapper::toDto).collect(Collectors.toList());
        }
    }

    @Override
    public List<ReportsCatsDto> getReportsForDate(LocalDate localDate) {
        List<ReportCat> reports = reportsCatsRepository.findByDateOfReport(localDate);
        if (reports.isEmpty()) {
            throw new NotFoundException(String.format("На заданную дату %s нет ни одного отчета", localDate));
        } else {
            return reports.stream().map(ReportsCatsMapper::toDto).collect(Collectors.toList());
        }
    }

    @Override
    public List<ReportsCatsDto> getReportsForDateWithNotAllReportFullfilled(LocalDate localDate) {
        List<ReportCat> reports = reportsCatsRepository.findAllReportsThatNotFullfilled(localDate);
        if (reports.isEmpty()) {
            throw new NotFoundException(String.format("На заданную дату %s нет ни одного отчета", localDate));
        } else {
            return reports.stream().map(ReportsCatsMapper::toDto).collect(Collectors.toList());
        }
    }


}