package omg.group.priuttelegrambot.entity.chats;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_chatting")
    private Boolean isChatting;

    @Column(name = "message_sent_time")
    private LocalDateTime messageSentTime;

    @Column(name = "answer_sent_time")
    private LocalDateTime answerSentTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
