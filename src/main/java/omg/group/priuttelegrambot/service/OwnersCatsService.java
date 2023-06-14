package omg.group.priuttelegrambot.service;

import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface OwnersCatsService {

    HttpStatus add(OwnerCatDto ownerDto);

    HttpStatus updateById(Long id, OwnerCatDto ownerDto);

    List<OwnerCatDto> findById(Long id);

    List<OwnerCatDto> findByUsername(String username);

    List<OwnerCatDto> findBySurname(String surname);

    List<OwnerCatDto> findByTelephone(String telephone);

    List<OwnerCatDto> getAll();

    HttpStatus deleteById(Long id);

    default OwnerCat constructOwner(OwnerCatDto ownerDto) {

        OwnerCat owner = new OwnerCat();

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
        owner.setIsVolunteer(ownerDto.getIsVolunteer());
        owner.setCatId(ownerDto.getCatId());
        owner.setFirstProbation(ownerDto.getFirstProbation());
        owner.setProbationStarts(ownerDto.getProbationStarts());
        owner.setProbationEnds(ownerDto.getProbationEnds());
        owner.setPassedProbation(ownerDto.getPassedProbation());

        return owner;
    }

    default OwnerCatDto constructOwnerDto(OwnerCat owner) {

        OwnerCatDto ownerDto = new OwnerCatDto();

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
        ownerDto.setIsVolunteer(owner.getIsVolunteer());
        ownerDto.setCatId(owner.getCatId());
        ownerDto.setFirstProbation(owner.getFirstProbation());
        ownerDto.setProbationStarts(owner.getProbationStarts());
        ownerDto.setProbationEnds(owner.getProbationEnds());
        ownerDto.setPassedProbation(owner.getPassedProbation());

        return ownerDto;
    }
}
