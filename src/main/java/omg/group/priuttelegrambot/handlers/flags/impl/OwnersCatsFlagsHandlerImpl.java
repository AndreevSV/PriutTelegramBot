package omg.group.priuttelegrambot.handlers.flags.impl;

import omg.group.priuttelegrambot.dto.flags.OwnersCatsFlagsDto;
import omg.group.priuttelegrambot.dto.flags.OwnersCatsFlagsMapper;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.dto.owners.OwnerCatMapper;
import omg.group.priuttelegrambot.dto.pets.CatDto;
import omg.group.priuttelegrambot.entity.flags.OwnersCatsFlags;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.handlers.flags.OwnersCatsFlagsHandler;
import omg.group.priuttelegrambot.repository.flags.OwnersCatsFlagsRepository;
import omg.group.priuttelegrambot.repository.owners.OwnersCatsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class OwnersCatsFlagsHandlerImpl implements OwnersCatsFlagsHandler {

    private final OwnersCatsFlagsRepository ownersCatsFlagsRepository;
    private final OwnersCatsRepository ownersCatsRepository;

    public OwnersCatsFlagsHandlerImpl(OwnersCatsFlagsRepository ownersCatsFlagsRepository, OwnersCatsRepository ownersCatsRepository) {
        this.ownersCatsFlagsRepository = ownersCatsFlagsRepository;
        this.ownersCatsRepository = ownersCatsRepository;
    }

    @Override
    public void createWaitingForNewReportFlag(OwnerCatDto ownerDto, CatDto petDto) {

        OwnersCatsFlagsDto flagDto = new OwnersCatsFlagsDto();
        flagDto.setOwnerDto(ownerDto);
        flagDto.setCatDto(petDto);
        flagDto.setIsWaitingForPhoto(true);
        flagDto.setIsWaitingForRation(true);
        flagDto.setIsWaitingForFeeling(true);
        flagDto.setIsWaitingForChanges(true);
        flagDto.setIsWaitingForContacts(false);
        flagDto.setIsChatting(false);
        flagDto.setCreatedAt(LocalDateTime.now());
        flagDto.setDate(LocalDate.now());
        OwnersCatsFlags flag = OwnersCatsFlagsMapper.toEntity(flagDto);
        ownersCatsFlagsRepository.save(flag);
    }

    @Override
    public void createWaitingForPhotoFlag(OwnerCatDto ownerDto, CatDto petDto) {

        OwnersCatsFlagsDto flagDto = new OwnersCatsFlagsDto();
        flagDto.setOwnerDto(ownerDto);
        flagDto.setCatDto(petDto);
        flagDto.setIsWaitingForPhoto(true);
        flagDto.setIsWaitingForRation(false);
        flagDto.setIsWaitingForFeeling(false);
        flagDto.setIsWaitingForChanges(false);
        flagDto.setIsWaitingForContacts(false);
        flagDto.setIsChatting(false);
        flagDto.setCreatedAt(LocalDateTime.now());
        flagDto.setDate(LocalDate.now());
        OwnersCatsFlags flag = OwnersCatsFlagsMapper.toEntity(flagDto);
        ownersCatsFlagsRepository.save(flag);
    }

    @Override
    public void createWaitingForRationFlag(OwnerCatDto ownerDto, CatDto petDto) {

        OwnersCatsFlagsDto flagDto = new OwnersCatsFlagsDto();
        flagDto.setOwnerDto(ownerDto);
        flagDto.setCatDto(petDto);
        flagDto.setIsWaitingForPhoto(false);
        flagDto.setIsWaitingForRation(true);
        flagDto.setIsWaitingForFeeling(false);
        flagDto.setIsWaitingForChanges(false);
        flagDto.setIsWaitingForContacts(false);
        flagDto.setIsChatting(false);
        flagDto.setCreatedAt(LocalDateTime.now());
        flagDto.setDate(LocalDate.now());
        OwnersCatsFlags flag = OwnersCatsFlagsMapper.toEntity(flagDto);
        ownersCatsFlagsRepository.save(flag);
    }

    @Override
    public void createWaitingForFeelingFlag(OwnerCatDto ownerDto, CatDto petDto) {

        OwnersCatsFlagsDto flagDto = new OwnersCatsFlagsDto();
        flagDto.setOwnerDto(ownerDto);
        flagDto.setCatDto(petDto);
        flagDto.setIsWaitingForPhoto(false);
        flagDto.setIsWaitingForRation(false);
        flagDto.setIsWaitingForFeeling(true);
        flagDto.setIsWaitingForChanges(false);
        flagDto.setIsWaitingForContacts(false);
        flagDto.setIsChatting(false);
        flagDto.setCreatedAt(LocalDateTime.now());
        flagDto.setDate(LocalDate.now());
        OwnersCatsFlags flag = OwnersCatsFlagsMapper.toEntity(flagDto);
        ownersCatsFlagsRepository.save(flag);
    }

    @Override
    public void createWaitingForChangesFlag(OwnerCatDto ownerDto, CatDto petDto) {

        OwnersCatsFlagsDto flagDto = new OwnersCatsFlagsDto();
        flagDto.setOwnerDto(ownerDto);
        flagDto.setCatDto(petDto);
        flagDto.setIsWaitingForPhoto(false);
        flagDto.setIsWaitingForRation(false);
        flagDto.setIsWaitingForFeeling(false);
        flagDto.setIsWaitingForChanges(true);
        flagDto.setIsWaitingForContacts(false);
        flagDto.setIsChatting(false);
        flagDto.setCreatedAt(LocalDateTime.now());
        flagDto.setDate(LocalDate.now());
        OwnersCatsFlags flag = OwnersCatsFlagsMapper.toEntity(flagDto);
        ownersCatsFlagsRepository.save(flag);
    }

    @Override
    public void createWaitingForContactsFlag(OwnerCatDto ownerDto) {

        OwnersCatsFlagsDto flagDto = new OwnersCatsFlagsDto();
        flagDto.setOwnerDto(ownerDto);
        flagDto.setIsWaitingForPhoto(false);
        flagDto.setIsWaitingForRation(false);
        flagDto.setIsWaitingForFeeling(false);
        flagDto.setIsWaitingForChanges(false);
        flagDto.setIsWaitingForContacts(true);
        flagDto.setIsChatting(false);
        flagDto.setCreatedAt(LocalDateTime.now());
        flagDto.setDate(LocalDate.now());
        OwnersCatsFlags flag = OwnersCatsFlagsMapper.toEntity(flagDto);
        ownersCatsFlagsRepository.save(flag);
    }

    @Override
    public void createChattingFlag(OwnerCatDto ownerDto, OwnerCatDto volunteerDto) {

        OwnersCatsFlagsDto flagDto = new OwnersCatsFlagsDto();
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
        OwnersCatsFlags flag = OwnersCatsFlagsMapper.toEntity(flagDto);
        ownersCatsFlagsRepository.save(flag);
    }

    @Override
    public OwnersCatsFlagsDto findFlagByOwner(OwnerCatDto ownerDto) {
        OwnerCat owner = OwnerCatMapper.toEntity(ownerDto);
        Optional<OwnersCatsFlags> ownersCatsFlagOptional = ownersCatsFlagsRepository.findByOwner(owner);
        if (ownersCatsFlagOptional.isPresent()) {
            OwnersCatsFlags flag = ownersCatsFlagOptional.get();
            return OwnersCatsFlagsMapper.toDto(flag);
        } else {
            return null;
        }
    }

    @Override
    public OwnersCatsFlagsDto findFlagByVolunteer(OwnerCatDto ownerDto) {
        OwnerCat volunteer = OwnerCatMapper.toEntity(ownerDto);
        Optional<OwnersCatsFlags> volunteerCatsFlagOptional = ownersCatsFlagsRepository.findByVolunteer(volunteer);
        if (volunteerCatsFlagOptional.isPresent()) {
            OwnersCatsFlags flag = volunteerCatsFlagOptional.get();
            return OwnersCatsFlagsMapper.toDto(flag);
        } else {
            return null;
        }
    }
    @Override
    public OwnersCatsFlagsDto findFlagByOwnerAndIsWaitingForReportIsTrue(OwnerCatDto ownerDto) {
        OwnerCat owner = OwnerCatMapper.toEntity(ownerDto);
        Optional<OwnersCatsFlags> volunteerCatsFlagOptional = ownersCatsFlagsRepository.findByOwnerAndIsWaitingForPhotoIsTrueOrIsWaitingForFeelingIsTrueOrIsWaitingForRationIsTrueOrIsWaitingForChangesIsTrue(owner);
        if (volunteerCatsFlagOptional.isPresent()) {
            OwnersCatsFlags flag = volunteerCatsFlagOptional.get();
            return OwnersCatsFlagsMapper.toDto(flag);
        } else {
            return null;
        }
    }

    @Override
    public OwnersCatsFlagsDto findFlagByOwnerAndIsChattingIsTrue(OwnerCatDto ownerDto) {
        OwnerCat owner = OwnerCatMapper.toEntity(ownerDto);
        Optional<OwnersCatsFlags> flagOptional = ownersCatsFlagsRepository.findByOwnerAndIsChattingIsTrue(owner);
        if (flagOptional.isPresent()) {
            OwnersCatsFlags flag = flagOptional.get();
            return OwnersCatsFlagsMapper.toDto(flag);
        } else {
            return null;
        }
    }

    @Override
    public void removeFlag(OwnersCatsFlagsDto flagDto) {
        OwnersCatsFlags flag = OwnersCatsFlagsMapper.toEntity(flagDto);
        ownersCatsFlagsRepository.delete(flag);
    }

}
