package omg.group.priuttelegrambot.service.owners.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.dto.owners.OwnerCatMapper;
import omg.group.priuttelegrambot.dto.pets.CatDto;
import omg.group.priuttelegrambot.dto.pets.CatsMapper;
import omg.group.priuttelegrambot.entity.pets.Cat;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.exception.BadRequestException;
import omg.group.priuttelegrambot.exception.NotFoundException;
import omg.group.priuttelegrambot.repository.pets.CatsRepository;
import omg.group.priuttelegrambot.repository.owners.OwnersCatsRepository;
import omg.group.priuttelegrambot.service.owners.OwnersCatsService;
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
public class OwnersCatsServiceImpl implements OwnersCatsService {

    private final OwnersCatsRepository ownersCatsRepository;
    private final CatsRepository catsRepository;

    @Override
    @Transactional
    public void add(OwnerCatDto ownerCatDto) {
        if (ownerCatDto != null) {
            OwnerCat owner = OwnerCatMapper.toEntity(ownerCatDto);
            owner.setCreatedAt(LocalDateTime.now());
            ownersCatsRepository.save(owner);
            log.info("The OwnerCat added successfully");
        } else {
            String errorMessage = "The OwnerCatDto is empty";
            log.error(errorMessage);
            throw new BadRequestException(errorMessage);
        }
    }

    @Override
    @Transactional
    public void updateById(Long id, OwnerCatDto ownerCatDto) {
        if (ownersCatsRepository.existsById(id)) {
            OwnerCat owner = OwnerCatMapper.toEntity(ownerCatDto);
            owner.setUpdatedAt(LocalDateTime.now());
            ownersCatsRepository.save(owner);
            log.info("The OwnerCat updated successfully");
        } else {
            String errorMessage = String.format("The OwnerCat with id %d not found", id);
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }
    }

    @Override
    public OwnerCatDto findById(Long ownerId) {
        Optional<OwnerCat> ownerCatOptional = ownersCatsRepository.findById(ownerId);
        if (ownerCatOptional.isPresent()) {
            OwnerCat owner = ownerCatOptional.get();
            log.info("The OwnerCat found successfully {} ", owner);
            return OwnerCatMapper.toDto(owner);
        } else {
            String errorMessage = String.format("The OwnerCat with id %d not found", ownerId);
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }
    }

    @Override
    public Boolean findByChatId(Long chatId) {
        Optional<OwnerCat> ownerCatOptional = ownersCatsRepository.findByChatId(chatId);
        return ownerCatOptional.isPresent();
    }

    @Override
    public List<OwnerCatDto> findByUsername(String username) {
        List<OwnerCat> ownerList = ownersCatsRepository.findByUserNameContainingIgnoreCase(username);
        if (ownerList.isEmpty()) {
            String errorMessage = "No one OwnerCat found";
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        } else {
            log.info("OwnerCats found");
            return ownerList.stream()
                    .map(OwnerCatMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<OwnerCatDto> findBySurname(String surname) {
        List<OwnerCat> ownerList = ownersCatsRepository.findBySurnameContainingIgnoreCase(surname);
        if (ownerList.isEmpty()) {
            String errorMessage = "No one OwnerCats found";
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        } else {
            log.info("OwnerCats found");
            return ownerList.stream()
                    .map(OwnerCatMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<OwnerCatDto> findByPhoneNumber(String telephone) {
        List<OwnerCat> ownerList = ownersCatsRepository.findByTelephoneContainingIgnoreCase(telephone);
        if (ownerList.isEmpty()) {
            String errorMessage = "No one OwnerCats found";
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        } else {
            log.info("OwnerCats found");
            return ownerList.stream()
                    .map(OwnerCatMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<OwnerCatDto> getAll() {
        List<OwnerCat> ownerList = ownersCatsRepository.findAll();
        if (ownerList.isEmpty()) {
            String errorMessage = "No one OwnerCats found";
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        } else {
            log.info("OwnerCats found");
            return ownerList.stream()
                    .map(OwnerCatMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (ownersCatsRepository.existsById(id)) {
            ownersCatsRepository.deleteById(id);
            log.info("OwnerCat deleted successfully");
        } else {
            String errorMessage = String.format("The OwnerCat with id %d not found", id);
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }
    }

    @Override
    public OwnerCatDto findCatsVolunteer() {
        Optional<OwnerCat> volunteer = ownersCatsRepository.findVolunteerByVolunteerIsTrueAndNoChatsOpened();
        if (volunteer.isPresent()) {
            OwnerCat owner = volunteer.get();
            log.info("Free Cat volunteer found successfully");
            return OwnerCatMapper.toDto(owner);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public void setPetsToOwnerAndDateOfIncome(
            Long ownerId,
            List<Long> petsIds,
            LocalDate dateStart
    ) {
        OwnerCatDto ownerDto = findById(ownerId);
        List<Cat> pets = catsRepository.findAllById(petsIds);
        if (pets.isEmpty()) {
            throw new NotFoundException("No one pet found");
        } else {
            List<CatDto> petsDto = pets.stream().map(CatsMapper::toDto).collect(Collectors.toList());
            ownerDto.setDateIncome(dateStart);
            ownerDto.setUpdatedAt(LocalDateTime.now());
            ownerDto.setBecameClient(true);
            ownerDto.setCatsDto(petsDto);

            OwnerCat owner = OwnerCatMapper.toEntity(ownerDto);
            ownersCatsRepository.save(owner);
            log.info("Cat set to owner and probation period started");
        }
    }

    @Override
    @Transactional
    public void setOwnerDateOfOutcome(Long ownerId, LocalDate dateEnds) {
        OwnerCatDto ownerDto = findById(ownerId);
        ownerDto.setUpdatedAt(LocalDateTime.now());
        ownerDto.setDateOutcome(dateEnds);
        OwnerCat owner = OwnerCatMapper.toEntity(ownerDto);
        ownersCatsRepository.save(owner);
        log.info("Day of outcome set to owner");
    }

}