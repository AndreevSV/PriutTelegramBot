package omg.group.priuttelegrambot.entity.report;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Data
@RequiredArgsConstructor
@Component
public class ReportCatBoolean {
    private boolean isWaitingForPhoto;
    private boolean isWaitingForRation;
    private boolean isWaitingForFeeling;
    private boolean isWaitingForChanges;

    public Boolean reportIsNecessary(@NotNull ReportCatBoolean reportCatBoolean) {
        if (!reportCatBoolean.isWaitingForPhoto &&
                !reportCatBoolean.isWaitingForRation &&
                !reportCatBoolean.isWaitingForFeeling &&
                !reportCatBoolean.isWaitingForChanges) {
            return false;
        } else {
            return true;
        }
    }

    public ReportCatBoolean photoIsNecessary() {
        ReportCatBoolean reportCatBoolean = new ReportCatBoolean();

        reportCatBoolean.setWaitingForPhoto(true);
        reportCatBoolean.setWaitingForRation(false);
        reportCatBoolean.setWaitingForFeeling(false);
        reportCatBoolean.setWaitingForChanges(false);

        return reportCatBoolean;
    }

    public ReportCatBoolean rationIsNecessary() {
        ReportCatBoolean reportCatBoolean = new ReportCatBoolean();

        reportCatBoolean.setWaitingForPhoto(false);
        reportCatBoolean.setWaitingForRation(true);
        reportCatBoolean.setWaitingForFeeling(false);
        reportCatBoolean.setWaitingForChanges(false);

        return reportCatBoolean;
    }

    public ReportCatBoolean feelingIsNecessary() {
        ReportCatBoolean reportCatBoolean = new ReportCatBoolean();

        reportCatBoolean.setWaitingForPhoto(false);
        reportCatBoolean.setWaitingForRation(false);
        reportCatBoolean.setWaitingForFeeling(true);
        reportCatBoolean.setWaitingForChanges(false);

        return reportCatBoolean;
    }

    public ReportCatBoolean changesIsNecessary() {
        ReportCatBoolean reportCatBoolean = new ReportCatBoolean();

        reportCatBoolean.setWaitingForPhoto(false);
        reportCatBoolean.setWaitingForRation(false);
        reportCatBoolean.setWaitingForFeeling(false);
        reportCatBoolean.setWaitingForChanges(true);

        return reportCatBoolean;
    }

    public ReportCatBoolean reportFalse() {
        ReportCatBoolean reportCatBoolean = new ReportCatBoolean();

        reportCatBoolean.setWaitingForPhoto(false);
        reportCatBoolean.setWaitingForRation(false);
        reportCatBoolean.setWaitingForFeeling(false);
        reportCatBoolean.setWaitingForChanges(false);

        return reportCatBoolean;
    }
}
