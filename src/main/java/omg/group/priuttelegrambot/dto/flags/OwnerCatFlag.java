package omg.group.priuttelegrambot.dto.flags;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@RequiredArgsConstructor
@Component
public class OwnerCatFlag {
    private boolean isWaitingForPhoto;
    private boolean isWaitingForRation;
    private boolean isWaitingForFeeling;
    private boolean isWaitingForChanges;
    private boolean isWaitingForContacts;
    private boolean isChatting;

    public boolean somethingInProgress() {
        return isWaitingForPhoto ||
                isWaitingForRation ||
                isWaitingForFeeling ||
                isWaitingForChanges ||
                isWaitingForContacts ||
                isChatting;
    }
}
