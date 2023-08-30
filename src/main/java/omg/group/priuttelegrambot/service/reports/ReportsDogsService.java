package omg.group.priuttelegrambot.service.reports;

import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;
import omg.group.priuttelegrambot.dto.pets.DogDto;

import java.time.LocalDate;

public interface ReportsDogsService {
    void createNewReportsDog(OwnerDogDto ownerDto, DogDto petDto, LocalDate startDate);
}
