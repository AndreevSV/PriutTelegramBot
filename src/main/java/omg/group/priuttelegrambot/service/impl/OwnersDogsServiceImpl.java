package omg.group.priuttelegrambot.service.impl;


import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.Exception.OwnerDogNotFoundException;
import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.repository.OwnersDogsRepository;
import omg.group.priuttelegrambot.service.OwnersDogsService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OwnersDogsServiceImpl implements OwnersDogsService {

    private final OwnersDogsRepository ownersDogsRepository;


    @Override
    public HttpStatus add(OwnerDogDto ownerDto) {

        OwnerDog owner = constructOwner(ownerDto);
        owner.setCreatedAt(LocalDateTime.now());

        ownersDogsRepository.save(owner);
        return HttpStatus.CREATED;
    }

    @Override
    public HttpStatus updateById(Long id, OwnerDogDto ownerCatDto) {

        OwnerDog owner = constructOwner(ownerCatDto);
        owner.setUpdatedAt(LocalDateTime.now());

        if (ownersDogsRepository.existsById(id)) {
            ownersDogsRepository.save(owner);
            return HttpStatus.OK;
        } else {
            throw new OwnerDogNotFoundException();
        }
    }

    @Override
    public List<OwnerDogDto> findById(Long id) {

        Optional<OwnerDog> ownerDogOptional = ownersDogsRepository.findById(id);

        if (ownerDogOptional.isPresent()) {
            OwnerDog owner = ownerDogOptional.get();
            OwnerDogDto ownerDto = constructOwnerDto(owner);
            return Collections.singletonList(ownerDto);
        } else {
            throw new OwnerDogNotFoundException();
        }
    }

    @Override
    public List<OwnerDogDto> findByUsername(String username) {
        List<OwnerDog> ownerCatList = ownersDogsRepository.findByUserNameContainingIgnoreCase(username);
        return ownerCatList.stream()
                .map(this::constructOwnerDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OwnerDogDto> findBySurname(String surname) {
        List<OwnerDog> ownerList = ownersDogsRepository.findBySurnameContainingIgnoreCase(surname);
        return ownerList.stream()
                .map(this::constructOwnerDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OwnerDogDto> findByTelephone(String telephone) {
        List<OwnerDog> ownerList = ownersDogsRepository.findByTelephoneContainingIgnoreCase(telephone);
        return ownerList.stream()
                .map(this::constructOwnerDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OwnerDogDto> getAll() {
        List<OwnerDog> ownerList = ownersDogsRepository.findAll();
        return ownerList.stream()
                .map(this::constructOwnerDto)
                .collect(Collectors.toList());
    }

    @Override
    public HttpStatus deleteById(Long id) {
        if (ownersDogsRepository.existsById(id)) {
            ownersDogsRepository.deleteById(id);
            return HttpStatus.NO_CONTENT;
        } else {
            throw new OwnerDogNotFoundException();
        }
    }

}



