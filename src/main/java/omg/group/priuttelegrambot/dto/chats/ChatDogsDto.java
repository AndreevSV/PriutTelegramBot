package omg.group.priuttelegrambot.dto.chats;

import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;

import java.time.LocalDateTime;

public record ChatDogsDto(
        Long id,
        Boolean isChatting,
        OwnerDogDto ownerDto,
        OwnerDogDto volunteerDto,
        LocalDateTime messageSentTime,
        LocalDateTime answerSentTime,
        LocalDateTime createdAt) {
}