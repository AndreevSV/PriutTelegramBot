package omg.group.priuttelegrambot.service.owners;

import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;

import java.util.List;

public interface OwnersCatsService {
    void add(OwnerCatDto ownerDto);
    void updateById(Long id, OwnerCatDto ownerDto);
    List<OwnerCatDto> findById(Long id);
    Boolean findByChatId(Long chatId);
    List<OwnerCatDto> findByUsername(String username);
    List<OwnerCatDto> findBySurname(String surname);
    List<OwnerCatDto> findByTelephone(String telephone);
    List<OwnerCatDto> getAll();
    void deleteById(Long id);
    OwnerCatDto findCatsVolunteer();
    void setVolunteer(Long id, List<Long> catsIds);

    default OwnerCat constructOwner(OwnerCatDto ownerDto) {

        OwnerCat owner = new OwnerCat();

        owner.setId(ownerDto.getId());
        owner.setUserName(ownerDto.getUserName());
        owner.setName(ownerDto.getName());
        owner.setSurname(ownerDto.getSurname());
        owner.setPatronymic(ownerDto.getPatronymic());
        owner.setBirthday(ownerDto.getBirthday());
        owner.setTelephone(ownerDto.getTelephone());
        owner.setEmail(ownerDto.getEmail());
        owner.setAddress(ownerDto.getAddress());
        owner.setCreatedAt(ownerDto.getCreatedAt());
        owner.setUpdatedAt(ownerDto.getUpdatedAt());
        owner.setDateIncome(ownerDto.getDateIncome());
        owner.setDateOutcome(ownerDto.getDateOutcome());
        owner.setBecameClient(ownerDto.getBecameClient());
        owner.setIsVolunteer(owner.getIsVolunteer());
        owner.setCats(ownerDto.getCats());
        owner.setChatId(ownerDto.getChatId());
        owner.setVolunteer(owner.getVolunteer());

        return owner;
    }

    default OwnerCatDto constructOwnerDto(OwnerCat owner) {

        OwnerCatDto ownerDto = new OwnerCatDto();

        ownerDto.setId(owner.getId());
        ownerDto.setUserName(owner.getUserName());
        ownerDto.setName(owner.getName());
        ownerDto.setSurname(owner.getSurname());
        ownerDto.setPatronymic(owner.getPatronymic());
        ownerDto.setBirthday(owner.getBirthday());
        ownerDto.setTelephone(owner.getTelephone());
        ownerDto.setEmail(owner.getEmail());
        ownerDto.setAddress(owner.getAddress());
        ownerDto.setCreatedAt(owner.getCreatedAt());
        ownerDto.setUpdatedAt(owner.getUpdatedAt());
        ownerDto.setDateIncome(owner.getDateIncome());
        ownerDto.setDateOutcome(owner.getDateOutcome());
        ownerDto.setBecameClient(owner.getBecameClient());
        ownerDto.setIsVolunteer(owner.getIsVolunteer());
        ownerDto.setCats(owner.getCats());
        ownerDto.setChatId(owner.getChatId());
        ownerDto.setVolunteer(owner.getVolunteer());


        return ownerDto;
    }

}
