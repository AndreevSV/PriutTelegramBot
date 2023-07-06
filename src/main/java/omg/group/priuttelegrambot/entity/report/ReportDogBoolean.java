package omg.group.priuttelegrambot.entity.report;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Data
@RequiredArgsConstructor
@Component
public class ReportDogBoolean {
    private boolean isWaitingForPhoto;
    private boolean isWaitingForRation;
    private boolean isWaitingForFeeling;
    private boolean isWaitingForChanges;

    public Boolean reportIsNecessary(@NotNull ReportDogBoolean reportDogBoolean) {
        if (!reportDogBoolean.isWaitingForPhoto &&
                !reportDogBoolean.isWaitingForRation &&
                !reportDogBoolean.isWaitingForFeeling &&
                !reportDogBoolean.isWaitingForChanges) {
            return false;
        } else {
            return true;
        }
    }

    public ReportDogBoolean photoIsNecessary() {
        ReportDogBoolean reportDogBoolean = new ReportDogBoolean();

        reportDogBoolean.setWaitingForPhoto(true);
        reportDogBoolean.setWaitingForRation(false);
        reportDogBoolean.setWaitingForFeeling(false);
        reportDogBoolean.setWaitingForChanges(false);

        return reportDogBoolean;
    }

    public ReportDogBoolean rationIsNecessary() {
        ReportDogBoolean reportDogBoolean = new ReportDogBoolean();

        reportDogBoolean.setWaitingForPhoto(false);
        reportDogBoolean.setWaitingForRation(true);
        reportDogBoolean.setWaitingForFeeling(false);
        reportDogBoolean.setWaitingForChanges(false);

        return reportDogBoolean;
    }

    public ReportDogBoolean feelingIsNecessary() {
        ReportDogBoolean reportDogBoolean = new ReportDogBoolean();

        reportDogBoolean.setWaitingForPhoto(false);
        reportDogBoolean.setWaitingForRation(false);
        reportDogBoolean.setWaitingForFeeling(true);
        reportDogBoolean.setWaitingForChanges(false);

        return reportDogBoolean;
    }

    public ReportDogBoolean changesIsNecessary() {
        ReportDogBoolean reportDogBoolean = new ReportDogBoolean();

        reportDogBoolean.setWaitingForPhoto(false);
        reportDogBoolean.setWaitingForRation(false);
        reportDogBoolean.setWaitingForFeeling(false);
        reportDogBoolean.setWaitingForChanges(true);

        return reportDogBoolean;
    }


    public ReportDogBoolean reportFalse() {
        ReportDogBoolean reportDogBoolean = new ReportDogBoolean();

        reportDogBoolean.setWaitingForPhoto(false);
        reportDogBoolean.setWaitingForRation(false);
        reportDogBoolean.setWaitingForFeeling(false);
        reportDogBoolean.setWaitingForChanges(false);

        return reportDogBoolean;
    }

}
