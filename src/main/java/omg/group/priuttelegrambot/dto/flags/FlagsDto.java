package omg.group.priuttelegrambot.dto.flags;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public abstract class FlagsDto {
    private Long id;
    private Boolean isWaitingForPhoto;
    private Boolean isWaitingForRation;
    private Boolean isWaitingForFeeling;
    private Boolean isWaitingForChanges;
    private Boolean isWaitingForContacts;
    private LocalDate date;
    private Boolean isChatting;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
