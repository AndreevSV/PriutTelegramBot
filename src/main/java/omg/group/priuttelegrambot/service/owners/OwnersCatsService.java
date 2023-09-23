package omg.group.priuttelegrambot.service.owners;

import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;

import java.time.LocalDate;
import java.util.List;

public interface OwnersCatsService {
    void add(OwnerCatDto ownerDto);
    void updateById(Long id, OwnerCatDto ownerDto);
    OwnerCatDto findById(Long id);
    Boolean findByChatId(Long chatId);
    List<OwnerCatDto> findByUsername(String username);
    List<OwnerCatDto> findBySurname(String surname);
    List<OwnerCatDto> findByPhoneNumber(String telephone);
    List<OwnerCatDto> getAll();
    void deleteById(Long id);
    OwnerCatDto findCatsVolunteer();
//    void setVolunteer(Long id, List<Long> catsIds);
    void setPetsToOwnerAndSetStartOfProbationPeriod(Long id, List<Long> petsIds, LocalDate dateStart);
}
