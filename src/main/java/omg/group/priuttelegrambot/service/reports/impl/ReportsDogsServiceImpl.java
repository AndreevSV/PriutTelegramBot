package omg.group.priuttelegrambot.service.reports.impl;

import omg.group.priuttelegrambot.dto.pets.DogDto;
import omg.group.priuttelegrambot.dto.pets.DogsMapper;
import omg.group.priuttelegrambot.entity.pets.Dog;
import omg.group.priuttelegrambot.entity.reports.ReportDog;
import omg.group.priuttelegrambot.repository.reports.ReportsDogsRepository;
import omg.group.priuttelegrambot.service.reports.ReportsDogsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
}