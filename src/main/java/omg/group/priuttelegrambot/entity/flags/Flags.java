package omg.group.priuttelegrambot.entity.flags;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@MappedSuperclass
public abstract class Flags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "waiting_for_photo")
    private boolean isWaitingForPhoto;
    @Column(name = "waiting_for_ration")
    private boolean isWaitingForRation;
    @Column(name = "waiting_for_feeling")
    private boolean isWaitingForFeeling;
    @Column(name = "waiting_for_changes")
    private boolean isWaitingForChanges;
    @Column(name = "waiting_for_contacts")
    private boolean isWaitingForContacts;
    @Column(name = "chatting")
    private boolean isChatting;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "date")
    private LocalDate date;

}
