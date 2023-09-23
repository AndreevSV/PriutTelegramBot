package omg.group.priuttelegrambot.dto.flags;

import omg.group.priuttelegrambot.dto.owners.OwnerCatMapper;
import omg.group.priuttelegrambot.dto.pets.CatsMapper;
import omg.group.priuttelegrambot.entity.flags.OwnersCatsFlags;

public class OwnersCatsFlagsMapper {
    public static OwnersCatsFlagsDto toDto(OwnersCatsFlags flag) {
        return OwnersCatsFlagsDto.builder()
                .id(flag.getId())
                .ownerDto(OwnerCatMapper.toDto(flag.getOwner()))
                .volunteerDto(flag.getVolunteer() != null ? OwnerCatMapper.toDto(flag.getVolunteer()) : null)
                .catDto(flag.getCat() != null ? CatsMapper.toDto(flag.getCat()) : null)
                .waitingForPhoto(flag.getIsWaitingForPhoto())
                .waitingForRation(flag.getIsWaitingForRation())
                .waitingForFeeling(flag.getIsWaitingForFeeling())
                .waitingForChanges(flag.getIsWaitingForChanges())
                .waitingForContacts(flag.getIsWaitingForContacts())
                .chatting(flag.getIsChatting())
                .createdAt(flag.getCreatedAt())
                .updatedAt(flag.getUpdatedAt())
                .date(flag.getDate())
                .build();
    }

    public static OwnersCatsFlags toEntity(OwnersCatsFlagsDto dto) {
        OwnersCatsFlags flag = new OwnersCatsFlags();
        flag.setId(dto.getId());
        flag.setOwner(OwnerCatMapper.toEntity(dto.getOwnerDto()));
        flag.setVolunteer(dto.getVolunteerDto() != null ? OwnerCatMapper.toEntity(dto.getVolunteerDto()) : null);
        flag.setCat(dto.getCatDto() != null ? CatsMapper.toEntity(dto.getCatDto()) : null);
        flag.setIsWaitingForPhoto(dto.isWaitingForPhoto());
        flag.setIsWaitingForRation(dto.isWaitingForRation());
        flag.setIsWaitingForFeeling(dto.isWaitingForFeeling());
        flag.setIsWaitingForChanges(dto.isWaitingForChanges());
        flag.setIsWaitingForContacts(dto.isWaitingForContacts());
        flag.setIsChatting(dto.isChatting());
        flag.setCreatedAt(dto.getCreatedAt());
        flag.setUpdatedAt(dto.getUpdatedAt());
        flag.setDate(dto.getDate());

        return flag;
    }
}
