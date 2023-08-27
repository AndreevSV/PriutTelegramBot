package omg.group.priuttelegrambot.handlers.flags;

import omg.group.priuttelegrambot.entity.flags.OwnersDogsFlags;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.entity.pets.Dog;

public interface OwnersDogsFlagsHandler {


    void createWaitingForNewReportFlag(OwnerDog owner, Dog pet);

    void createWaitingForPhotoFlag(OwnerDog owner, Dog pet);

    void createWaitingForRationFlag(OwnerDog owner, Dog pet);

    void createWaitingForFeelingFlag(OwnerDog owner, Dog pet);

    void createWaitingForChangesFlag(OwnerDog owner, Dog pet);

    void createWaitingForContactsFlag(OwnerDog owner);

    void createChattingFlag(OwnerDog owner, OwnerDog volunteer);

    OwnersDogsFlags findOwnersDogsFlagsByOwner(OwnerDog owner);

    OwnersDogsFlags findOwnersDogsFlagsByVolunteer(OwnerDog owner);

    void removeFlag(OwnersDogsFlags flag);
}
