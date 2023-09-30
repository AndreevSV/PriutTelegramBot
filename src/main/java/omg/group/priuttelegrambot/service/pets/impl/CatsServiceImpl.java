package omg.group.priuttelegrambot.service.pets.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import omg.group.priuttelegrambot.dto.pets.CatDto;
import omg.group.priuttelegrambot.entity.pets.Cat;
import omg.group.priuttelegrambot.entity.pets.petsenum.CatsBreed;
import omg.group.priuttelegrambot.entity.pets.petsenum.Sex;
import omg.group.priuttelegrambot.exception.BadRequestException;
import omg.group.priuttelegrambot.exception.NotFoundException;
import omg.group.priuttelegrambot.repository.pets.CatsRepository;
import omg.group.priuttelegrambot.service.pets.CatsService;
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
public class CatsServiceImpl implements CatsService {

    private final CatsRepository catsRepository;

    @Override
    @Transactional
    public void add(CatDto catDto) {
        if (catDto != null) {
            Cat cat = mapCatFromCatDto(catDto);
            cat.setCreatedAt(LocalDateTime.now());
            catsRepository.save(cat);
            log.info("The Cat added successfully");
        } else {
            String errorMessage = "The CatDto is empty";
            log.error(errorMessage);
            throw new BadRequestException(errorMessage);
        }
    }

    @Override
    @Transactional
    public void updateById(Long id, CatDto catDto) {
        if (catsRepository.existsById(id)) {
            Cat cat = mapCatFromCatDto(catDto);
            cat.setUpdatedAt(LocalDateTime.now());
            catsRepository.save(cat);
            log.info("The Cat updated successfully {} ", catDto);
        } else {
            String errorMessage = String.format("The Cat with id %d not found", id);
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }
    }

    @Override
    public CatDto findById(Long id) {
        Optional<Cat> catOptional = catsRepository.findById(id);
        if (catOptional.isPresent()) {
            Cat cat = catOptional.get();
            log.info("The OwnerCat found successfully {} ", cat);
            return mapCatDtoFromCat(cat);
        } else {
            String errorMessage = String.format("The Cat with id %d not found", id);
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }
    }

    @Override
    public List<CatDto> findByIdContaining(Long id) {
        List<Cat> catsList = catsRepository.findByIdContaining(id);
        if (catsList.isEmpty()) {
            String errorMessage = "No one Cat found";
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        } else {
            log.info("Cats found");
            return catsList.stream()
                    .map(this::mapCatDtoFromCat)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<CatDto> findBySex(Sex sex) {
        List<Cat> catsList = catsRepository.findBySex(sex);
        if (catsList.isEmpty()) {
            String errorMessage = "No one Cat found";
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        } else {
            log.info("Cats found");
            return catsList.stream()
                    .map(this::mapCatDtoFromCat)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<CatDto> findByNickname(String nickname) {
        List<Cat> catsList = catsRepository.findByNickNameContainingIgnoreCase(nickname);
        if (catsList.isEmpty()) {
            String errorMessage = "No one Cat found";
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        } else {
            log.info("Cats found");
            return catsList.stream()
                    .map(this::mapCatDtoFromCat)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<CatDto> findByBreed(CatsBreed breed) {
        List<Cat> catsList = catsRepository.findByBreedContaining(breed);
        if (catsList.isEmpty()) {
            String errorMessage = "No one Cat found";
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        } else {
            log.info("Cats found");
            return catsList.stream()
                    .map(this::mapCatDtoFromCat)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<CatDto> findByBirthdayBetweenDates(Date startDate, Date endDate) {
        List<Cat> catsList = catsRepository.findByBirthdayBetween(startDate, endDate);
        if (catsList.isEmpty()) {
            String errorMessage = "No one Cat found";
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        } else {
            log.info("Cats found");
            return catsList.stream()
                    .map(this::mapCatDtoFromCat)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<CatDto> getAll() {
        List<Cat> catsList = catsRepository.findAll();
        if (catsList.isEmpty()) {
            String errorMessage = "No one Cat found";
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        } else {
            log.info("Cats found");
            return catsList.stream()
                    .map(this::mapCatDtoFromCat)
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (catsRepository.existsById(id)) {
            catsRepository.deleteById(id);
            log.info("Cat deleted successfully");
        } else {
            String errorMessage = String.format("The Cat with id %d not found", id);
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }
    }

    @Override
    @Transactional
    public void setFirstProbationPeriod(Long petId, LocalDate date) {
        final int PROBATION_PERIOD_DAYS = 30;
        CatDto catDto = findById(petId);
        catDto.setUpdatedAt(LocalDateTime.now());
        catDto.setFirstProbation(true);
        catDto.setProbationStarts(date);
        catDto.setProbationEnds(date.plusDays(PROBATION_PERIOD_DAYS));

        Cat cat = mapCatFromCatDto(catDto);
        catsRepository.save(cat);
    }

    @Override
    @Transactional
    public LocalDate setSecondProbationPeriod(Long petId) {
        final int PROBATION_PERIOD_DAYS = 30;
        CatDto petDto = findById(petId);
        petDto.setUpdatedAt(LocalDateTime.now());
        petDto.setSecondProbation(true);
        petDto.setProbationEnds(petDto.getProbationEnds().plusDays(PROBATION_PERIOD_DAYS));
        petDto.setPassedFirstProbation(false);

        Cat pet = mapCatFromCatDto(petDto);
        catsRepository.save(pet);

        return petDto.getProbationEnds();
    }

    /**
     * If volunteer set that probation period is passed then doesn't matter if it first probation or second,
     * all probation markers sets to passed
     * @param petId Id of the pet
     */
    @Override
    @Transactional
    public void setProbationPassed(Long petId) {
        CatDto petDto = findById(petId);

        if (petDto.getFirstProbation() && petDto.getPassedFirstProbation() == null) {
            petDto.setUpdatedAt(LocalDateTime.now());
            petDto.setPassedFirstProbation(true);
            Cat pet = mapCatFromCatDto(petDto);
            catsRepository.save(pet);
            return;
        }

        if (petDto.getSecondProbation() && petDto.getPassedSecondProbation() == null) {
            petDto.setUpdatedAt(LocalDateTime.now());
            petDto.setPassedSecondProbation(true);
            Cat pet = mapCatFromCatDto(petDto);
            catsRepository.save(pet);
        }
    }

    @Override
    public List<CatDto> getAllPetsOnProbationToday() {
        LocalDate currentDay = LocalDate.now();
        List<Cat> petsOnProbationList = catsRepository.findCatsWhereCurrentDateBetweenProbationStartsAndProbationEnds(currentDay);
        if (!petsOnProbationList.isEmpty()) {
            return petsOnProbationList.stream().map(this::mapCatDtoFromCat).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

}