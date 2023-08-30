package omg.group.priuttelegrambot.dto.flags;


import omg.group.priuttelegrambot.dto.owners.OwnerDogMapper;
import omg.group.priuttelegrambot.dto.pets.DogsMapper;
import omg.group.priuttelegrambot.entity.flags.OwnersDogsFlags;

public class OwnersDogsFlagsMapper {

    public static OwnersDogsFlagsDto toDto(OwnersDogsFlags flag) {
        OwnersDogsFlagsDto dto = new OwnersDogsFlagsDto();
        dto.setId(flag.getId());
        dto.setOwnerDto(OwnerDogMapper.toDto(flag.getOwner()));
        if (flag.getVolunteer() != null) {
            dto.setVolunteerDto(OwnerDogMapper.toDto(flag.getVolunteer()));
        } else {
            dto.setVolunteerDto(null);
        }
        if (flag.getDog() != null) {
            dto.setDogDto(DogsMapper.toDto(flag.getDog()));
        } else {
            dto.setDogDto(null);
        }
        dto.setIsWaitingForPhoto(flag.getIsWaitingForPhoto());
        dto.setIsWaitingForRation(flag.getIsWaitingForRation());
        dto.setIsWaitingForFeeling(flag.getIsWaitingForFeeling());
        dto.setIsWaitingForChanges(flag.getIsWaitingForChanges());
        dto.setIsWaitingForContacts(flag.getIsWaitingForContacts());
        dto.setIsChatting(flag.getIsChatting());
        dto.setCreatedAt(flag.getCreatedAt());
        dto.setUpdatedAt(flag.getUpdatedAt());
        dto.setDate(flag.getDate());

        return dto;
    }

    public static OwnersDogsFlags toEntity(OwnersDogsFlagsDto dto) {
        OwnersDogsFlags flag = new OwnersDogsFlags();
        flag.setId(dto.getId());
        flag.setOwner(OwnerDogMapper.toEntity(dto.getOwnerDto()));
        if (flag.getVolunteer() != null) {
            flag.setVolunteer(OwnerDogMapper.toEntity(dto.getVolunteerDto()));
        } else {
            flag.setVolunteer(null);
        }
        if (flag.getDog() != null) {
            flag.setDog(DogsMapper.toEntity(dto.getDogDto()));
        } else {
            flag.setDog(null);
        }
        flag.setIsWaitingForPhoto(dto.getIsWaitingForPhoto());
        flag.setIsWaitingForRation(dto.getIsWaitingForRation());
        flag.setIsWaitingForFeeling(dto.getIsWaitingForFeeling());
        flag.setIsWaitingForChanges(dto.getIsWaitingForChanges());
        flag.setIsWaitingForContacts(dto.getIsWaitingForContacts());
        flag.setIsChatting(dto.getIsChatting());
        flag.setCreatedAt(dto.getCreatedAt());
        flag.setUpdatedAt(dto.getUpdatedAt());
        flag.setDate(dto.getDate());

        return flag;
    }
}