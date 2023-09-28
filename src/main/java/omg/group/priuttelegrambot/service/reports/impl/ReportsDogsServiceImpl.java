package omg.group.priuttelegrambot.service.reports.impl;

import omg.group.priuttelegrambot.dto.pets.DogDto;
import omg.group.priuttelegrambot.dto.pets.DogsMapper;
import omg.group.priuttelegrambot.dto.reports.ReportsDogsDto;
import omg.group.priuttelegrambot.dto.reports.ReportsDogsMapper;
import omg.group.priuttelegrambot.entity.pets.Dog;
import omg.group.priuttelegrambot.entity.reports.ReportDog;
import omg.group.priuttelegrambot.exception.NotFoundException;
import omg.group.priuttelegrambot.repository.reports.ReportsDogsRepository;
import omg.group.priuttelegrambot.service.reports.ReportsDogsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportsDogsServiceImpl implements ReportsDogsService {

    private final ReportsDogsRepository reportsDogsRepository;

    public ReportsDogsServiceImpl(ReportsDogsRepository reportsDogsRepository) {
        this.reportsDogsRepository = reportsDogsRepository;
    }

    @Override
    @Transactional
    public void createNewReportsDog(DogDto petDto) {
        Dog pet = DogsMapper.toEntity(petDto);
        ReportDog report = new ReportDog();
        report.setOwner(pet.getOwner());
        report.setPet(pet);
        report.setCreatedAt(LocalDateTime.now());
        report.setDateOfReport(LocalDate.now());
        reportsDogsRepository.save(report);
    }

    @Override
    public List<ReportsDogsDto> getReportsForToday() {
        LocalDate currentDay = LocalDate.now();
        List<ReportDog> reports = reportsDogsRepository.findByDateOfReport(currentDay);

        if (reports.isEmpty()) {
            throw new NotFoundException("На сегодня нет ни одного отчета");
        } else {
            return reports.stream().map(ReportsDogsMapper::toDto).collect(Collectors.toList());
        }
    }

    @Override
    public List<ReportsDogsDto> getReportsForDate(LocalDate localDate) {
        List<ReportDog> reports = reportsDogsRepository.findByDateOfReport(localDate);
        if (reports.isEmpty()) {
            throw new NotFoundException(String.format("На заданную дату %s нет ни одного отчета", localDate));
        } else {
            return reports.stream().map(ReportsDogsMapper::toDto).collect(Collectors.toList());
        }
    }

    @Override
    public List<ReportsDogsDto> getReportsForDateWithNotAllReportFullfilled(LocalDate localDate) {
        List<ReportDog> reports = reportsDogsRepository.findAllReportsThatNotFullfilled(localDate);
        if (reports.isEmpty()) {
            throw new NotFoundException(String.format("На заданную дату %s нет ни одного отчета", localDate));
        } else {
            return reports.stream().map(ReportsDogsMapper::toDto).collect(Collectors.toList());
        }
    }
}