package omg.group.priuttelegrambot.handlers.flags.impl;

import omg.group.priuttelegrambot.entity.flags.OwnersDogsFlags;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.entity.pets.Dog;
import omg.group.priuttelegrambot.handlers.flags.OwnersDogsFlagsHandler;
import omg.group.priuttelegrambot.handlers.updates.OwnUpdatesHandler;
import omg.group.priuttelegrambot.repository.flags.OwnersDogsFlagsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class OwnersDogsFlagsHandlerImpl implements OwnersDogsFlagsHandler {

    private final OwnersDogsFlagsRepository ownersDogsFlagsRepository;
//    private final OwnUpdatesHandler ownUpdatesHandler;

    public OwnersDogsFlagsHandlerImpl(OwnersDogsFlagsRepository ownersDogsFlagsRepository) {
        this.ownersDogsFlagsRepository = ownersDogsFlagsRepository;
    }

    @Override
    public void createWaitingForNewReportFlag(OwnerDog owner, Dog pet) {

        OwnersDogsFlags flag = new OwnersDogsFlags();
        flag.setOwner(owner);
        flag.setDog(pet);
        flag.setWaitingForPhoto(true);
        flag.setWaitingForRation(true);
        flag.setWaitingForFeeling(true);
        flag.setWaitingForChanges(true);
        flag.setWaitingForContacts(false);
        flag.setChatting(false);
        flag.setCreatedAt(LocalDateTime.now());
        flag.setDate(LocalDate.now());
        ownersDogsFlagsRepository.save(flag);
    }

    @Override
    public void createWaitingForPhotoFlag(OwnerDog owner, Dog pet) {

        OwnersDogsFlags flag = new OwnersDogsFlags();
        flag.setOwner(owner);
        flag.setDog(pet);
        flag.setWaitingForPhoto(true);
        flag.setWaitingForRation(false);
        flag.setWaitingForFeeling(false);
        flag.setWaitingForChanges(false);
        flag.setWaitingForContacts(false);
        flag.setChatting(false);
        flag.setCreatedAt(LocalDateTime.now());
        flag.setDate(LocalDate.now());
        ownersDogsFlagsRepository.save(flag);
    }

    @Override
    public void createWaitingForRationFlag(OwnerDog owner, Dog pet) {

        OwnersDogsFlags flag = new OwnersDogsFlags();
        flag.setOwner(owner);
        flag.setDog(pet);
        flag.setWaitingForPhoto(false);
        flag.setWaitingForRation(true);
        flag.setWaitingForFeeling(false);
        flag.setWaitingForChanges(false);
        flag.setWaitingForContacts(false);
        flag.setChatting(false);
        flag.setCreatedAt(LocalDateTime.now());
        flag.setDate(LocalDate.now());
        ownersDogsFlagsRepository.save(flag);
    }

    @Override
    public void createWaitingForFeelingFlag(OwnerDog owner, Dog pet) {

        OwnersDogsFlags flag = new OwnersDogsFlags();
        flag.setOwner(owner);
        flag.setDog(pet);
        flag.setWaitingForPhoto(false);
        flag.setWaitingForRation(false);
        flag.setWaitingForFeeling(true);
        flag.setWaitingForChanges(false);
        flag.setWaitingForContacts(false);
        flag.setChatting(false);
        flag.setCreatedAt(LocalDateTime.now());
        flag.setDate(LocalDate.now());
        ownersDogsFlagsRepository.save(flag);
    }

    @Override
    public void createWaitingForChangesFlag(OwnerDog owner, Dog pet) {

        OwnersDogsFlags flag = new OwnersDogsFlags();
        flag.setOwner(owner);
        flag.setDog(pet);
        flag.setWaitingForPhoto(false);
        flag.setWaitingForRation(false);
        flag.setWaitingForFeeling(false);
        flag.setWaitingForChanges(true);
        flag.setWaitingForContacts(false);
        flag.setChatting(false);
        flag.setCreatedAt(LocalDateTime.now());
        flag.setDate(LocalDate.now());
        ownersDogsFlagsRepository.save(flag);
    }

    @Override
    public void createWaitingForContactsFlag(OwnerDog owner) {

        OwnersDogsFlags flag = new OwnersDogsFlags();
        flag.setOwner(owner);
        flag.setVolunteer(null);
        flag.setDog(null);
        flag.setWaitingForPhoto(false);
        flag.setWaitingForRation(false);
        flag.setWaitingForFeeling(false);
        flag.setWaitingForChanges(false);
        flag.setWaitingForContacts(true);
        flag.setChatting(false);
        flag.setCreatedAt(LocalDateTime.now());
        flag.setDate(LocalDate.now());
        ownersDogsFlagsRepository.save(flag);
    }

    @Override
    public void createChattingFlag(OwnerDog owner, OwnerDog volunteer) {

        OwnersDogsFlags flag = new OwnersDogsFlags();
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
        ownersDogsFlagsRepository.save(flag);
    }

    @Override
    public OwnersDogsFlags findOwnersDogsFlagsByOwner(OwnerDog owner) {
        return ownersDogsFlagsRepository.findOwnersDogsFlagsByOwner(owner).orElse(null);
    }

    @Override
    public OwnersDogsFlags findOwnersDogsFlagsByVolunteer(OwnerDog owner) {
        return ownersDogsFlagsRepository.findOwnersDogsFlagsByVolunteer(owner).orElse(null);
    }

    @Override
    public void removeFlag(OwnersDogsFlags flag) {
        ownersDogsFlagsRepository.delete(flag);
    }


}
