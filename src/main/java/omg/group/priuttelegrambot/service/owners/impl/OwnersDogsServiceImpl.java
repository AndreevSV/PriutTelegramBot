package omg.group.priuttelegrambot.service.owners.impl;


import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;
import omg.group.priuttelegrambot.dto.owners.OwnerDogMapper;
import omg.group.priuttelegrambot.dto.pets.DogDto;
import omg.group.priuttelegrambot.dto.pets.DogsMapper;
import omg.group.priuttelegrambot.entity.pets.Dog;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.repository.pets.DogsRepository;
import omg.group.priuttelegrambot.repository.owners.OwnersDogsRepository;
import omg.group.priuttelegrambot.service.owners.OwnersDogsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OwnersDogsServiceImpl implements OwnersDogsService {

    private final OwnersDogsRepository ownersDogsRepository;
    private final DogsRepository dogsRepository;

    @Override
    public void add(OwnerDogDto ownerDto) {

        OwnerDog owner = OwnerDogMapper.toEntity(ownerDto);
        owner.setCreatedAt(LocalDateTime.now());

        ownersDogsRepository.save(owner);
    }

    @Override
    public void updateById(Long id, OwnerDogDto ownerCatDto) {

        OwnerDog owner = OwnerDogMapper.toEntity(ownerCatDto);
        owner.setUpdatedAt(LocalDateTime.now());

        if (ownersDogsRepository.existsById(id)) {
            ownersDogsRepository.save(owner);
        } else {
            System.out.println((String.format("Клиент с id %d не найден", id)));
        }
    }

    @Override
    public OwnerDogDto findById(Long id) {

        Optional<OwnerDog> ownerDogOptional = ownersDogsRepository.findById(id);

        if (ownerDogOptional.isPresent()) {
            OwnerDog owner = ownerDogOptional.get();
            return OwnerDogMapper.toDto(owner);
        } else {
            System.out.println((String.format("Клиент с id %d не найден", id)));
            return null;
        }
    }

    @Override
    public Boolean findByChatId(Long chatId) {

        Optional<OwnerDog> ownerDogOptional = ownersDogsRepository.findByChatId(chatId);

        return ownerDogOptional.isPresent();
    }

    @Override
    public List<OwnerDogDto> findByUsername(String username) {
        List<OwnerDog> ownerCatList = ownersDogsRepository.findByUserNameContainingIgnoreCase(username);
        return ownerCatList.stream()
                .map(OwnerDogMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OwnerDogDto> findBySurname(String surname) {
        List<OwnerDog> ownerList = ownersDogsRepository.findBySurnameContainingIgnoreCase(surname);
        return ownerList.stream()
                .map(OwnerDogMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OwnerDogDto> findByTelephone(String telephone) {
        List<OwnerDog> ownerList = ownersDogsRepository.findByTelephoneContainingIgnoreCase(telephone);
        return ownerList.stream()
                .map(OwnerDogMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OwnerDogDto> getAll() {
        List<OwnerDog> ownerList = ownersDogsRepository.findAll();
        return ownerList.stream()
                .map(OwnerDogMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        if (ownersDogsRepository.existsById(id)) {
            ownersDogsRepository.deleteById(id);
        } else {
            System.out.println((String.format("Клиент с id %d не найден", id)));
        }
    }

    @Override
    public OwnerDogDto findDogsVolunteer() {

        Optional<OwnerDog> ownerOptional = ownersDogsRepository.findVolunteerByVolunteerIsTrueAndNoChatsOpened();

        if (ownerOptional.isPresent()) {
            OwnerDog owner = ownerOptional.get();
            return OwnerDogMapper.toDto(owner);
        } else {
            return null;
        }
    }

//    @Override
//    public void setVolunteer(Long id, List<Long> dogsIds) {
//
//        OwnerDog owner = ownersDogsRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Волонтер с таким id " + id + " не найден."));
//        owner.setIsVolunteer(true);
//        owner.setUpdatedAt(LocalDateTime.now());
//        owner.setVolunteerChatOpened(false);
//
//        List<Dog> dogs = dogsRepository.findAllById(dogsIds);
//        for (Dog dog : dogs) {
//            dog.setOwner(owner);
//        }
//
//        owner.setDogs(dogs);
//        ownersDogsRepository.save(owner);
//    }

    @Override
    public void setPetsToOwnerAndSetStartOfProbationPeriod(Long id, List<Long> petsIds, LocalDate dateStart) {

        List<Dog> pets = dogsRepository.findAllById(petsIds);
        List<DogDto> petsDto = new ArrayList<>();

        for (Dog pet : pets) {
            DogDto petDto = DogsMapper.toDto(pet);
            petsDto.add(petDto);
        }

        OwnerDogDto ownerDto = findById(id);
        ownerDto.setDateIncome(dateStart);
        ownerDto.setUpdatedAt(LocalDateTime.now());
        ownerDto.setBecameClient(true);
        ownerDto.setDogsDto(petsDto);
        OwnerDog owner = OwnerDogMapper.toEntity(ownerDto);
        ownersDogsRepository.save(owner);
    }

}





