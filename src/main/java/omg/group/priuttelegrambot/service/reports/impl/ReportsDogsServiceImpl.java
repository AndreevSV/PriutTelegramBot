package omg.group.priuttelegrambot.service.reports.impl;

import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;
import omg.group.priuttelegrambot.dto.owners.OwnerDogMapper;
import omg.group.priuttelegrambot.dto.pets.DogDto;
import omg.group.priuttelegrambot.dto.pets.DogsMapper;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.entity.pets.Dog;
import omg.group.priuttelegrambot.entity.reports.ReportDog;
import omg.group.priuttelegrambot.repository.reports.ReportsDogsRepository;
import omg.group.priuttelegrambot.service.reports.ReportsDogsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportsDogsServiceImpl implements ReportsDogsService {

    private final ReportsDogsRepository reportsDogsRepository;

    public ReportsDogsServiceImpl(ReportsDogsRepository reportsDogsRepository) {
        this.reportsDogsRepository = reportsDogsRepository;
    }

    @Override
    public void createNewReportsDog(OwnerDogDto ownerDto, DogDto petDto, LocalDate startDate) {
        int PROBATION_PERIOD_DAYS = 14;
        LocalDate dateOfLastReport = startDate.plusDays(PROBATION_PERIOD_DAYS);
        OwnerDog owner = OwnerDogMapper.toEntity(ownerDto);
        Dog pet = DogsMapper.toEntity(petDto);

        List<ReportDog> reports = new ArrayList<>();

        for (int i = 0; i < PROBATION_PERIOD_DAYS - 1; i++) {
            ReportDog report = new ReportDog();
            report.setOwner(owner);
            report.setPet(pet);
            report.setCreatedAt(LocalDateTime.now());
            report.setDateOfReport(startDate.plusDays(i));
            report.setDateOfLastReport(dateOfLastReport);

            reports.add(report);
        }
        reportsDogsRepository.saveAllAndFlush(reports);
    }
}
