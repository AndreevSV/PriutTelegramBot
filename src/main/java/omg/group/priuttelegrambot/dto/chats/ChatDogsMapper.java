package omg.group.priuttelegrambot.dto.chats;


import omg.group.priuttelegrambot.dto.owners.OwnerDogMapper;
import omg.group.priuttelegrambot.entity.chats.ChatDogs;

public class ChatDogsMapper {
    public static ChatDogsDto toDto(ChatDogs chat) {
        return new ChatDogsDto(
                chat.getId(),
                chat.getIsChatting(),
                OwnerDogMapper.toDto(chat.getOwner()),
                OwnerDogMapper.toDto(chat.getVolunteer()),
                chat.getMessageSentTime(),
                chat.getAnswerSentTime(),
                chat.getCreatedAt());
    }

    public static ChatDogs toEntity(ChatDogsDto dto) {
        ChatDogs chat = new ChatDogs();
        chat.setId(dto.id());
        chat.setIsChatting(dto.isChatting());
        chat.setOwner(OwnerDogMapper.toEntity(dto.ownerDto()));
        chat.setVolunteer(OwnerDogMapper.toEntity(dto.volunteerDto()));
        chat.setMessageSentTime(dto.messageSentTime());
        chat.setAnswerSentTime(dto.answerSentTime());
        chat.setCreatedAt(dto.createdAt());

        return chat;
    }
}
