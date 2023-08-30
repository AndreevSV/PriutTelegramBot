package omg.group.priuttelegrambot.dto.flags;

import omg.group.priuttelegrambot.dto.owners.OwnerCatMapper;
import omg.group.priuttelegrambot.dto.pets.CatsMapper;
import omg.group.priuttelegrambot.entity.flags.OwnersCatsFlags;

public class OwnersCatsFlagsMapper {
    public static OwnersCatsFlagsDto toDto(OwnersCatsFlags flag) {
        OwnersCatsFlagsDto dto = new OwnersCatsFlagsDto();
        dto.setId(flag.getId());
        dto.setOwnerDto(OwnerCatMapper.toDto(flag.getOwner()));
        if (flag.getVolunteer() != null) {
            dto.setVolunteerDto(OwnerCatMapper.toDto(flag.getVolunteer()));
        } else {
            dto.setVolunteerDto(null);
        }
        if (flag.getCat() != null) {
            dto.setCatDto(CatsMapper.toDto(flag.getCat()));
        } else {
            dto.setCatDto(null);
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

    public static OwnersCatsFlags toEntity(OwnersCatsFlagsDto dto) {
        OwnersCatsFlags flag = new OwnersCatsFlags();
        flag.setId(dto.getId());
        flag.setOwner(OwnerCatMapper.toEntity(dto.getOwnerDto()));
        if (dto.getVolunteerDto() != null) {
            flag.setVolunteer(OwnerCatMapper.toEntity(dto.getVolunteerDto()));
        } else {
            flag.setVolunteer(null);
        }
        if (dto.getCatDto() != null) {
            flag.setCat(CatsMapper.toEntity(dto.getCatDto()));
        } else {
            flag.setCat(null);
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
