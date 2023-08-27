package omg.group.priuttelegrambot.dto.owners;

import omg.group.priuttelegrambot.entity.owners.OwnerDog;

public class OwnerDogMapper {

    public static OwnerDogDto toDto(OwnerDog owner) {
        OwnerDogDto dto = new OwnerDogDto();
        dto.setId(owner.getId());
        dto.setUserName(owner.getUserName());
        dto.setName(owner.getName());
        dto.setSurname(owner.getSurname());
        dto.setPatronymic(owner.getPatronymic());
        dto.setBirthday(owner.getBirthday());
        dto.setTelephone(owner.getTelephone());
        dto.setEmail(owner.getEmail());
        dto.setCreatedAt(owner.getCreatedAt());
        dto.setUpdatedAt(owner.getUpdatedAt());
        dto.setDateIncome(owner.getDateIncome());
        dto.setDateOutcome(owner.getDateOutcome());
        dto.setBecameClient(owner.getBecameClient());
        dto.setIsVolunteer(owner.getIsVolunteer());
        dto.setChatId(owner.getChatId());
        dto.setVolunteer(owner.getVolunteer());
        dto.setTelegramUserId(owner.getTelegramUserId());
        dto.setVolunteerChatOpened(owner.getVolunteerChatOpened());

        return dto;
    }

    public static OwnerDog toEntity(OwnerDogDto dto) {
        OwnerDog owner = new OwnerDog();
        owner.setId(dto.getId());
        owner.setUserName(dto.getUserName());
        owner.setName(dto.getName());
        owner.setSurname(dto.getSurname());
        owner.setPatronymic(dto.getPatronymic());
        owner.setBirthday(dto.getBirthday());
        owner.setTelephone(dto.getTelephone());
        owner.setEmail(dto.getEmail());
        owner.setCreatedAt(dto.getCreatedAt());
        owner.setUpdatedAt(dto.getUpdatedAt());
        owner.setDateIncome(dto.getDateIncome());
        owner.setDateOutcome(dto.getDateOutcome());
        owner.setBecameClient(dto.getBecameClient());
        owner.setIsVolunteer(dto.getIsVolunteer());
        owner.setChatId(dto.getChatId());
        owner.setVolunteer(dto.getVolunteer());
        owner.setTelegramUserId(dto.getTelegramUserId());
        owner.setVolunteerChatOpened(dto.getVolunteerChatOpened());

        return owner;
    }
}
