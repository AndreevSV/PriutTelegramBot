package omg.group.priuttelegrambot.entity.flags;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class Flags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "waiting_for_photo")
    private Boolean isWaitingForPhoto;
    @Column(name = "waiting_for_ration")
    private Boolean isWaitingForRation;
    @Column(name = "waiting_for_feeling")
    private Boolean isWaitingForFeeling;
    @Column(name = "waiting_for_changes")
    private Boolean isWaitingForChanges;
    @Column(name = "waiting_for_contacts")
    private Boolean isWaitingForContacts;
    @Column(name = "chatting")
    private Boolean isChatting;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "date")
    private LocalDate date;

}
