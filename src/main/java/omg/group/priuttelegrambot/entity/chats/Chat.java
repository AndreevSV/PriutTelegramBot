package omg.group.priuttelegrambot.entity.chats;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@MappedSuperclass
public abstract class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_chatting")
    private Boolean isChatting;

    @Column(name = "message_id")
    private int lastMessageId;

    @Column(name = "message_text")
    private String messageText;

    @Column(name = "answer_text")
    private String answerText;

    @Column(name = "answer_sent")
    private Boolean answerSent;

//    @Column(name = "owner_id")
//    private Long ownerId;
//
//    @Column(name = "volunteer_id")
//    private Long volunteerId;

}
