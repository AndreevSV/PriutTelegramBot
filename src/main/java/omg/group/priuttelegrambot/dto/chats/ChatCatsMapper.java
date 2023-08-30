package omg.group.priuttelegrambot.dto.chats;

import omg.group.priuttelegrambot.dto.owners.OwnerCatMapper;
import omg.group.priuttelegrambot.entity.chats.ChatCats;

public class ChatCatsMapper {
    public static ChatCatsDto toDto(ChatCats chat) {
        ChatCatsDto dto = new ChatCatsDto();
        dto.setId(chat.getId());
        dto.setIsChatting(chat.getIsChatting());
        dto.setOwnerDto(OwnerCatMapper.toDto(chat.getOwner()));
        dto.setVolunteerDto(OwnerCatMapper.toDto(chat.getVolunteer()));
        dto.setMessageSentTime(chat.getMessageSentTime());
        dto.setMessageSentTime(chat.getAnswerSentTime());
        dto.setCreatedAt(chat.getCreatedAt());
        return dto;
    }

    public static ChatCats toEntity(ChatCatsDto dto) {
        ChatCats chat = new ChatCats();
        chat.setId(dto.getId());
        chat.setIsChatting(dto.getIsChatting());
        chat.setOwner(OwnerCatMapper.toEntity(dto.getOwnerDto()));
        chat.setVolunteer(OwnerCatMapper.toEntity(dto.getVolunteerDto()));
        chat.setMessageSentTime(dto.getMessageSentTime());
        chat.setAnswerSentTime(dto.getAnswerSentTime());
        chat.setCreatedAt(dto.getCreatedAt());
        return chat;
    }
}





