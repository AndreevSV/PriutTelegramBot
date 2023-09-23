package omg.group.priuttelegrambot.handlers.flags.impl;

import omg.group.priuttelegrambot.dto.flags.OwnersDogsFlagsDto;
import omg.group.priuttelegrambot.dto.flags.OwnersDogsFlagsMapper;
import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;
import omg.group.priuttelegrambot.dto.owners.OwnerDogMapper;
import omg.group.priuttelegrambot.dto.pets.DogDto;
import omg.group.priuttelegrambot.entity.flags.OwnersDogsFlags;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.handlers.flags.OwnersDogsFlagsHandler;
import omg.group.priuttelegrambot.repository.flags.OwnersDogsFlagsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OwnersDogsFlagsHandlerImpl implements OwnersDogsFlagsHandler {

    private final OwnersDogsFlagsRepository ownersDogsFlagsRepository;

    public OwnersDogsFlagsHandlerImpl(OwnersDogsFlagsRepository ownersDogsFlagsRepository) {
        this.ownersDogsFlagsRepository = ownersDogsFlagsRepository;
    }

    @Override
    @Transactional
    public void createWaitingForNewReportFlag(OwnerDogDto ownerDto, DogDto petDto) {
        OwnersDogsFlagsDto flagDto = OwnersDogsFlagsDto.builder()
                .ownerDto(ownerDto)
                .dogDto(petDto)
                .waitingForPhoto(true)
                .waitingForRation(true)
                .waitingForFeeling(true)
                .waitingForChanges(true)
                .createdAt(LocalDateTime.now())
                .date(LocalDate.now())
                .build();
        OwnersDogsFlags flag = OwnersDogsFlagsMapper.toEntity(flagDto);
        ownersDogsFlagsRepository.save(flag);
    }

    @Override
    @Transactional
    public void createWaitingForPhotoFlag(OwnerDogDto ownerDto, DogDto petDto) {
        OwnersDogsFlagsDto flagDto = OwnersDogsFlagsDto.builder()
                .ownerDto(ownerDto)
                .dogDto(petDto)
                .waitingForPhoto(true)
                .createdAt(LocalDateTime.now())
                .date(LocalDate.now())
                .build();
        OwnersDogsFlags flag = OwnersDogsFlagsMapper.toEntity(flagDto);
        ownersDogsFlagsRepository.save(flag);
    }

    @Override
    @Transactional
    public void createWaitingForRationFlag(OwnerDogDto ownerDto, DogDto petDto) {
        OwnersDogsFlagsDto flagDto = OwnersDogsFlagsDto.builder()
                .ownerDto(ownerDto)
                .dogDto(petDto)
                .waitingForRation(true)
                .createdAt(LocalDateTime.now())
                .date(LocalDate.now())
                .build();
        OwnersDogsFlags flag = OwnersDogsFlagsMapper.toEntity(flagDto);
        ownersDogsFlagsRepository.save(flag);
    }

    @Override
    @Transactional
    public void createWaitingForFeelingFlag(OwnerDogDto ownerDto, DogDto petDto) {
        OwnersDogsFlagsDto flagDto = OwnersDogsFlagsDto.builder()
                .ownerDto(ownerDto)
                .dogDto(petDto)
                .waitingForFeeling(true)
                .createdAt(LocalDateTime.now())
                .date(LocalDate.now())
                .build();
        OwnersDogsFlags flag = OwnersDogsFlagsMapper.toEntity(flagDto);
        ownersDogsFlagsRepository.save(flag);
    }

    @Override
    @Transactional
    public void createWaitingForChangesFlag(OwnerDogDto ownerDto, DogDto petDto) {
        OwnersDogsFlagsDto flagDto = OwnersDogsFlagsDto.builder()
                .ownerDto(ownerDto)
                .dogDto(petDto)
                .waitingForChanges(true)
                .createdAt(LocalDateTime.now())
                .date(LocalDate.now())
                .build();
        OwnersDogsFlags flag = OwnersDogsFlagsMapper.toEntity(flagDto);
        ownersDogsFlagsRepository.save(flag);
    }

    @Override
    @Transactional
    public void createWaitingForContactsFlag(OwnerDogDto ownerDto) {
        OwnersDogsFlagsDto flagDto = OwnersDogsFlagsDto.builder()
                .ownerDto(ownerDto)
                .waitingForContacts(true)
                .createdAt(LocalDateTime.now())
                .date(LocalDate.now())
                .build();
        OwnersDogsFlags flag = OwnersDogsFlagsMapper.toEntity(flagDto);
        ownersDogsFlagsRepository.save(flag);
    }

    @Override
    @Transactional
    public void createChattingFlag(OwnerDogDto ownerDto, OwnerDogDto volunteerDto) {
        OwnersDogsFlagsDto flagDto = OwnersDogsFlagsDto.builder()
                .ownerDto(ownerDto)
                .volunteerDto(volunteerDto)
                .chatting(true)
                .createdAt(LocalDateTime.now())
                .date(LocalDate.now())
                .build();
        OwnersDogsFlags flag = OwnersDogsFlagsMapper.toEntity(flagDto);
        ownersDogsFlagsRepository.save(flag);
    }

    @Override
    public OwnersDogsFlagsDto findFlagByOwner(OwnerDogDto ownerDto) {
        OwnerDog owner = OwnerDogMapper.toEntity(ownerDto);
        Optional<OwnersDogsFlags> ownersDogsFlagsOptional = ownersDogsFlagsRepository.findByOwner(owner);
        if (ownersDogsFlagsOptional.isPresent()) {
            OwnersDogsFlags flag = ownersDogsFlagsOptional.get();
            return OwnersDogsFlagsMapper.toDto(flag);
        } else {
            return null;
        }
    }

    @Override
    public OwnersDogsFlagsDto findFlagByOwnerAndIsWaitingForReportIsTrue(OwnerDogDto ownerDto) {
        OwnerDog owner = OwnerDogMapper.toEntity(ownerDto);
        Optional<OwnersDogsFlags> ownersDogsFlagsOptional = ownersDogsFlagsRepository.findByOwnerAndIsWaitingForPhotoIsTrueOrIsWaitingForFeelingIsTrueOrIsWaitingForRationIsTrueOrIsWaitingForChangesIsTrue(owner);
        if (ownersDogsFlagsOptional.isPresent()) {
            OwnersDogsFlags flag = ownersDogsFlagsOptional.get();
            return OwnersDogsFlagsMapper.toDto(flag);
        } else {
            return null;
        }
    }

    @Override
    public OwnersDogsFlagsDto findFlagByVolunteer(OwnerDogDto ownerDto) {
        OwnerDog owner = OwnerDogMapper.toEntity(ownerDto);
        Optional<OwnersDogsFlags> volunteerDogsFlagsOptional = ownersDogsFlagsRepository.findByVolunteer(owner);
        if (volunteerDogsFlagsOptional.isPresent()) {
            OwnersDogsFlags flag = volunteerDogsFlagsOptional.get();
            return OwnersDogsFlagsMapper.toDto(flag);
        } else {
            return null;
        }
    }

    @Override
    public OwnersDogsFlagsDto findFlagByOwnerAndIsChattingIsTrue(OwnerDogDto ownerDto) {
        OwnerDog owner = OwnerDogMapper.toEntity(ownerDto);
        Optional<OwnersDogsFlags> flagOptional = ownersDogsFlagsRepository.findByOwnerAndIsChattingIsTrue(owner);
        if (flagOptional.isPresent()) {
            OwnersDogsFlags flag = flagOptional.get();
            return OwnersDogsFlagsMapper.toDto(flag);
        } else {
            return null;
        }
    }


    @Override
    @Transactional
    public void removeFlag(OwnersDogsFlagsDto flagDto) {
        OwnersDogsFlags flag = OwnersDogsFlagsMapper.toEntity(flagDto);
        ownersDogsFlagsRepository.delete(flag);
    }
}
