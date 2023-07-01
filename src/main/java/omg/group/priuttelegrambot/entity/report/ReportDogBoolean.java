package omg.group.priuttelegrambot.entity.report;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ReportDogBoolean {
    boolean isWaitingForDogPhoto;
    boolean isWaitingForDogRation;
    boolean isWaitingForDogFeeling;
    boolean isWaitingForDogChanges;

    public Boolean reportDogIsNecessary(ReportDogBoolean reportDogBoolean) {
        return reportDogBoolean.isWaitingForDogPhoto ||
                reportDogBoolean.isWaitingForDogRation ||
                reportDogBoolean.isWaitingForDogChanges ||
                reportDogBoolean.isWaitingForDogFeeling;
    }

    public ReportDogBoolean createReportDogBooleanFalse() {

        ReportDogBoolean reportDogBoolean = new ReportDogBoolean();

        reportDogBoolean.setWaitingForDogPhoto(false);
        reportDogBoolean.setWaitingForDogRation(false);
        reportDogBoolean.setWaitingForDogFeeling(false);
        reportDogBoolean.setWaitingForDogChanges(false);

        return reportDogBoolean;
    }
}
