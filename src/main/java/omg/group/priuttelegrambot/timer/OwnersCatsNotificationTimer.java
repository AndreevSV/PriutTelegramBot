package omg.group.priuttelegrambot.timer;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.extern.slf4j.Slf4j;
import omg.group.priuttelegrambot.dto.pets.CatDto;
import omg.group.priuttelegrambot.entity.reports.ReportCat;
import omg.group.priuttelegrambot.repository.reports.ReportsCatsRepository;
import omg.group.priuttelegrambot.service.pets.CatsService;
import omg.group.priuttelegrambot.service.reports.ReportsCatsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
public class OwnersCatsNotificationTimer {

    private final TelegramBot telegramBot;

    private final CatsService catsService;

    private final ReportsCatsService reportsCatsService;
    private final ReportsCatsRepository reportsCatsRepository;


    public OwnersCatsNotificationTimer(TelegramBot telegramBot, CatsService catsService, ReportsCatsService reportsCatsService,
                                       ReportsCatsRepository reportsCatsRepository) {
        this.telegramBot = telegramBot;
        this.catsService = catsService;
        this.reportsCatsService = reportsCatsService;
        this.reportsCatsRepository = reportsCatsRepository;
    }

    /**
     * Create reports for all cats on probation period in time: 00:00:00 by schedule
     */
    @Scheduled(cron = "0 26 0 * * *")
    private void createReportsForCurrent() {
        List<CatDto> petsDto = catsService.getAllPetsOnProbationToday();
        petsDto.forEach(reportsCatsService::createNewReportCat);
    }

    @Scheduled(cron = "0 0 9 * * *")
    @Scheduled(cron = "0 0 14 * * *")
    @Scheduled(cron = "0 0 18 * * *")
    public void sendReportMessages() {
        log.info("Method sendReport started successfully");
        List<ReportCat> reports = reportsCatsRepository.findAllReportsThatNotFullfilled(LocalDate.now());
        log.info("Reports got successfully {}", reports);
        reports.forEach(reportCat -> {
            Long chatId = reportCat.getOwner().getChatId();
            if (reportCat.getFileId() == null &&
                    reportCat.getRation() == null &&
                    reportCat.getFeeling() == null &&
                    reportCat.getChanges() == null) {
                log.info("Check up was passed successfully");
                String message = String.format(
                        """
                                У Вас сегодня отчетный день по животному %s. До конца дня необходимо отправить отчет через бота.Вы должны отослать за вчерашний день:\s
                                - фотографию животного\s
                                 - рацион животного\s
                                 - самочувствие животного\s
                                 - произошедшие с ним изменения\s"""
                        , reportCat.getPet().getNickName());
                SendMessage sendMessage = new SendMessage(chatId, message);
                telegramBot.execute(sendMessage);
            } else if (reportCat.getFileId() == null ||
                    reportCat.getRation() == null ||
                    reportCat.getFeeling() == null ||
                    reportCat.getChanges() == null) {

                String photo = "отправлена";
                String ration = "отправлен";
                String feeling = "отправлено";
                String changes = "отправлены";

                if (reportCat.getFileId() == null) {
                    photo = " - ТРЕБУЕТ отправки";
                }
                if (reportCat.getRation() == null) {
                    ration = " - ТРЕБУЕТ отправки";
                }
                if (reportCat.getFeeling() == null) {
                    feeling = " - ТРЕБУЕТ отправки";
                }
                if (reportCat.getChanges() == null) {
                    changes = " - ТРЕБУЕТ отправки";
                }
                String message = String.format(
                        """
                                У Вас сегодня отчетный день по животному %s за вчерашний день. До конца дня необходимо отправить отчет через бота.Вы началали отправлять отчет. Ниже указано, что вы отправили, а что ТРЕБУЕТ отправки:\s
                                - фотографию %s\s
                                - рацион животного %s\s
                                 - самочувствие животного %s\s
                                 - произошедшие с ним изменения %s"""
                        , reportCat.getPet().getNickName()
                        , photo
                        , ration
                        , feeling
                        , changes);
                SendMessage sendMessage = new SendMessage(chatId, message);
                telegramBot.execute(sendMessage);
                log.info("Some positions was opt");
            }
        });
        log.info("Method passed successfully");
    }
}
