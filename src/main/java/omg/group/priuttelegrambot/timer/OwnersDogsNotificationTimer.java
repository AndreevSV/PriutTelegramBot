package omg.group.priuttelegrambot.timer;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import omg.group.priuttelegrambot.dto.pets.DogDto;
import omg.group.priuttelegrambot.entity.reports.ReportDog;
import omg.group.priuttelegrambot.repository.reports.ReportsDogsRepository;
import omg.group.priuttelegrambot.service.pets.DogsService;
import omg.group.priuttelegrambot.service.reports.ReportsDogsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class OwnersDogsNotificationTimer {

    private final TelegramBot telegramBot;
    private final DogsService dogsService;
    private final ReportsDogsService reportsDogsService;
    private final ReportsDogsRepository reportsDogsRepository;


    public OwnersDogsNotificationTimer(TelegramBot telegramBot, DogsService dogsService, ReportsDogsService reportsDogsService,
                                       ReportsDogsRepository reportsDogsRepository) {
        this.telegramBot = telegramBot;
        this.dogsService = dogsService;
        this.reportsDogsService = reportsDogsService;
        this.reportsDogsRepository = reportsDogsRepository;
    }

    /**
     * Create reports for all cats on probation period in time: 00:00:00 by schedule
     */
    @Scheduled(cron = "0 0 0 * * *")
    private void createReportsForCurrent() {
        List<DogDto> petsDto = dogsService.getAllPetsOnProbationToday();
        petsDto.forEach(reportsDogsService::createNewReportsDog);
    }


    @Scheduled(cron = "0 0 9 * * *")
    @Scheduled(cron = "0 0 14 * * *")
    @Scheduled(cron = "0 0 18 * * *")
    public void sendReportMessages() {
        List<ReportDog> reports = reportsDogsRepository.findAllReportsThatNotFullfilled(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS));
        reports.forEach(reportDog -> {
            Long chatId = reportDog.getOwner().getChatId();
            if (reportDog.getFileId() == null &&
                    reportDog.getRation() == null &&
                    reportDog.getFeeling() == null &&
                    reportDog.getChanges() == null) {
                String message = String.format(
                        """
                                У Вас сегодня отчетный день по животному %s. До конца дня необходимо отправить отчет через бота. Вы должны отослать за вчерашний день: - фотографию животного\s
                                - рацион животного\s
                                 - самочувствие животного\s
                                 - произошедшие с ним изменения\s"""
                        , reportDog.getPet().getNickName());
                SendMessage sendMessage = new SendMessage(chatId, message).parseMode(ParseMode.HTML);
                telegramBot.execute(sendMessage);
            } else if (reportDog.getFileId() == null ||
                    reportDog.getRation() == null ||
                    reportDog.getFeeling() == null ||
                    reportDog.getChanges() == null) {

                String photo = "отправлена";
                String ration = "отправлен";
                String feeling = "отправлено";
                String changes = "отправлены";

                if (reportDog.getFileId() == null) {
                    photo = " - ТРЕБУЕТ отправки";
                }
                if (reportDog.getRation() == null) {
                    ration = " - ТРЕБУЕТ отправки";
                }
                if (reportDog.getFeeling() == null) {
                    feeling = " - ТРЕБУЕТ отправки";
                }
                if (reportDog.getChanges() == null) {
                    changes = " - ТРЕБУЕТ отправки";
                }

                String message = String.format(
                        """
                                У Вас сегодня отчетный день по животному %s за вчерашний день. До конца дня необходимо отправить отчет через бота. Вы началали отправлять отчет. Ниже указано, что вы отправили, а что ТРЕБУЕТ отправки:\s
                                - фотографию %s\s
                                - рацион животного %s\s
                                 - самочувствие животного %s\s
                                 - произошедшие с ним изменения %s"""
                        , reportDog.getPet().getNickName()
                        , photo
                        , ration
                        , feeling
                        , changes);
                SendMessage sendMessage = new SendMessage(chatId, message).parseMode(ParseMode.HTML);
                telegramBot.execute(sendMessage);
            }
        });
    }
}
