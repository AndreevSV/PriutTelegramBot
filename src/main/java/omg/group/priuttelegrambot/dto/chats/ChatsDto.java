package omg.group.priuttelegrambot.dto.chats;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class ChatsDto {
    private Long id;
    private Boolean chatting;
    private LocalDateTime messageSentTime;
    private LocalDateTime answerSentTime;
    private LocalDateTime createdAt;
}
