package omg.group.priuttelegrambot.service;


import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.repository.OwnersDogsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OwnersDogsService {

    private final OwnersDogsRepository ownersDogsRepository;

    public HttpStatus save(OwnerDogDto ownerDogDto) {
        OwnerDog owner = constructOwner(ownerDogDto);
        ownersDogsRepository.save(owner);
        return HttpStatus.CREATED;
    }

    public HttpStatus delete(OwnerDogDto ownerDogDto) {
        OwnerDog owner = constructOwner(ownerDogDto);
        ownersDogsRepository.delete(owner);
        return HttpStatus.NO_CONTENT;
    }

    public Optional<OwnerDog> findById(Long id) {
        return ownersDogsRepository.findById(id);
    }

    public HttpStatus deleteById(Long id) {
        ownersDogsRepository.deleteById(id);
        return HttpStatus.OK;
    }

    private OwnerDog constructOwner(OwnerDogDto ownerDogDto) {
        OwnerDog owner = new OwnerDog();
        owner.setUserName(ownerDogDto.getUserName());
        owner.setSurname(ownerDogDto.getSurname());
        owner.setPatronymic(ownerDogDto.getPatronymic());
        owner.setBirthday(ownerDogDto.getBirthday());
        owner.setTelephone(ownerDogDto.getTelephone());
        owner.setEmail(ownerDogDto.getEmail());
        owner.setAddress(ownerDogDto.getAddress());
        owner.setCreatedAt(ownerDogDto.getCreatedAt());
        owner.setUpdatedAt(ownerDogDto.getUpdatedAt());
        owner.setDateIncome(ownerDogDto.getDateIncome());
        owner.setDateOutcome(ownerDogDto.getDateOutcome());
        owner.setBecameClient(ownerDogDto.getBecameClient());
        owner.setVolunteer(ownerDogDto.getVolunteer());
        owner.setDogId(ownerDogDto.getDogId());
        owner.setFirstProbation(ownerDogDto.getFirstProbation());
        owner.setProbationStarts(ownerDogDto.getProbationStarts());
        owner.setProbationEnds(ownerDogDto.getProbationEnds());
        owner.setPassedProbation(ownerDogDto.getPassedProbation());

        return owner;
    }

}



