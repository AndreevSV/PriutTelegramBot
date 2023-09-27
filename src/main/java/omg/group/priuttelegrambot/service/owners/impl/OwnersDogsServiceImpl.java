package omg.group.priuttelegrambot.service.owners.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;
import omg.group.priuttelegrambot.dto.owners.OwnerDogMapper;
import omg.group.priuttelegrambot.dto.pets.DogDto;
import omg.group.priuttelegrambot.dto.pets.DogsMapper;
import omg.group.priuttelegrambot.entity.pets.Dog;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.exception.BadRequestException;
import omg.group.priuttelegrambot.exception.NotFoundException;
import omg.group.priuttelegrambot.repository.pets.DogsRepository;
import omg.group.priuttelegrambot.repository.owners.OwnersDogsRepository;
import omg.group.priuttelegrambot.service.owners.OwnersDogsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OwnersDogsServiceImpl implements OwnersDogsService {

    private final OwnersDogsRepository ownersDogsRepository;
    private final DogsRepository dogsRepository;

    @Override
    @Transactional
    public void add(OwnerDogDto ownerDto) {
        if (ownerDto != null) {
            OwnerDog owner = OwnerDogMapper.toEntity(ownerDto);
            owner.setCreatedAt(LocalDateTime.now());
            ownersDogsRepository.save(owner);
            log.info("The OwnerDog added successfully");
        } else {
            String errorMessage = "The OwnerDogDto is empty";
            log.error(errorMessage);
            throw new BadRequestException(errorMessage);
        }
    }

    @Override
    @Transactional
    public void updateById(Long id, OwnerDogDto ownerCatDto) {
        if (ownersDogsRepository.existsById(id)) {
            OwnerDog owner = OwnerDogMapper.toEntity(ownerCatDto);
            owner.setUpdatedAt(LocalDateTime.now());
            ownersDogsRepository.save(owner);
            log.info("The OwnerCat updated successfully");
        } else {
            String errorMessage = String.format("The OwnerDog with id %d not found", id);
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }
    }

    @Override
    public OwnerDogDto findById(Long ownerId) {
        Optional<OwnerDog> ownerDogOptional = ownersDogsRepository.findById(ownerId);
        if (ownerDogOptional.isPresent()) {
            OwnerDog owner = ownerDogOptional.get();
            log.info("The OwnerDog found successfully");
            return OwnerDogMapper.toDto(owner);
        } else {
            String errorMessage = String.format("The OwnerDog with id %d not found", ownerId);
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }
    }

    @Override
    public Boolean findByChatId(Long chatId) {
        Optional<OwnerDog> ownerDogOptional = ownersDogsRepository.findByChatId(chatId);
        return ownerDogOptional.isPresent();
    }

    @Override
    public List<OwnerDogDto> findByUsername(String username) {
        List<OwnerDog> ownerList = ownersDogsRepository.findByUserNameContainingIgnoreCase(username);
        if (ownerList.isEmpty()) {
            String errorMessage = "No one OwnerDogs found";
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        } else {
            log.info("OwnerDogs found");
            return ownerList.stream()
                    .map(OwnerDogMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<OwnerDogDto> findBySurname(String surname) {
        List<OwnerDog> ownerList = ownersDogsRepository.findBySurnameContainingIgnoreCase(surname);
        if (ownerList.isEmpty()) {
            String errorMessage = "No one OwnerDogs found";
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        } else {
            log.info("OwnerDogs found");
            return ownerList.stream()
                    .map(OwnerDogMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<OwnerDogDto> findByPhoneNumber(String telephone) {
        List<OwnerDog> ownerList = ownersDogsRepository.findByTelephoneContainingIgnoreCase(telephone);
        if (ownerList.isEmpty()) {
            String errorMessage = "No one OwnerDogs found";
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        } else {
            log.info("OwnerDogs found");
            return ownerList.stream()
                    .map(OwnerDogMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<OwnerDogDto> getAll() {
        List<OwnerDog> ownerList = ownersDogsRepository.findAll();
        if (ownerList.isEmpty()) {
            String errorMessage = "No one OwnerDogs found";
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        } else {
            log.info("OwnerDogs found");
            return ownerList.stream()
                    .map(OwnerDogMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public void deleteById(Long ownerId) {
        if (ownersDogsRepository.existsById(ownerId)) {
            ownersDogsRepository.deleteById(ownerId);
            log.info("OwnerDog deleted successfully");
        } else {
            String errorMessage = String.format("The OwnerDog with id %d not found", ownerId);
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }
    }

    @Override
    public OwnerDogDto findDogsVolunteer() {
        Optional<OwnerDog> ownerOptional = ownersDogsRepository.findVolunteerByVolunteerIsTrueAndNoChatsOpened();
        if (ownerOptional.isPresent()) {
            OwnerDog owner = ownerOptional.get();
            log.info("Free Dog volunteer found successfully");
            return OwnerDogMapper.toDto(owner);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public void setPetsToOwnerAndDateIncome(
            Long ownerId,
            List<Long> petsIds,
            LocalDate dateStart
    ) {
        OwnerDogDto ownerDto = findById(ownerId);
        List<Dog> pets = dogsRepository.findAllById(petsIds);
        if (pets.isEmpty()) {
            throw new NotFoundException("No one pet found");
        } else {
            List<DogDto> petsDto = pets.stream().map(DogsMapper::toDto).collect(Collectors.toList());
            ownerDto.setDateIncome(dateStart);
            ownerDto.setUpdatedAt(LocalDateTime.now());
            ownerDto.setBecameClient(true);
            ownerDto.setDogsDto(petsDto);

            OwnerDog owner = OwnerDogMapper.toEntity(ownerDto);
            ownersDogsRepository.save(owner);
            log.info("Dog set to owner and probation period started");
        }
    }

    @Override
    @Transactional
    public void setOwnerDateOfOutcome(Long ownerId, LocalDate dateEnds) {
        OwnerDogDto ownerDto = findById(ownerId);
        ownerDto.setUpdatedAt(LocalDateTime.now());
        ownerDto.setDateOutcome(dateEnds);
        OwnerDog owner = OwnerDogMapper.toEntity(ownerDto);
        ownersDogsRepository.save(owner);
        log.info("Day of outcome set to owner");
    }

}