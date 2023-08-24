package omg.group.priuttelegrambot.handlers.reports.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import omg.group.priuttelegrambot.entity.pets.Cat;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.entity.reports.ReportCat;
import omg.group.priuttelegrambot.handlers.menu.CatsMenuHandler;
import omg.group.priuttelegrambot.handlers.owners.OwnersCatsHandler;
import omg.group.priuttelegrambot.handlers.pets.CatsHandler;
import omg.group.priuttelegrambot.handlers.photo.PhotoHandler;
import omg.group.priuttelegrambot.handlers.reports.ReportsCatsHandler;
import omg.group.priuttelegrambot.repository.reports.ReportsCatsRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class ReportsCatsHandlerImpl implements ReportsCatsHandler {

    private final int DAYS_OF_PROBATION = 14;

    @Value("${telegram.bot.token}")
    private String token;
    private final TelegramBot telegramBot;
    private final OwnersCatsHandler ownersCatsHandler;
    private final CatsHandler catsHandler;
    private final ReportsCatsRepository reportsCatsRepository;
    private final CatsMenuHandler catsMenuHandler;
    private final PhotoHandler photoHandler;

    public ReportsCatsHandlerImpl(TelegramBot telegramBot,
                                  OwnersCatsHandler ownersCatsHandler,
                                  CatsHandler catsHandler, ReportsCatsRepository reportsCatsRepository,
                                  CatsMenuHandler catsMenuHandler,
                                  PhotoHandler photoHandler) {
        this.ownersCatsHandler = ownersCatsHandler;
        this.catsHandler = catsHandler;
        this.catsMenuHandler = catsMenuHandler;
        this.telegramBot = telegramBot;
        this.reportsCatsRepository = reportsCatsRepository;
        this.photoHandler = photoHandler;
    }

    /**
     * Method checks if report for today exist in ReportsCatsRepository
     */
    @Override
    public boolean isReportExist(Update update) {

        Cat cat = catsHandler.returnOneCatOnProbation(update);

        Long animalId = cat.getId();
        Long clientId = cat.getOwnerCat().getId();

        Optional<ReportCat> report = reportsCatsRepository
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

                Cat cat = catsHandler.returnOneCatOnProbation(update);

                Long clientId = cat.getOwnerCat().getId();
                Long catId = cat.getId();

                if (!isReportExist(update)) { // If report for today not exist

                    ReportCat report = new ReportCat(); // Set new report with filePath
                    report.setClientId(clientId);
                    report.setAnimalId(catId);
                    report.setFileId(fileId);
                    report.setCreatedAt(localDateTime);
                    report.setDateReport(LocalDate.now());
                    report.setDateLastReport(LocalDate.now().plusDays(DAYS_OF_PROBATION));
                    report.setHashOfPhoto(hashOfPhoto);

                    reportsCatsRepository.save(report); // Save photoPath in report

                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
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

                    Optional<ReportCat> report = reportsCatsRepository
                            .findByClientIdAndAnimalIdAndDateReport(clientId, catId, LocalDate.now());

                    if (report.isPresent() &&
                            report.get().getFileId().isEmpty() &&
                            reportsCatsRepository.findByHashOfPhoto(hashOfPhoto).isEmpty()) {

                        report.get().setFileId(fileId);
                        report.get().setUpdatedAt(LocalDateTime.now());
                        report.get().setHashOfPhoto(hashOfPhoto);

                        reportsCatsRepository.save(report.get());

                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                        telegramBot.execute(new SendMessage(chatId, """
                                *Фотография успешно сохранена*.
                                Выберите следующую команду:""")
                                .parseMode(ParseMode.Markdown)
                                .replyMarkup(inlineKeyboardMarkup));
                        return true;
                    } else if (report.isPresent() &&
                            report.get().getFileId().isEmpty() &&
                            reportsCatsRepository.findByHashOfPhoto(hashOfPhoto).isPresent()) {

                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                        telegramBot.execute(new SendMessage(chatId, """
                                Вы уже отсылали такую фотографию.
                                Отошлите свежую фотографию.""")
                                .parseMode(ParseMode.Markdown)
                                .replyMarkup(inlineKeyboardMarkup));
                        return false;
                    } else if (report.isPresent() &&
                            !report.get().getFileId().isEmpty()) {

                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
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
                InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
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

        if (checkForProbationPeriodSetAndValid(update)) {
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

            Cat cat = catsHandler.returnOneCatOnProbation(update);

            Long clientId = cat.getOwnerCat().getId();
            Long catId = cat.getId();

            if (!isReportExist(update)) { // If report for today not exist

                ReportCat report = new ReportCat(); // Set new report with filePath
                report.setClientId(clientId);
                report.setAnimalId(catId);
                report.setRation(text);
                report.setCreatedAt(localDateTime);
                report.setDateReport(LocalDate.now());
                report.setDateLastReport(LocalDate.now().plusDays(DAYS_OF_PROBATION));

                reportsCatsRepository.save(report);

                InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                telegramBot.execute(new SendMessage(chatId, """
                        Создан новый отчет за сегодняший
                        день и высланный рацион успешно
                        сохранен. Вам необходимо
                        дополнительно отослать следующую
                        информацию:
                        - фотограцию животного;
                        - самочувствие животного;
                        - произошедшие изменения.
                        Для этого нажмите соответствующую
                        кнопку
                        """)
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(inlineKeyboardMarkup));

                return true;
            } else if (isReportExist(update)) {

                Optional<ReportCat> report = reportsCatsRepository
                        .findByClientIdAndAnimalIdAndDateReport(clientId, catId, LocalDate.now());

                if (report.isPresent() && (report.get().getRation() == null || report.get().getRation().isEmpty())) {

                    report.get().setRation(text);
                    report.get().setUpdatedAt(LocalDateTime.now());

                    reportsCatsRepository.save(report.get());

                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                    telegramBot.execute(new SendMessage(chatId, """
                            *Введеный *рацион* успешно сохранен*.
                            Выберите следующую команду:
                            """)
                            .parseMode(ParseMode.Markdown)
                            .replyMarkup(inlineKeyboardMarkup));
                    return true;
                } else if (report.isPresent()) {

                    String ration = report.get().getRation();

                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                    telegramBot.execute(new SendMessage(chatId, String.format("""
                            Вы уже отсылали сегодня рацион.
                            Вот он:
                            %s""", ration))
                            .parseMode(ParseMode.Markdown)
                            .replyMarkup(inlineKeyboardMarkup));
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method that receives self-feeling of the animal and stores it in database
     */
    @Override
    public boolean receiveFeeling(Update update) {

        if (checkForProbationPeriodSetAndValid(update)) {
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

            Cat cat = catsHandler.returnOneCatOnProbation(update);

            Long clientId = cat.getOwnerCat().getId();
            Long catId = cat.getId();

            if (!isReportExist(update)) { // If report for today not exist

                ReportCat report = new ReportCat(); // Set new report with filePath
                report.setClientId(clientId);
                report.setAnimalId(catId);
                report.setFeeling(text);
                report.setCreatedAt(localDateTime);
                report.setDateReport(LocalDate.now());
                report.setDateLastReport(LocalDate.now().plusDays(DAYS_OF_PROBATION));

                reportsCatsRepository.save(report);

                InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                telegramBot.execute(new SendMessage(chatId, """
                        Создан новый отчет за сегодняший
                        день и высланный отчет о *самочувствии
                        животного* успешно сохранен. Вам
                        необходимо дополнительно отослать
                        следующую информацию:
                        - фотограцию животного;
                        - рацион животного;
                        - произошедшие изменения.
                        Для этого нажмите соответствующую
                        кнопку
                        """)
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(inlineKeyboardMarkup));
                return true;
            } else if (isReportExist(update)) {

                Optional<ReportCat> report = reportsCatsRepository
                        .findByClientIdAndAnimalIdAndDateReport(clientId, catId, LocalDate.now());

                if (report.isPresent() && (report.get().getFeeling() == null || report.get().getFeeling().isEmpty())) {

                    report.get().setFeeling(text);
                    report.get().setUpdatedAt(LocalDateTime.now());

                    reportsCatsRepository.save(report.get());

                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                    telegramBot.execute(new SendMessage(chatId, """
                            *Введенный отчет *о самочувствии
                            животного* успешно сохранен*.
                            Выберите следующую команду:
                            """)
                            .parseMode(ParseMode.Markdown)
                            .replyMarkup(inlineKeyboardMarkup));
                    return true;
                } else if (report.isPresent()) {

                    String feeling = report.get().getFeeling();

                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
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
        }
        return false;
    }

    /**
     * Method that receives changes that happened with the animal and stores it in database
     */
    @Override
    public boolean receiveChanges(Update update) {

        if (checkForProbationPeriodSetAndValid(update)) {
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

            Cat cat = catsHandler.returnOneCatOnProbation(update);

            Long clientId = cat.getOwnerCat().getId();
            Long catId = cat.getId();

            if (!isReportExist(update)) { // If report for today not exist

                ReportCat report = new ReportCat(); // Set new report with filePath
                report.setClientId(clientId);
                report.setAnimalId(catId);
                report.setChanges(text);
                report.setCreatedAt(localDateTime);
                report.setDateReport(LocalDate.now());
                report.setDateLastReport(LocalDate.now().plusDays(DAYS_OF_PROBATION));

                reportsCatsRepository.save(report);

                InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                telegramBot.execute(new SendMessage(chatId, """
                        Создан новый отчет за сегодняший
                        день и высланный отчет *об изменениях
                        в поведении животного* успешно сохранен.
                        Вам необходимо дополнительно отослать
                        следующую информацию:
                        - фотограцию животного;
                        - рацион животного;
                        - самочувствие животного.
                        Для этого нажмите соответствующую
                        кнопку
                        """)
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(inlineKeyboardMarkup));
                return true;
            } else if (isReportExist(update)) {

                Optional<ReportCat> report = reportsCatsRepository
                        .findByClientIdAndAnimalIdAndDateReport(clientId, catId, LocalDate.now());

                if (report.isPresent() && (report.get().getChanges() == null || report.get().getChanges().isEmpty())) {

                    report.get().setChanges(text);
                    report.get().setUpdatedAt(LocalDateTime.now());

                    reportsCatsRepository.save(report.get());

                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                    telegramBot.execute(new SendMessage(chatId, """
                            **Введенный отчет *об изменениях в
                            поведении животного* успешно сохранен*.
                            Выберите следующую команду:
                            """)
                            .parseMode(ParseMode.Markdown)
                            .replyMarkup(inlineKeyboardMarkup));
                    return true;
                } else if (report.isPresent()) {

                    String changes = report.get().getChanges();

                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
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

        Cat cat = catsHandler.returnOneCatOnProbation(update);

        if (cat != null) {
            LocalDate probationStarts = cat.getProbationStarts();
            LocalDate probationEnds = cat.getProbationEnds();

            String probationStartsString = String.format("%tF", probationStarts);
            String probationEndsString = String.format("%tF", probationEnds);


            if (probationStarts == null) {

                InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForInfoMenuButton();

                telegramBot.execute(new SendMessage(chatId, """
                        У вас еще не назначен испытательный
                        срок. Вы не можете отправлять отчет.
                        Если вы взяли животное, но видите это
                        сообщение, значит Ваш менеджер не
                        установил Вам испытательный срок.
                        Мы отправили ему запрос на установление
                        испытательного срока. Попробуйте
                        отослать отчет чуть позже.
                        Выберите следующую команду:""")
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(inlineKeyboardMarkup));

                OwnerCat ownerCat = cat.getOwnerCat();

                Long volunteerChatId = ownerCat.getVolunteer().getChatId();

                SendMessage sendMessage = new SendMessage(volunteerChatId, String.format("""
                        Клиент %s с chatId %d пытается
                        отправить Вам 1-й отчет по своему
                        животному, но вы не установили
                        ему испытательный срок в системе.
                        Пожалуйста,установите ему
                        испытательный срок.
                        """, userName, chatId));
                telegramBot.execute(sendMessage);

                return false;

            } else if ((LocalDate.now().isAfter(probationStarts) || LocalDate.now().isEqual(probationStarts)) &&
                    (LocalDate.now().isBefore(probationEnds) || LocalDate.now().isEqual(probationEnds))) {

                return true;

            } else if (LocalDate.now().isBefore(probationStarts)) {

                InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formPriutMainMenuButton();

                SendMessage sendMessage = new SendMessage(chatId, String.format("""
                        У вас еще не начался испытательный
                        срок. Вы можете начать отправлять
                        отчет начиная с %s
                        """, probationStartsString))
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(inlineKeyboardMarkup);

                telegramBot.execute(sendMessage);

                return false;

            } else if (LocalDate.now().isAfter(probationEnds)) {

                InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formPriutMainMenuButton();

                SendMessage sendMessage = new SendMessage(chatId, String.format("""
                        У вас уже закончился испытательный срок
                        %s.
                        Вам не нужно больше отправлять отчет.
                        Выберите следующую команду:
                        """, probationEndsString))
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(inlineKeyboardMarkup);

                telegramBot.execute(sendMessage);
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean isReportCompleted(Update update) {
        Optional<ReportCat> reportCatOptional = returnReportCatOptional(update);

        if (reportCatOptional.isPresent()) {
            ReportCat reportCat = reportCatOptional.get();
            return reportCat.getFileId() != null &&
                    reportCat.getRation() != null &&
                    reportCat.getFeeling() != null &&
                    reportCat.getChanges() != null;
        }
        return false;
    }

    @Override
    public boolean isPhoto(Update update) {
        Optional<ReportCat> reportCatOptional = returnReportCatOptional(update);

        if (reportCatOptional.isPresent()) {
            ReportCat reportCat = reportCatOptional.get();
            return reportCat.getFileId() != null;
        }
        return false;
    }

    @Override
    public boolean isRation(Update update) {
        Optional<ReportCat> reportCatOptional = returnReportCatOptional(update);

        if (reportCatOptional.isPresent()) {
            ReportCat reportCat = reportCatOptional.get();
            return reportCat.getRation() != null;
        }
        return false;
    }

    @Override
    public boolean isFeeling(Update update) {
        Optional<ReportCat> reportCatOptional = returnReportCatOptional(update);

        if (reportCatOptional.isPresent()) {
            ReportCat reportCat = reportCatOptional.get();
            return reportCat.getFeeling() != null;
        }
        return false;
    }

    @Override
    public boolean isChanges(Update update) {
        Optional<ReportCat> reportCatOptional = returnReportCatOptional(update);

        if (reportCatOptional.isPresent()) {
            ReportCat reportCat = reportCatOptional.get();
            return reportCat.getChanges() != null;
        }
        return false;
    }

    @Override
    public Optional<ReportCat> returnReportCatOptional(Update update) {

        OwnerCat ownerCat = ownersCatsHandler.checkForOwnerExist(update);
        Cat cat = catsHandler.returnOneCatOnProbation(update);

        Long clientId = ownerCat.getId();
        Long catId = cat.getId();
        LocalDate date = LocalDate.now();

        return reportsCatsRepository.findByClientIdAndAnimalIdAndDateReport(clientId, catId, date);

    }

}



