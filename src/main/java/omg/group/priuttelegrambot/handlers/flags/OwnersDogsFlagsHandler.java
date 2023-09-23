package omg.group.priuttelegrambot.handlers.flags;

import omg.group.priuttelegrambot.dto.flags.OwnersCatsFlagsDto;
import omg.group.priuttelegrambot.dto.flags.OwnersDogsFlagsDto;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;
import omg.group.priuttelegrambot.dto.pets.DogDto;
import omg.group.priuttelegrambot.entity.flags.OwnersDogsFlags;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.entity.pets.Dog;

public interface OwnersDogsFlagsHandler {


    void createWaitingForNewReportFlag(OwnerDogDto ownerDto, DogDto petDto);

    void createWaitingForPhotoFlag(OwnerDogDto ownerDto, DogDto petDto);

    void createWaitingForRationFlag(OwnerDogDto ownerDto, DogDto petDto);

    void createWaitingForFeelingFlag(OwnerDogDto owneDto, DogDto petDto);

    void createWaitingForChangesFlag(OwnerDogDto ownerDto, DogDto petDto);

    void createWaitingForContactsFlag(OwnerDogDto ownerDto);

    void createChattingFlag(OwnerDogDto ownerDto, OwnerDogDto volunteerDto);

    OwnersDogsFlagsDto findFlagByOwner(OwnerDogDto ownerDto);

    OwnersDogsFlagsDto findFlagByOwnerAndIsWaitingForReportIsTrue(OwnerDogDto ownerDto);

    OwnersDogsFlagsDto findFlagByVolunteer(OwnerDogDto ownerDto);

    OwnersDogsFlagsDto findFlagByOwnerAndIsChattingIsTrue(OwnerDogDto ownerDto);

    void removeFlag(OwnersDogsFlagsDto flagDto);
}
