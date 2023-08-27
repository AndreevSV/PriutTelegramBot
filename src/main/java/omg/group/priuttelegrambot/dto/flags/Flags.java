package omg.group.priuttelegrambot.dto.flags;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@Component
public class Flags {
    private Long id;
    private Long ownerId;
    private Long volunteerId;
    private Long animalId;
    private boolean isWaitingForPhoto;
    private boolean isWaitingForRation;
    private boolean isWaitingForFeeling;
    private boolean isWaitingForChanges;
    private boolean isWaitingForContacts;
    private LocalDate date;
    private boolean isChatting;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}