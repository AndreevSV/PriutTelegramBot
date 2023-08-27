package omg.group.priuttelegrambot.handlers.flags;

import omg.group.priuttelegrambot.entity.flags.OwnersCatsFlags;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.entity.pets.Cat;


public interface OwnersCatsFlagsHandler {

    void createWaitingForNewReportFlag(OwnerCat owner, Cat pet);

    void createWaitingForPhotoFlag(OwnerCat owner, Cat pet);

    void createWaitingForRationFlag(OwnerCat owner, Cat pet);

    void createWaitingForFeelingFlag(OwnerCat owner, Cat pet);

    void createWaitingForChangesFlag(OwnerCat owner, Cat pet);

    void createWaitingForContactsFlag(OwnerCat owner);

    void createChattingFlag(OwnerCat owner, OwnerCat volunteer);

    OwnersCatsFlags findOwnersCatsFlagsByOwner(OwnerCat owner);

    OwnersCatsFlags findOwnersCatsFlagsByVolunteer(OwnerCat owner);

    void removeFlag(OwnersCatsFlags flag);
}
