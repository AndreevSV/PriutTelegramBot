package omg.group.priuttelegrambot.dto.chats;

import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;

import java.time.LocalDateTime;

public record ChatCatsDto(
        Long id,
        Boolean isChatting,
        OwnerCatDto ownerDto,
        OwnerCatDto volunteerDto,
        LocalDateTime messageSentTime,
        LocalDateTime answerSentTime,
        LocalDateTime createdAt) {}
