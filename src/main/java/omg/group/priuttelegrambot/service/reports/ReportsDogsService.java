package omg.group.priuttelegrambot.service.reports;

import omg.group.priuttelegrambot.dto.pets.DogDto;

public interface ReportsDogsService {
    void createNewReportsDog(DogDto petDto);
}
