package omg.group.priuttelegrambot.dto.flags;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SuperBuilder(toBuilder = true)
@Getter
public abstract class FlagsDto {
    private Long id;
    @Builder.Default
    private boolean waitingForPhoto = false;
    @Builder.Default
    private boolean waitingForRation = false;
    @Builder.Default
    private boolean waitingForFeeling = false;
    @Builder.Default
    private boolean waitingForChanges = false;
    @Builder.Default
    private boolean waitingForContacts = false;
    private LocalDate date;
    @Builder.Default
    private boolean chatting = false;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
