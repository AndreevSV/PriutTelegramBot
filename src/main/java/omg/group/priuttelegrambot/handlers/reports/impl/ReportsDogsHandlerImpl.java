package omg.group.priuttelegrambot.handlers.reports.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import omg.group.priuttelegrambot.entity.pets.Dog;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.entity.reports.ReportDog;
import omg.group.priuttelegrambot.handlers.menu.DogsMenuHandler;
import omg.group.priuttelegrambot.handlers.owners.OwnersDogsHandler;
import omg.group.priuttelegrambot.handlers.pets.DogsHandler;
import omg.group.priuttelegrambot.handlers.photo.PhotoHandler;
import omg.group.priuttelegrambot.handlers.reports.ReportsDogsHandler;
import omg.group.priuttelegrambot.repository.reports.ReportsDogsRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class ReportsDogsHandlerImpl implements ReportsDogsHandler {

    private final int DAYS_OF_PROBATION = 14;
    @Value("${telegram.bot.token}")
    private String token;
    private final TelegramBot telegramBot;
    private final OwnersDogsHandler ownersDogsHandler;
    private final DogsHandler dogsHandler;
    private final ReportsDogsRepository reportsDogsRepository;
    private final DogsMenuHandler dogsMenuHandler;
    private final PhotoHandler photoHandler;

    public ReportsDogsHandlerImpl(TelegramBot telegramBot,
                                  OwnersDogsHandler ownersDogsHandler,
                                  DogsHandler dogsHandler,
                                  ReportsDogsRepository reportsDogsRepository,
                                  DogsMenuHandler dogsMenuHandler,
                                  PhotoHandler photoHandler) {
        this.telegramBot = telegramBot;
        this.ownersDogsHandler = ownersDogsHandler;
        this.dogsHandler = dogsHandler;
        this.reportsDogsRepository = reportsDogsRepository;
        this.dogsMenuHandler = dogsMenuHandler;
        this.photoHandler = photoHandler;
    }

    /**
     * Method checks if report for today exist in ReportsDogsRepository
     */
    @Override
    public boolean isReportExist(Update update) {

        Dog dog = dogsHandler.returnOneDogOnProbation(update);

        Long animalId = dog.getId();
        Long clientId = dog.getOwnerDog().getId();

        Optional<ReportDog> report = reportsDogsRepository
                .findByClientIdAndAnimalIdAndDateReport(clientId, animalId, LocalDate.now());

        return report.isPresent();
    }

    /**
     * Method that receives photo and stores it in database
     */
    @Override
    @Transactional
    public boolean receivePhoto(Update update) {

        Long chatId;
        Integer date;

        if (checkForProbationPeriodSetAndValid(update)) {

            if (update.message() != null && update.message().photo() != null) {
                chatId = update.message().chat().id();
                date = update.message().date();

                String fileId = photoHandler.getFileIdFromUpdate(update);
                String savePath = photoHandler.downloadFileByFileId(fileId);
                byte[] fileAsArray = photoHandler.getFileAsArray(savePath);
                int hashOfPhoto = photoHandler.getHashOfFile(fileAsArray);

                LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

                Dog dog = dogsHandler.returnOneDogOnProbation(update);

                Long clientId = dog.getOwnerDog().getId();
                Long dogId = dog.getId();

                if (!isReportExist(update)) { // If report for today not exist

                    ReportDog report = new ReportDog(); // Set new report with filePath
                    report.setClientId(clientId);
                    report.setAnimalId(dogId);
                    report.setFileId(fileId);
                    report.setCreatedAt(localDateTime);
                    report.setDateReport(LocalDate.now());
                    report.setDateLastReport(LocalDate.now().plusDays(DAYS_OF_PROBATION));
                    report.setHashOfPhoto(hashOfPhoto);

                    reportsDogsRepository.save(report); // Save photoPath in report

                    InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                    telegramBot.execute(new SendMessage(chatId, """
                            Создан новый отчет за сегодняший
                            день и высланная фотография успешно
                            сохранена. Вам необходимо отослать
                            следующую информацию:
                            - рацион животного;
                            - самочувствие животного;
                            - произошедшие изменения.
                            Для этого нажмите соответствующую
                            кнопку
                            """)
                            .parseMode(ParseMode.Markdown)
                            .replyMarkup(inlineKeyboardMarkup));
                    return true;
                } else if (isReportExist(update)) {

                    Optional<ReportDog> report = reportsDogsRepository
                            .findByClientIdAndAnimalIdAndDateReport(clientId, dogId, LocalDate.now());

                    if (report.isPresent() &&
                            report.get().getFileId().isEmpty() &&
                            reportsDogsRepository.findByHashOfPhoto(hashOfPhoto).isEmpty()) {

                        report.get().setFileId(fileId);
                        report.get().setUpdatedAt(LocalDateTime.now());
                        report.get().setHashOfPhoto(hashOfPhoto);

                        reportsDogsRepository.save(report.get());

                        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                        telegramBot.execute(new SendMessage(chatId, """
                                *Фотография успешно сохранена*.
                                Выберите следующую команду:""")
                                .parseMode(ParseMode.Markdown)
                                .replyMarkup(inlineKeyboardMarkup));
                        return true;
                    } else if (report.isPresent() &&
                            report.get().getFileId().isEmpty() &&
                            reportsDogsRepository.findByHashOfPhoto(hashOfPhoto).isPresent()) {

                        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                        telegramBot.execute(new SendMessage(chatId, """
                                Вы уже отсылали такую фотографию.
                                Отошлите свежую фотографию.""")
                                .parseMode(ParseMode.Markdown)
                                .replyMarkup(inlineKeyboardMarkup));
                        return false;
                    } else if (report.isPresent() &&
                            !report.get().getFileId().isEmpty()) {

                        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                        telegramBot.execute(new SendMessage(chatId, """
                                Вы уже отсылали фотографию.
                                Отсылать фотографию сегодня
                                больше не нужно""")
                                .parseMode(ParseMode.Markdown)
                                .replyMarkup(inlineKeyboardMarkup));
                        return true;
                    }
                }
            } else {
                chatId = update.message().chat().id();
                InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                telegramBot.execute(new SendMessage(chatId, """
                        Вы пытаетесь отослать не фотографию.
                        Для отсылки фотографии нажмите снова
                        команду *Отправить фото*.
                        """)
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(inlineKeyboardMarkup));
                return false;
            }
        }
        return false;
    }

    /**
     * Method that receives ration of the animal and stores it in database
     */
    @Override
    public boolean receiveRation(Update update) {
        Long chatId = 0L;
        Integer date = null;
        String text = "";

        if (update.message() != null) {
            chatId = update.message().chat().id();
            date = update.message().date();
            text = update.message().text();
        } else if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
        }

        LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        Dog dog = dogsHandler.returnOneDogOnProbation(update);

        Long clientId = dog.getOwnerDog().getId();
        Long dogId = dog.getId();

        if (!isReportExist(update)) { // If report for today not exist

            ReportDog report = new ReportDog(); // Set new report with filePath
            report.setClientId(clientId);
            report.setAnimalId(dogId);
            report.setRation(text);
            report.setCreatedAt(localDateTime);
            report.setDateReport(LocalDate.now());
            report.setDateLastReport(LocalDate.now().plusDays(DAYS_OF_PROBATION));

            reportsDogsRepository.save(report);

            InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
            telegramBot.execute(new SendMessage(chatId, """
                    Создан новый отчет за сегодняший день и
                    высланный рацион успешно сохранен.
                    Вам необходимо дополнительно отослать
                    следующую информацию:
                    - фотограцию животного;
                    - самочувствие животного;
                    - произошедшие изменения.
                    Для этого нажмите соответствующую кнопку
                    """)
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup));
            return true;
        } else if (isReportExist(update)) {

            Optional<ReportDog> report = reportsDogsRepository
                    .findByClientIdAndAnimalIdAndDateReport(clientId, dogId, LocalDate.now());

            if (report.isPresent() && (report.get().getRation() == null || report.get().getRation().isEmpty())) {

                report.get().setRation(text);
                report.get().setUpdatedAt(LocalDateTime.now());

                reportsDogsRepository.save(report.get());

                InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                telegramBot.execute(new SendMessage(chatId, """
                        *Введеный *рацион* успешно сохранен*.
                        Выберите следующую команду:
                        """)
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(inlineKeyboardMarkup));
                return true;
            } else if (report.isPresent()) {

                String ration = report.get().getRation();

                InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                telegramBot.execute(new SendMessage(chatId, String.format("""
                        Вы уже отсылали сегодня рацион.
                        Вот он:
                        %s""", ration))
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(inlineKeyboardMarkup));
                return true;
            }
        }
        return false;
    }

    /**
     * Method that receives self-feeling of the animal and stores it in database
     */
    @Override
    public boolean receiveFeeling(Update update) {
        Long chatId = 0L;
        Integer date = null;
        String text = "";

        if (update.message() != null) {
            chatId = update.message().chat().id();
            date = update.message().date();
            text = update.message().text();
        } else if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
        }

        LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        Dog dog = dogsHandler.returnOneDogOnProbation(update);

        Long clientId = dog.getOwnerDog().getId();
        Long dogId = dog.getId();

        if (!isReportExist(update)) { // If report for today not exist

            ReportDog report = new ReportDog(); // Set new report with filePath
            report.setClientId(clientId);
            report.setAnimalId(dogId);
            report.setFeeling(text);
            report.setCreatedAt(localDateTime);
            report.setDateReport(LocalDate.now());
            report.setDateLastReport(LocalDate.now().plusDays(DAYS_OF_PROBATION));

            reportsDogsRepository.save(report);

            InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
            telegramBot.execute(new SendMessage(chatId, """
                    Создан новый отчет за сегодняший день и
                    высланный отчет о *самочувствии животного* успешно сохранен.
                    Вам необходимо дополнительно отослать
                    следующую информацию:
                    - фотограцию животного;
                    - рацион животного;
                    - произошедшие изменения.
                    Для этого нажмите соответствующую кнопку
                    """)
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup));
            return true;
        } else if (isReportExist(update)) {

            Optional<ReportDog> report = reportsDogsRepository
                    .findByClientIdAndAnimalIdAndDateReport(clientId, dogId, LocalDate.now());

            if (report.isPresent() && (report.get().getFeeling() == null || report.get().getFeeling().isEmpty())) {

                report.get().setFeeling(text);
                report.get().setUpdatedAt(LocalDateTime.now());

                reportsDogsRepository.save(report.get());

                InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                telegramBot.execute(new SendMessage(chatId, """
                        *Введенный отчет *о самочувствии животного*
                        успешно сохранен*.
                        Выберите следующую команду:
                        """)
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(inlineKeyboardMarkup));
                return true;
            } else if (report.isPresent()) {

                String feeling = report.get().getFeeling();

                InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                telegramBot.execute(new SendMessage(chatId, String.format("""
                        Вы уже отсылали сегодня отчет
                        *о самочувствии животного*.
                        Вот он:
                        %s""", feeling))
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(inlineKeyboardMarkup));
                return true;
            }
        }
        return false;
    }

    /**
     * Method that receives changes that happened with the animal and stores it in database
     */
    @Override
    public boolean receiveChanges(Update update) {
        Long chatId = 0L;
        Integer date = null;
        String text = "";

        if (update.message() != null) {
            chatId = update.message().chat().id();
            date = update.message().date();
            text = update.message().text();
        } else if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
        }

        LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        Dog dog = dogsHandler.returnOneDogOnProbation(update);

        Long clientId = dog.getOwnerDog().getId();
        Long dogId = dog.getId();

        if (!isReportExist(update)) { // If report for today not exist

            ReportDog report = new ReportDog(); // Set new report with filePath
            report.setClientId(clientId);
            report.setAnimalId(dogId);
            report.setChanges(text);
            report.setCreatedAt(localDateTime);
            report.setDateReport(LocalDate.now());
            report.setDateLastReport(LocalDate.now().plusDays(DAYS_OF_PROBATION));

            reportsDogsRepository.save(report);

            InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
            telegramBot.execute(new SendMessage(chatId, """
                    Создан новый отчет за сегодняший день и
                    высланный отчет *об изменениях в поведении
                    животного* успешно сохранен.
                    Вам необходимо дополнительно отослать
                    следующую информацию:
                    - фотограцию животного;
                    - рацион животного;
                    - самочувствие животного.
                    Для этого нажмите соответствующую кнопку
                    """)
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup));
            return true;
        } else if (isReportExist(update)) {

            Optional<ReportDog> report = reportsDogsRepository
                    .findByClientIdAndAnimalIdAndDateReport(clientId, dogId, LocalDate.now());

            if (report.isPresent() && (report.get().getChanges() == null || report.get().getChanges().isEmpty())) {

                report.get().setChanges(text);
                report.get().setUpdatedAt(LocalDateTime.now());

                reportsDogsRepository.save(report.get());

                InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                telegramBot.execute(new SendMessage(chatId, """
                        **Введенный отчет *об изменениях в
                        поведении животного* успешно сохранен*.
                        Выберите следующую команду:
                        """)
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(inlineKeyboardMarkup));
                return true;
            } else if (report.isPresent() && !report.get().getRation().isEmpty()) {

                String changes = report.get().getChanges();

                InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                telegramBot.execute(new SendMessage(chatId, String.format("""
                        Вы уже отсылали сегодня отчет
                        *об изменениях в поведении животного*
                        Вот он:
                        %s""", changes))
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(inlineKeyboardMarkup));
                return true;
            }
        }
        return false;
    }

    /**
     * Method that checks if a probation period for the new owner of the animal set and valid
     */
    @Override
    public boolean checkForProbationPeriodSetAndValid(@NotNull Update update) {

        Long chatId = 0L;
        String userName = null;

        if (update.message() != null) {
            chatId = update.message().chat().id();
            userName = update.message().chat().username();
        } else if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
        }

        Dog dog = dogsHandler.returnOneDogOnProbation(update);

        LocalDate probationStarts = dog.getProbationStarts();
        LocalDate probationEnds = dog.getProbationEnds();

        String probationStartsString = String.format("%tF", probationStarts);
        String probationEndsString = String.format("%tF", probationEnds);


        if (probationStarts == null) {

            InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForInfoMenuButton();

            telegramBot.execute(new SendMessage(chatId, """
                    У вас еще не назначен испытательный срок.
                    Вы не можете отправлять отчет.
                    Если вы взяли животное, но видите это сообщение,
                    значит Ваш менеджер не установил Вам испытательный срок.
                    Мы отправили ему запрос на установление испытательного
                    срока. Попробуйте отослать отчет чуть позже.
                    Выберите следующую команду:""")
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup));

            OwnerDog ownerDog = dog.getOwnerDog();

            Long volunteerChatId = ownerDog.getVolunteer().getChatId();

            SendMessage sendMessage = new SendMessage(volunteerChatId, String.format("""
                    Клиент %s с chatId %d пытается отправить Вам 1-й
                    отчет по своему животному, но вы не установили
                    ему испытательный срок в системе. Пожалуйста,
                    установите ему испытательный срок.
                    """, userName, chatId));
            telegramBot.execute(sendMessage);

            return false;

        } else if ((LocalDate.now().isAfter(probationStarts) || LocalDate.now().isEqual(probationStarts)) &&
                (LocalDate.now().isBefore(probationEnds) || LocalDate.now().isEqual(probationEnds))) {

            return true;

        } else if (LocalDate.now().isBefore(probationStarts)) {

            InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formPriutMainMenuButton();

            SendMessage sendMessage = new SendMessage(chatId, String.format("""
                    У вас еще не начался испытательный срок.
                    Вы можете начать отправлять отчет начиная
                    с %s
                    """, probationStartsString))
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup);

            telegramBot.execute(sendMessage);
            return false;

        } else if (LocalDate.now().isAfter(probationEnds)) {

            InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formPriutMainMenuButton();

            SendMessage sendMessage = new SendMessage(chatId, String.format("""
                    У вас уже закончился испытательный срок
                    %s.
                    Вам не нужно больше отправлять отчет.
                    Выберите следующую команду:
                    """, probationEndsString))
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup);

            telegramBot.execute(sendMessage);
        }
        return false;
    }

    @Override
    public boolean isReportCompleted(Update update) {
        OwnerDog ownerDog = ownersDogsHandler.checkForOwnerExist(update);
        Dog dog = dogsHandler.returnOneDogOnProbation(update);

        Long clientId = ownerDog.getId();
        Long dogId = dog.getId();
        LocalDate date = LocalDate.now();

        Optional<ReportDog> reportDogOptional = reportsDogsRepository.findByClientIdAndAnimalIdAndDateReport(clientId, dogId, date);

        if (reportDogOptional.isPresent()) {

            ReportDog reportDog = reportDogOptional.get();

            return reportDog.getFileId() != null &&
                    reportDog.getRation() != null &&
                    reportDog.getFeeling() != null &&
                    reportDog.getChanges() != null;
        }
        return false;
    }

    @Override
    public boolean isPhoto(Update update) {
        OwnerDog ownerDog = ownersDogsHandler.checkForOwnerExist(update);
        Dog dog = dogsHandler.returnOneDogOnProbation(update);

        Long clientId = ownerDog.getId();
        Long dogId = dog.getId();
        LocalDate date = LocalDate.now();

        Optional<ReportDog> reportDogOptional = reportsDogsRepository.findByClientIdAndAnimalIdAndDateReport(clientId, dogId, date);

        if (reportDogOptional.isPresent()) {

            ReportDog reportDog = reportDogOptional.get();

            return reportDog.getFileId() != null;
        }
        return false;
    }

    @Override
    public boolean isRation(Update update) {
        OwnerDog ownerDog = ownersDogsHandler.checkForOwnerExist(update);
        Dog dog = dogsHandler.returnOneDogOnProbation(update);

        Long clientId = ownerDog.getId();
        Long dogId = dog.getId();
        LocalDate date = LocalDate.now();

        Optional<ReportDog> reportDogOptional = reportsDogsRepository.findByClientIdAndAnimalIdAndDateReport(clientId, dogId, date);

        if (reportDogOptional.isPresent()) {

            ReportDog reportDog = reportDogOptional.get();

            return reportDog.getRation() != null;
        }
        return false;
    }

    @Override
    public boolean isFeeling(Update update) {
        OwnerDog ownerDog = ownersDogsHandler.checkForOwnerExist(update);
        Dog dog = dogsHandler.returnOneDogOnProbation(update);

        Long clientId = ownerDog.getId();
        Long dogId = dog.getId();
        LocalDate date = LocalDate.now();

        Optional<ReportDog> reportDogOptional = reportsDogsRepository.findByClientIdAndAnimalIdAndDateReport(clientId, dogId, date);

        if (reportDogOptional.isPresent()) {

            ReportDog reportDog = reportDogOptional.get();

            return reportDog.getFeeling() != null;
        }
        return false;
    }

    @Override
    public boolean isChanges(Update update) {
        OwnerDog ownerDog = ownersDogsHandler.checkForOwnerExist(update);
        Dog dog = dogsHandler.returnOneDogOnProbation(update);

        Long clientId = ownerDog.getId();
        Long dogId = dog.getId();
        LocalDate date = LocalDate.now();

        Optional<ReportDog> reportDogOptional = reportsDogsRepository.findByClientIdAndAnimalIdAndDateReport(clientId, dogId, date);

        if (reportDogOptional.isPresent()) {

            ReportDog reportDog = reportDogOptional.get();

            return reportDog.getChanges() != null;
        }
        return false;
    }

}