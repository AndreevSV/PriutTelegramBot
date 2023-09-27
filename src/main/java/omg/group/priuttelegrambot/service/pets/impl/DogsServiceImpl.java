package omg.group.priuttelegrambot.service.pets.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import omg.group.priuttelegrambot.dto.pets.DogDto;
import omg.group.priuttelegrambot.entity.pets.Dog;
import omg.group.priuttelegrambot.entity.pets.petsenum.DogsBreed;
import omg.group.priuttelegrambot.entity.pets.petsenum.Sex;
import omg.group.priuttelegrambot.exception.BadRequestException;
import omg.group.priuttelegrambot.exception.NotFoundException;
import omg.group.priuttelegrambot.repository.pets.DogsRepository;
import omg.group.priuttelegrambot.service.pets.DogsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DogsServiceImpl implements DogsService {

    private final DogsRepository dogsRepository;

    @Override
    public void add(DogDto dogDto) {
        if (dogDto != null) {
            Dog dog = mapDogFromDogDto(dogDto);
            dog.setCreatedAt(LocalDateTime.now());
            dogsRepository.save(dog);
            log.info("The Dog added successfully");
        } else {
            String errorMessage = "The DogDto is empty";
            log.error(errorMessage);
            throw new BadRequestException(errorMessage);
        }
    }

    @Override
    @Transactional
    public void updateById(Long id, DogDto dogDto) {
        if (dogsRepository.existsById(id)) {
            Dog dog = mapDogFromDogDto(dogDto);
            dog.setUpdatedAt(LocalDateTime.now());
            dogsRepository.save(dog);
            log.info("The Dog updated successfully {} ", dogDto);
        } else {
            String errorMessage = String.format("The Dog with id %d not found", id);
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }
    }

    @Override
    public DogDto findById(Long id) {
        Optional<Dog> dogOptional = dogsRepository.findById(id);
        if (dogOptional.isPresent()) {
            Dog dog = dogOptional.get();
            log.info("The Owner dog found successfully {} ", dog);
            return mapDogDtoFromDog(dog);
        } else {
            String errorMessage = String.format("The Dog with id %d not found", id);
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }
    }

    @Override
    public List<DogDto> findByIdContaining(Long id) {
        List<Dog> dogsList = dogsRepository.findByIdContaining(id);
        if (dogsList.isEmpty()) {
            String errorMessage = "No one Dog found";
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        } else {
            log.info("Dogs found");
            return dogsList.stream()
                    .map(this::mapDogDtoFromDog)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<DogDto> findBySex(Sex sex) {
        List<Dog> dogsList = dogsRepository.findBySex(sex);
        if (dogsList.isEmpty()) {
            String errorMessage = "No one Dog found";
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        } else {
            log.info("Dogs found");
            return dogsList.stream()
                    .map(this::mapDogDtoFromDog)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<DogDto> findByNickname(String nickname) {
        List<Dog> dogsList = dogsRepository.findByNickNameContainingIgnoreCase(nickname);
        if (dogsList.isEmpty()) {
            String errorMessage = "No one Dog found";
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        } else {
            log.info("Dogs found");
            return dogsList.stream()
                    .map(this::mapDogDtoFromDog)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<DogDto> findByBreed(DogsBreed breed) {
        List<Dog> dogsList = dogsRepository.findByBreedContaining(breed);
        if (dogsList.isEmpty()) {
            String errorMessage = "No one Dog found";
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        } else {
            log.info("Dogs found");
            return dogsList.stream()
                    .map(this::mapDogDtoFromDog)
                    .collect(Collectors.toList());
        }
    }


    @Override
    public List<DogDto> findByBirthdayBetweenDates(Date startDate, Date endDate) {
        List<Dog> dogsList = dogsRepository.findByBirthdayBetween(startDate, endDate);
        if (dogsList.isEmpty()) {
            String errorMessage = "No one Dog found";
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        } else {
            log.info("Dogs found");
            return dogsList.stream()
                    .map(this::mapDogDtoFromDog)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<DogDto> getAll() {
        List<Dog> dogsList = dogsRepository.findAll();
        if (dogsList.isEmpty()) {
            String errorMessage = "No one Dog found";
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        } else {
            log.info("Dogs found");
            return dogsList.stream()
                    .map(this::mapDogDtoFromDog)
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (dogsRepository.existsById(id)) {
            dogsRepository.deleteById(id);
            log.info("Dog deleted successfully");
        } else {
            String errorMessage = String.format("The Dog with id %d not found", id);
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }
    }

    @Override
    @Transactional
    public void setFirstProbationPeriod(Long petId, LocalDate date) {
        final int PROBATION_PERIOD_DAYS = 30;

        DogDto dogDto = findById(petId);
        dogDto.setUpdatedAt(LocalDateTime.now());
        dogDto.setFirstProbation(true);
        dogDto.setProbationStarts(date);
        dogDto.setProbationEnds(date.plusDays(PROBATION_PERIOD_DAYS));

        Dog dog = mapDogFromDogDto(dogDto);
        dogsRepository.save(dog);
    }

    @Override
    @Transactional
    public LocalDate setSecondProbationPeriod(Long petId) {
        final int PROBATION_PERIOD_DAYS = 30;
        DogDto petDto = findById(petId);
        petDto.setUpdatedAt(LocalDateTime.now());
        petDto.setSecondProbation(true);
        petDto.setProbationEnds(petDto.getProbationEnds().plusDays(PROBATION_PERIOD_DAYS));
        petDto.setPassedFirstProbation(false);

        Dog pet = mapDogFromDogDto(petDto);
        dogsRepository.save(pet);

        return petDto.getProbationEnds();
    }

    @Override
    @Transactional
    public void setSecondProbationPassed(Long petId) {
        DogDto petDto = findById(petId);
        petDto.setUpdatedAt(LocalDateTime.now());
        petDto.setPassedSecondProbation(true);

        Dog pet = mapDogFromDogDto(petDto);
        dogsRepository.save(pet);
    }

    @Override
    @Transactional
    public void setProbationPassed(Long petId) {
        DogDto petDto = findById(petId);

        if (petDto.getFirstProbation() && petDto.getPassedFirstProbation() == null) {
            petDto.setUpdatedAt(LocalDateTime.now());
            petDto.setPassedFirstProbation(true);
            Dog pet = mapDogFromDogDto(petDto);
            dogsRepository.save(pet);
            return;
        }

        if (petDto.getSecondProbation() && petDto.getPassedSecondProbation() == null) {
            petDto.setUpdatedAt(LocalDateTime.now());
            petDto.setPassedSecondProbation(true);
            Dog dog = mapDogFromDogDto(petDto);
            dogsRepository.save(dog);
        }
    }

    @Override
    public List<DogDto> getAllPetsOnProbationToday() {
        LocalDate currentDay = LocalDate.now();
        List<Dog> petsOnProbationList = dogsRepository.findDogsWhereCurrentDateBetweenProbationStartsAndProbationEnds(currentDay);
        if (!petsOnProbationList.isEmpty()) {
            return petsOnProbationList.stream().map(this::mapDogDtoFromDog).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

}