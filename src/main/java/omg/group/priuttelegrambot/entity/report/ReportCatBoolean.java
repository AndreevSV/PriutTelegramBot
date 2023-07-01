package omg.group.priuttelegrambot.entity.report;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Data
@Component
public class ReportCatBoolean {
    boolean isWaitingForCatPhoto;
    boolean isWaitingForCatRation;
    boolean isWaitingForCatFeeling;
    boolean isWaitingForCatChanges;

    public ReportCatBoolean createReportCatBooleanFalse() {
        ReportCatBoolean reportCatBoolean = new ReportCatBoolean();

        reportCatBoolean.setWaitingForCatPhoto(false);
        reportCatBoolean.setWaitingForCatRation(false);
        reportCatBoolean.setWaitingForCatFeeling(false);
        reportCatBoolean.setWaitingForCatChanges(false);

        return reportCatBoolean;
    }
    public Boolean reportCatIsNecessary(@NotNull ReportCatBoolean reportCatBoolean) {
        return reportCatBoolean.isWaitingForCatPhoto ||
                reportCatBoolean.isWaitingForCatRation ||
                reportCatBoolean.isWaitingForCatFeeling ||
                reportCatBoolean.isWaitingForCatChanges;
    }
    public ReportCatBoolean createReportCatBooleanWaitingForPhotoTrue() {
        ReportCatBoolean reportCatBoolean = new ReportCatBoolean();

        reportCatBoolean.setWaitingForCatPhoto(true);
        reportCatBoolean.setWaitingForCatRation(false);
        reportCatBoolean.setWaitingForCatFeeling(false);
        reportCatBoolean.setWaitingForCatChanges(false);

        return reportCatBoolean;
    }
    public ReportCatBoolean createReportCatBooleanWaitingForRationTrue() {
        ReportCatBoolean reportCatBoolean = new ReportCatBoolean();

        reportCatBoolean.setWaitingForCatPhoto(false);
        reportCatBoolean.setWaitingForCatRation(true);
        reportCatBoolean.setWaitingForCatFeeling(false);
        reportCatBoolean.setWaitingForCatChanges(false);

        return reportCatBoolean;
    }
    public ReportCatBoolean createReportCatBooleanWaitingForFeelingTrue() {
        ReportCatBoolean reportCatBoolean = new ReportCatBoolean();

        reportCatBoolean.setWaitingForCatPhoto(false);
        reportCatBoolean.setWaitingForCatRation(false);
        reportCatBoolean.setWaitingForCatFeeling(true);
        reportCatBoolean.setWaitingForCatChanges(false);

        return reportCatBoolean;
    }
    public ReportCatBoolean createReportCatBooleanWaitingForChangesTrue() {
        ReportCatBoolean reportCatBoolean = new ReportCatBoolean();

        reportCatBoolean.setWaitingForCatPhoto(false);
        reportCatBoolean.setWaitingForCatRation(false);
        reportCatBoolean.setWaitingForCatFeeling(false);
        reportCatBoolean.setWaitingForCatChanges(true);

        return reportCatBoolean;
    }
}
