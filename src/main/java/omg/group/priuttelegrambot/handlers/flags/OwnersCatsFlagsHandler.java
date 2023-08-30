package omg.group.priuttelegrambot.handlers.flags;

import omg.group.priuttelegrambot.dto.flags.OwnersCatsFlagsDto;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.dto.pets.CatDto;


public interface OwnersCatsFlagsHandler {

    void createWaitingForNewReportFlag(OwnerCatDto ownerDto, CatDto petDto);

    void createWaitingForPhotoFlag(OwnerCatDto ownerDto, CatDto petDto);

    void createWaitingForRationFlag(OwnerCatDto ownerDto, CatDto petDto);

    void createWaitingForFeelingFlag(OwnerCatDto ownerDto, CatDto petDto);

    void createWaitingForChangesFlag(OwnerCatDto ownerDto, CatDto petDto);

    void createWaitingForContactsFlag(OwnerCatDto ownerDto);

    void createChattingFlag(OwnerCatDto ownerDto, OwnerCatDto volunteerDto);

    OwnersCatsFlagsDto findFlagByOwner(OwnerCatDto ownerDto);

    OwnersCatsFlagsDto findFlagByOwnerAndIsWaitingForReportIsTrue(OwnerCatDto ownerDto);

    OwnersCatsFlagsDto findFlagByVolunteer(OwnerCatDto ownerDto);

    OwnersCatsFlagsDto findFlagByOwnerAndIsChattingIsTrue(OwnerCatDto ownerDto);

    void removeFlag(OwnersCatsFlagsDto flagDto);
}
