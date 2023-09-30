package omg.group.priuttelegrambot.service.owners;

import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface OwnersDogsService {
    void add(OwnerDogDto ownerDto);
    void updateById(Long id, OwnerDogDto ownerDto);
    OwnerDogDto findById(Long ownerId);
    Boolean findByChatId(Long chatId);
    List<OwnerDogDto> findByUsername(String username);
    List<OwnerDogDto> findBySurname(String surname);
    List<OwnerDogDto> findByPhoneNumber(String telephone);
    List<OwnerDogDto> getAll();
    void deleteById(Long ownerId);
    OwnerDogDto findDogsVolunteer();
    void setPetsToOwnerAndDateIncome(Long ownerId, List<Long> petsIds, LocalDate dateStart);

    @Transactional
    void setOwnerDateOfOutcome(Long ownerId, LocalDate dateEnds);
}

