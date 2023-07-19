package omg.group.priuttelegrambot.service;

import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface OwnersDogsService {
    void add(OwnerDogDto ownerDto);

    void updateById(Long id, OwnerDogDto ownerDto);

    List<OwnerDogDto> findById(Long id);

    Boolean findByChatId(Long chatId);

    List<OwnerDogDto> findByUsername(String username);

    List<OwnerDogDto> findBySurname(String surname);

    List<OwnerDogDto> findByTelephone(String telephone);

    List<OwnerDogDto> getAll();

    void deleteById(Long id);

    OwnerDogDto findDogsVolunteer();

    void setVolunteer(Long id, List<Long> dogsIds);

    default OwnerDog constructOwner(OwnerDogDto ownerDto) {

        OwnerDog owner = new OwnerDog();

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
        owner.setDogs(ownerDto.getDogs());
        owner.setChatId(ownerDto.getChatId());
        owner.setVolunteer(owner.getVolunteer());

        return owner;
    }

    default OwnerDogDto constructOwnerDto(OwnerDog owner) {

        OwnerDogDto ownerDto = new OwnerDogDto();

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
        ownerDto.setDogs(owner.getDogs());
        ownerDto.setChatId(owner.getChatId());
        ownerDto.setVolunteer(owner.getVolunteer());

        return ownerDto;
    }

}

