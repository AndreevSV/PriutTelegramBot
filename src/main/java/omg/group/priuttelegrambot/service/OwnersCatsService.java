package omg.group.priuttelegrambot.service;

import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.dto.OwnerCatDto;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.repository.OwnersCatsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OwnersCatsService {

    private final OwnersCatsRepository ownersCatsRepository;

    public HttpStatus save(OwnerCatDto ownerCatDto) {
        OwnerCat owner = constructOwner(ownerCatDto);
        ownersCatsRepository.save(owner);
        return HttpStatus.CREATED;
    }

    public HttpStatus delete(OwnerCatDto ownerCatDto) {
        OwnerCat owner = constructOwner(ownerCatDto);
        ownersCatsRepository.delete(owner);
        return HttpStatus.NO_CONTENT;
    }

    public Optional<OwnerCat> findById(Long id) {
        return ownersCatsRepository.findById(id);
    }

    public HttpStatus deleteById(Long id) {
        ownersCatsRepository.deleteById(id);
        return HttpStatus.OK;
    }

    private OwnerCat constructOwner(OwnerCatDto ownerCatDto) {
        OwnerCat owner = new OwnerCat();
        owner.setUserName(ownerCatDto.getUserName());
        owner.setSurname(ownerCatDto.getSurname());
        owner.setPatronymic(ownerCatDto.getPatronymic());
        owner.setBirthday(ownerCatDto.getBirthday());
        owner.setTelephone(ownerCatDto.getTelephone());
        owner.setEmail(ownerCatDto.getEmail());
        owner.setAddress(ownerCatDto.getAddress());
        owner.setCreatedAt(ownerCatDto.getCreatedAt());
        owner.setUpdatedAt(ownerCatDto.getUpdatedAt());
        owner.setDateIncome(ownerCatDto.getDateIncome());
        owner.setDateOutcome(ownerCatDto.getDateOutcome());
        owner.setBecameClient(ownerCatDto.getBecameClient());
        owner.setVolunteer(ownerCatDto.getVolunteer());
        owner.setCatId(ownerCatDto.getCatId());
        owner.setFirstProbation(ownerCatDto.getFirstProbation());
        owner.setProbationStarts(ownerCatDto.getProbationStarts());
        owner.setProbationEnds(ownerCatDto.getProbationEnds());
        owner.setPassedProbation(ownerCatDto.getPassedProbation());

        return owner;
    }

}
