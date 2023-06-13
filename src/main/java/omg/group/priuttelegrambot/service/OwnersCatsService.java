package omg.group.priuttelegrambot.service;

import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.repository.OwnersCatsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OwnersCatsService {

    private final OwnersCatsRepository ownersCatsRepository;

    public HttpStatus add(OwnerCatDto ownerCatDto) {

        OwnerCat owner = constructOwner(ownerCatDto);
        owner.setCreatedAt(LocalDateTime.now());

        ownersCatsRepository.save(owner);
        return HttpStatus.CREATED;
    }

    public HttpStatus updateById(Long id, OwnerCatDto ownerCatDto) {

        OwnerCat owner = constructOwner(ownerCatDto);
        owner.setUpdatedAt(LocalDateTime.now());

        if (ownersCatsRepository.existsById(id)) {
            ownersCatsRepository.save(owner);
            return HttpStatus.OK;
        } else {
            throw new RuntimeException(String.format("Клиент с id %d не найден", id));
        }
    }

    public Optional<OwnerCat> findById(Long id) {
        return ownersCatsRepository.findById(id);
    }

    public List<OwnerCatDto> getAll() {
        return ownersCatsRepository.findAll().stream()
                .map(ownerCat -> {

                    OwnerCatDto ownerCatDto = new OwnerCatDto();

                    ownerCatDto.setId(ownerCat.getId());
                    ownerCatDto.setUserName(ownerCat.getUserName());
                    ownerCatDto.setName(ownerCat.getName());
                    ownerCatDto.setSurname(ownerCat.getSurname());
                    ownerCatDto.setPatronymic(ownerCat.getPatronymic());
                    ownerCatDto.setTelephone(ownerCat.getTelephone());
                    ownerCatDto.setEmail(ownerCat.getEmail());
                    ownerCatDto.setAddress(ownerCat.getAddress());
                    ownerCatDto.setBecameClient(ownerCat.getBecameClient());
                    ownerCatDto.setVolunteer(ownerCat.getVolunteer());
                    ownerCatDto.setCatId(ownerCat.getCatId());

                    return ownerCatDto;

                })
                .collect(Collectors.toList());
    }


    public HttpStatus deleteById(Long id) {
        if (ownersCatsRepository.existsById(id)) {
            ownersCatsRepository.deleteById(id);
            return HttpStatus.NO_CONTENT;
        } else {
            throw new RuntimeException(String.format("Клиент с id %d не найден", id));
        }
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
//        owner.setCreatedAt(ownerCatDto.getCreatedAt());
//        owner.setUpdatedAt(ownerCatDto.getUpdatedAt());
//        owner.setDateIncome(ownerCatDto.getDateIncome());
//        owner.setDateOutcome(ownerCatDto.getDateOutcome());
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
