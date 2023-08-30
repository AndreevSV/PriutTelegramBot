package omg.group.priuttelegrambot.dto.chats;


import omg.group.priuttelegrambot.dto.owners.OwnerDogMapper;
import omg.group.priuttelegrambot.entity.chats.ChatDogs;

public class ChatDogsMapper {
    public static ChatDogsDto toDto(ChatDogs chat) {
        ChatDogsDto dto = new ChatDogsDto();
        dto.setId(chat.getId());
        dto.setIsChatting(chat.getIsChatting());
        dto.setOwnerDto(OwnerDogMapper.toDto(chat.getOwner()));
        dto.setVolunteerDto(OwnerDogMapper.toDto(chat.getVolunteer()));
        dto.setMessageSentTime(chat.getMessageSentTime());
        dto.setAnswerSentTime(chat.getAnswerSentTime());
        dto.setCreatedAt(chat.getCreatedAt());
        return dto;
    }

    public static ChatDogs toEntity(ChatDogsDto dto) {
        ChatDogs chat = new ChatDogs();
        chat.setId(dto.getId());
        chat.setIsChatting(dto.getIsChatting());
        chat.setOwner(OwnerDogMapper.toEntity(dto.getOwnerDto()));
        chat.setVolunteer(OwnerDogMapper.toEntity(dto.getVolunteerDto()));
        chat.setMessageSentTime(dto.getMessageSentTime());
        chat.setAnswerSentTime(dto.getAnswerSentTime());
        chat.setCreatedAt(dto.getCreatedAt());
        return chat;
    }
}
