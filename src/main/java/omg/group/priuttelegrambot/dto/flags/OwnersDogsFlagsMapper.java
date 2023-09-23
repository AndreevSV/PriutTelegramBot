package omg.group.priuttelegrambot.dto.flags;


import omg.group.priuttelegrambot.dto.owners.OwnerDogMapper;
import omg.group.priuttelegrambot.dto.pets.DogsMapper;
import omg.group.priuttelegrambot.entity.flags.OwnersDogsFlags;

public class OwnersDogsFlagsMapper {

    public static OwnersDogsFlagsDto toDto(OwnersDogsFlags flag) {

        return OwnersDogsFlagsDto.builder()
                .id(flag.getId())
                .ownerDto(OwnerDogMapper.toDto(flag.getOwner()))
                .volunteerDto(flag.getVolunteer() != null ? OwnerDogMapper.toDto(flag.getVolunteer()) : null)
                .dogDto(flag.getDog() != null ? DogsMapper.toDto(flag.getDog()) : null)
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

    public static OwnersDogsFlags toEntity(OwnersDogsFlagsDto dto) {
        OwnersDogsFlags flag = new OwnersDogsFlags();
        flag.setId(dto.getId());
        flag.setOwner(OwnerDogMapper.toEntity(dto.getOwnerDto()));
        flag.setVolunteer(flag.getVolunteer() != null ? OwnerDogMapper.toEntity(dto.getVolunteerDto()) : null);
        flag.setDog(flag.getDog() != null ? DogsMapper.toEntity(dto.getDogDto()) : null);
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