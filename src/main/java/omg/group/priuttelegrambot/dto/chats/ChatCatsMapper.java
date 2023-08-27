package omg.group.priuttelegrambot.dto.chats;

import omg.group.priuttelegrambot.dto.owners.OwnerCatMapper;
import omg.group.priuttelegrambot.entity.chats.ChatCats;

public class ChatCatsMapper {
    public static ChatCatsDto toDto(ChatCats chat) {
        return new ChatCatsDto(
                chat.getId(),
                chat.getIsChatting(),
                OwnerCatMapper.toDto(chat.getOwner()),
                OwnerCatMapper.toDto(chat.getVolunteer()),
                chat.getMessageSentTime(),
                chat.getAnswerSentTime(),
                chat.getCreatedAt());
    }

    public static ChatCats toEntity(ChatCatsDto dto) {
        ChatCats chat = new ChatCats();
        chat.setId(dto.id());
        chat.setIsChatting(dto.isChatting());
        chat.setOwner(OwnerCatMapper.toEntity(dto.ownerDto()));
        chat.setVolunteer(OwnerCatMapper.toEntity(dto.volunteerDto()));
        chat.setMessageSentTime(dto.messageSentTime());
        chat.setAnswerSentTime(dto.answerSentTime());
        chat.setCreatedAt(dto.createdAt());

        return chat;
    }
}





