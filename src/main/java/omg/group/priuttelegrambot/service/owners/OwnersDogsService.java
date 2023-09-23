package omg.group.priuttelegrambot.service.owners;

import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OwnersDogsService {
    void add(OwnerDogDto ownerDto);
    void updateById(Long id, OwnerDogDto ownerDto);
    OwnerDogDto findById(Long id);
    Boolean findByChatId(Long chatId);
    List<OwnerDogDto> findByUsername(String username);
    List<OwnerDogDto> findBySurname(String surname);
    List<OwnerDogDto> findByTelephone(String telephone);
    List<OwnerDogDto> getAll();
    void deleteById(Long id);
    OwnerDogDto findDogsVolunteer();
//    void setVolunteer(Long id, List<Long> dogsIds);

    void setPetsToOwnerAndSetStartOfProbationPeriod(Long id, List<Long> petsIds, LocalDate dateStart);
}

