package omg.group.priuttelegrambot.service;

import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface OwnersDogsService {
    HttpStatus add(OwnerDogDto ownerDto);

    HttpStatus updateById(Long id, OwnerDogDto ownerDto);

    List<OwnerDogDto> findById(Long id);

    List<OwnerDogDto> findByUsername(String username);

    List<OwnerDogDto> findBySurname(String surname);

    List<OwnerDogDto> findByTelephone(String telephone);

    List<OwnerDogDto> getAll();

    HttpStatus deleteById(Long id);

    default OwnerDog constructOwner(OwnerDogDto ownerDto) {

        OwnerDog owner = new OwnerDog();

        owner.setUserName(ownerDto.getUserName());
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
        owner.setVolunteer(ownerDto.getVolunteer());
        owner.setDogId(ownerDto.getDogId());
        owner.setFirstProbation(ownerDto.getFirstProbation());
        owner.setProbationStarts(ownerDto.getProbationStarts());
        owner.setProbationEnds(ownerDto.getProbationEnds());
        owner.setPassedProbation(ownerDto.getPassedProbation());

        return owner;
    }

    default OwnerDogDto constructOwnerDto(OwnerDog owner) {

        OwnerDogDto ownerDto = new OwnerDogDto();

        ownerDto.setUserName(owner.getUserName());
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
        ownerDto.setVolunteer(owner.getVolunteer());
        ownerDto.setDogId(owner.getDogId());
        ownerDto.setFirstProbation(owner.getFirstProbation());
        ownerDto.setProbationStarts(owner.getProbationStarts());
        ownerDto.setProbationEnds(owner.getProbationEnds());
        ownerDto.setPassedProbation(owner.getPassedProbation());

        return ownerDto;
    }
}

