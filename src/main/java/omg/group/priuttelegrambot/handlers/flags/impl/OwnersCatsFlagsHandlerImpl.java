package omg.group.priuttelegrambot.handlers.flags.impl;

import omg.group.priuttelegrambot.entity.flags.OwnersCatsFlags;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.entity.pets.Cat;
import omg.group.priuttelegrambot.handlers.flags.OwnersCatsFlagsHandler;
import omg.group.priuttelegrambot.repository.flags.OwnersCatsFlagsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Service
public class OwnersCatsFlagsHandlerImpl implements OwnersCatsFlagsHandler {

    private final OwnersCatsFlagsRepository ownersCatsFlagsRepository;

    public OwnersCatsFlagsHandlerImpl(OwnersCatsFlagsRepository ownersCatsFlagsRepository) {
        this.ownersCatsFlagsRepository = ownersCatsFlagsRepository;
    }

    @Override
    public void createWaitingForNewReportFlag(OwnerCat owner, Cat pet) {

        OwnersCatsFlags flag = new OwnersCatsFlags();
        flag.setOwner(owner);
        flag.setCat(pet);
        flag.setWaitingForPhoto(true);
        flag.setWaitingForRation(true);
        flag.setWaitingForFeeling(true);
        flag.setWaitingForChanges(true);
        flag.setWaitingForContacts(false);
        flag.setChatting(false);
        flag.setCreatedAt(LocalDateTime.now());
        flag.setDate(LocalDate.now());
        ownersCatsFlagsRepository.save(flag);
    }

    @Override
    public void createWaitingForPhotoFlag(OwnerCat owner, Cat pet) {

        OwnersCatsFlags flag = new OwnersCatsFlags();
        flag.setOwner(owner);
        flag.setCat(pet);
        flag.setWaitingForPhoto(true);
        flag.setWaitingForRation(false);
        flag.setWaitingForFeeling(false);
        flag.setWaitingForChanges(false);
        flag.setWaitingForContacts(false);
        flag.setChatting(false);
        flag.setCreatedAt(LocalDateTime.now());
        flag.setDate(LocalDate.now());
        ownersCatsFlagsRepository.save(flag);
    }

    @Override
    public void createWaitingForRationFlag(OwnerCat owner, Cat pet) {

        OwnersCatsFlags flag = new OwnersCatsFlags();
        flag.setOwner(owner);
        flag.setCat(pet);
        flag.setWaitingForPhoto(false);
        flag.setWaitingForRation(true);
        flag.setWaitingForFeeling(false);
        flag.setWaitingForChanges(false);
        flag.setWaitingForContacts(false);
        flag.setChatting(false);
        flag.setCreatedAt(LocalDateTime.now());
        flag.setDate(LocalDate.now());
        ownersCatsFlagsRepository.save(flag);
    }

    @Override
    public void createWaitingForFeelingFlag(OwnerCat owner, Cat pet) {

        OwnersCatsFlags flag = new OwnersCatsFlags();
        flag.setOwner(owner);
        flag.setCat(pet);
        flag.setWaitingForPhoto(false);
        flag.setWaitingForRation(false);
        flag.setWaitingForFeeling(true);
        flag.setWaitingForChanges(false);
        flag.setWaitingForContacts(false);
        flag.setChatting(false);
        flag.setCreatedAt(LocalDateTime.now());
        flag.setDate(LocalDate.now());
        ownersCatsFlagsRepository.save(flag);
    }

    @Override
    public void createWaitingForChangesFlag(OwnerCat owner, Cat pet) {

        OwnersCatsFlags flag = new OwnersCatsFlags();
        flag.setOwner(owner);
        flag.setCat(pet);
        flag.setWaitingForPhoto(false);
        flag.setWaitingForRation(false);
        flag.setWaitingForFeeling(false);
        flag.setWaitingForChanges(true);
        flag.setWaitingForContacts(false);
        flag.setChatting(false);
        flag.setCreatedAt(LocalDateTime.now());
        flag.setDate(LocalDate.now());
        ownersCatsFlagsRepository.save(flag);
    }

    @Override
    public void createWaitingForContactsFlag(OwnerCat owner) {

        OwnersCatsFlags flag = new OwnersCatsFlags();
        flag.setOwner(owner);
        flag.setVolunteer(null);
        flag.setCat(null);
        flag.setWaitingForPhoto(false);
        flag.setWaitingForRation(false);
        flag.setWaitingForFeeling(false);
        flag.setWaitingForChanges(false);
        flag.setWaitingForContacts(true);
        flag.setChatting(false);
        flag.setCreatedAt(LocalDateTime.now());
        flag.setDate(LocalDate.now());
        ownersCatsFlagsRepository.save(flag);
    }

    @Override
    public void createChattingFlag(OwnerCat owner, OwnerCat volunteer) {

        OwnersCatsFlags flag = new OwnersCatsFlags();
        flag.setOwner(owner);
        flag.setVolunteer(volunteer);
        flag.setWaitingForPhoto(false);
        flag.setWaitingForRation(false);
        flag.setWaitingForFeeling(false);
        flag.setWaitingForChanges(false);
        flag.setWaitingForContacts(false);
        flag.setChatting(true);
        flag.setCreatedAt(LocalDateTime.now());
        flag.setDate(LocalDate.now());
        ownersCatsFlagsRepository.save(flag);
    }

    @Override
    public OwnersCatsFlags findOwnersCatsFlagsByOwner(OwnerCat owner) {
        return ownersCatsFlagsRepository.findOwnersCatsFlagsByOwner(owner).orElse(null);
    }

    @Override
    public OwnersCatsFlags findOwnersCatsFlagsByVolunteer(OwnerCat owner) {
        return ownersCatsFlagsRepository.findOwnersCatsFlagsByVolunteer(owner).orElse(null);
    }

    @Override
    public void removeFlag(OwnersCatsFlags flag) {
        ownersCatsFlagsRepository.delete(flag);
    }

}
