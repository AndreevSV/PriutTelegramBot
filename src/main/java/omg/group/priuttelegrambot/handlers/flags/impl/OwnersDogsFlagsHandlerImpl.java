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
    public void createWaitingForNewReportFlag(OwnerDogDto ownerDto, DogDto petDto) {
        OwnersDogsFlagsDto flagDto = new OwnersDogsFlagsDto();
        flagDto.setOwnerDto(ownerDto);
        flagDto.setDogDto(petDto);
        flagDto.setIsWaitingForPhoto(true);
        flagDto.setIsWaitingForRation(true);
        flagDto.setIsWaitingForFeeling(true);
        flagDto.setIsWaitingForChanges(true);
        flagDto.setIsWaitingForContacts(false);
        flagDto.setIsChatting(false);
        flagDto.setCreatedAt(LocalDateTime.now());
        flagDto.setDate(LocalDate.now());
        OwnersDogsFlags flag = OwnersDogsFlagsMapper.toEntity(flagDto);
        ownersDogsFlagsRepository.save(flag);
    }

    @Override
    public void createWaitingForPhotoFlag(OwnerDogDto ownerDto, DogDto petDto) {
        OwnersDogsFlagsDto flagDto = new OwnersDogsFlagsDto();
        flagDto.setOwnerDto(ownerDto);
        flagDto.setDogDto(petDto);
        flagDto.setIsWaitingForPhoto(true);
        flagDto.setIsWaitingForRation(false);
        flagDto.setIsWaitingForFeeling(false);
        flagDto.setIsWaitingForChanges(false);
        flagDto.setIsWaitingForContacts(false);
        flagDto.setIsChatting(false);
        flagDto.setCreatedAt(LocalDateTime.now());
        flagDto.setDate(LocalDate.now());
        OwnersDogsFlags flag = OwnersDogsFlagsMapper.toEntity(flagDto);
        ownersDogsFlagsRepository.save(flag);
    }

    @Override
    public void createWaitingForRationFlag(OwnerDogDto ownerDto, DogDto petDto) {
        OwnersDogsFlagsDto flagDto = new OwnersDogsFlagsDto();
        flagDto.setOwnerDto(ownerDto);
        flagDto.setDogDto(petDto);
        flagDto.setIsWaitingForPhoto(false);
        flagDto.setIsWaitingForRation(true);
        flagDto.setIsWaitingForFeeling(false);
        flagDto.setIsWaitingForChanges(false);
        flagDto.setIsWaitingForContacts(false);
        flagDto.setIsChatting(false);
        flagDto.setCreatedAt(LocalDateTime.now());
        flagDto.setDate(LocalDate.now());
        OwnersDogsFlags flag = OwnersDogsFlagsMapper.toEntity(flagDto);
        ownersDogsFlagsRepository.save(flag);
    }

    @Override
    public void createWaitingForFeelingFlag(OwnerDogDto ownerDto, DogDto petDto) {
        OwnersDogsFlagsDto flagDto = new OwnersDogsFlagsDto();
        flagDto.setOwnerDto(ownerDto);
        flagDto.setDogDto(petDto);
        flagDto.setIsWaitingForPhoto(false);
        flagDto.setIsWaitingForRation(false);
        flagDto.setIsWaitingForFeeling(true);
        flagDto.setIsWaitingForChanges(false);
        flagDto.setIsWaitingForContacts(false);
        flagDto.setIsChatting(false);
        flagDto.setCreatedAt(LocalDateTime.now());
        flagDto.setDate(LocalDate.now());
        OwnersDogsFlags flag = OwnersDogsFlagsMapper.toEntity(flagDto);
        ownersDogsFlagsRepository.save(flag);
    }

    @Override
    public void createWaitingForChangesFlag(OwnerDogDto ownerDto, DogDto petDto) {
        OwnersDogsFlagsDto flagDto = new OwnersDogsFlagsDto();
        flagDto.setOwnerDto(ownerDto);
        flagDto.setDogDto(petDto);
        flagDto.setIsWaitingForPhoto(false);
        flagDto.setIsWaitingForRation(false);
        flagDto.setIsWaitingForFeeling(false);
        flagDto.setIsWaitingForChanges(true);
        flagDto.setIsWaitingForContacts(false);
        flagDto.setIsChatting(false);
        flagDto.setCreatedAt(LocalDateTime.now());
        flagDto.setDate(LocalDate.now());
        OwnersDogsFlags flag = OwnersDogsFlagsMapper.toEntity(flagDto);
        ownersDogsFlagsRepository.save(flag);
    }

    @Override
    public void createWaitingForContactsFlag(OwnerDogDto ownerDto) {
        OwnersDogsFlagsDto flagDto = new OwnersDogsFlagsDto();
        flagDto.setOwnerDto(ownerDto);
        flagDto.setIsWaitingForPhoto(false);
        flagDto.setIsWaitingForRation(false);
        flagDto.setIsWaitingForFeeling(false);
        flagDto.setIsWaitingForChanges(false);
        flagDto.setIsWaitingForContacts(true);
        flagDto.setIsChatting(false);
        flagDto.setCreatedAt(LocalDateTime.now());
        flagDto.setDate(LocalDate.now());
        OwnersDogsFlags flag = OwnersDogsFlagsMapper.toEntity(flagDto);
        ownersDogsFlagsRepository.save(flag);
    }

    @Override
    public void createChattingFlag(OwnerDogDto ownerDto, OwnerDogDto volunteerDto) {
        OwnersDogsFlagsDto flagDto = new OwnersDogsFlagsDto();
        flagDto.setOwnerDto(ownerDto);
        flagDto.setVolunteerDto(volunteerDto);
        flagDto.setIsWaitingForPhoto(false);
        flagDto.setIsWaitingForRation(false);
        flagDto.setIsWaitingForFeeling(false);
        flagDto.setIsWaitingForChanges(false);
        flagDto.setIsWaitingForContacts(false);
        flagDto.setIsChatting(true);
        flagDto.setCreatedAt(LocalDateTime.now());
        flagDto.setDate(LocalDate.now());
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
    public void removeFlag(OwnersDogsFlagsDto flagDto) {
        OwnersDogsFlags flag = OwnersDogsFlagsMapper.toEntity(flagDto);
        ownersDogsFlagsRepository.delete(flag);
    }
}
