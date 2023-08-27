package omg.group.priuttelegrambot.handlers.reports.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import omg.group.priuttelegrambot.entity.pets.Cat;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.entity.reports.ReportCat;
import omg.group.priuttelegrambot.handlers.menu.CatsMenuHandler;
import omg.group.priuttelegrambot.handlers.menu.MainMenuHandler;
import omg.group.priuttelegrambot.handlers.owners.OwnersCatsHandler;
import omg.group.priuttelegrambot.handlers.pets.CatsHandler;
import omg.group.priuttelegrambot.handlers.photo.PhotoHandler;
import omg.group.priuttelegrambot.handlers.reports.ReportsCatsHandler;
import omg.group.priuttelegrambot.handlers.updates.OwnUpdatesHandler;
import omg.group.priuttelegrambot.repository.reports.ReportsCatsRepository;
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
    private final MainMenuHandler mainMenuHandler;
    private final OwnUpdatesHandler ownUpdatesHandler;

    public ReportsCatsHandlerImpl(TelegramBot telegramBot,
                                  OwnersCatsHandler ownersCatsHandler,
                                  CatsHandler catsHandler,
                                  ReportsCatsRepository reportsCatsRepository,
                                  CatsMenuHandler catsMenuHandler,
                                  PhotoHandler photoHandler,
                                  MainMenuHandler mainMenuHandler,
                                  OwnUpdatesHandler ownUpdatesHandler) {
        this.ownersCatsHandler = ownersCatsHandler;
        this.catsHandler = catsHandler;
        this.catsMenuHandler = catsMenuHandler;
        this.telegramBot = telegramBot;
        this.reportsCatsRepository = reportsCatsRepository;
        this.photoHandler = photoHandler;
        this.mainMenuHandler = mainMenuHandler;
        this.ownUpdatesHandler = ownUpdatesHandler;
    }

    /**
     * Method checks if report for today exist in ReportsCatsRepository
     */
    @Override
    public ReportCat isReportExist(Update update) {

        Cat cat = catsHandler.returnOneCatOnProbation(update);
        OwnerCat owner = cat.getOwner();

        Optional<ReportCat> report = reportsCatsRepository.findReportCatByOwnerAndCatAndDateReport(owner, cat, LocalDate.now());

        return report.orElse(null);
    }

    @Override
    public ReportCat isReportCompleted(ReportCat report) {

        if (report != null) {
            if (report.getFileId() != null && report.getRation() != null && report.getFeeling() != null &&
                    report.getChanges() != null) {
                return null;
            } else {
                return report;
            }
        }
        return null;
    }

    @Override
    public ReportCat returnReportFromUpdate(Update update) {
        ReportCat report = isReportExist(update);
        if (report != null) {
            ReportCat reportCompleted = isReportCompleted(report);
            if (reportCompleted != null) {
                return reportCompleted;
            }
        }
        return null;
    }

    @Override
    public ReportCat isPhoto(ReportCat report) {
        if (report != null) {
            if (report.getFileId() != null) {
                return null;
            } else {
                return report;
            }
        }
        return null;
    }

    /**
     * Method that receives photo and stores it in database
     */
    @Override
    @Transactional
    public boolean receivePhoto(Update update) {

        Long chatId = ownUpdatesHandler.extractChatIdFromUpdate(update);
        int messageId = ownUpdatesHandler.extractMessageIdFromUpdate(update);
        int date = ownUpdatesHandler.extractDateFromUpdate(update);

        // Check for report for today exist. If exist return Report, if not exist return null
        ReportCat report = isReportExist(update);

        if (report != null) {
            // If Report exist check for photo is set. If photo was not set - return Report, otherwise return null
            ReportCat reportWithoutPhoto = isPhoto(report);
            if (reportWithoutPhoto != null) {
                // If photo was not set check for Update has a photo
                if (update.message() != null && update.message().photo() != null) {
                    // If update has a photo get photo from Update
                    String fileId = photoHandler.getFileIdFromUpdate(update);
                    String savePath = photoHandler.downloadFileByFileId(fileId);
                    byte[] fileAsArray = photoHandler.getFileAsArray(savePath);
                    int hashOfPhoto = photoHandler.getHashOfFile(fileAsArray);
                    LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();
                    // Check if photo from update already is in reports_cats database
                    if (reportsCatsRepository.findByHashOfPhoto(hashOfPhoto).isPresent()) {
                        //If photo already set in reports_cats database sent appropriate message
                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                        mainMenuHandler.photoDublicateSentMessage(chatId, messageId, inlineKeyboardMarkup);
                        return false;
                    } else {
                        reportWithoutPhoto.setFileId(fileId);
                        reportWithoutPhoto.setUpdatedAt(localDateTime);
                        reportWithoutPhoto.setHashOfPhoto(hashOfPhoto);
                        reportsCatsRepository.save(reportWithoutPhoto);
                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                        mainMenuHandler.photoSavedOkMessage(chatId, messageId, inlineKeyboardMarkup);
                        return true;
                    }
                }
            }
            InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
            mainMenuHandler.photoAlreadySentMessage(chatId, messageId, inlineKeyboardMarkup);
            return false;
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
        mainMenuHandler.notPhotoMessage(chatId, messageId, inlineKeyboardMarkup);
        return false;
    }

    @Override
    public ReportCat isRation(ReportCat report) {
        if (report != null) {
            if (report.getRation() != null) {
                return null;
            } else {
                return report;
            }
        }
        return null;
    }

    /**
     * Method that receives ration of the animal and stores it in database
     */
    @Override
    @Transactional
    public boolean receiveRation(Update update) {

        Long chatId = ownUpdatesHandler.extractChatIdFromUpdate(update);
        int messageId = ownUpdatesHandler.extractMessageIdFromUpdate(update);
        int date = ownUpdatesHandler.extractDateFromUpdate(update);

        // Check for report for today exist. If exist return Report, if not exist return null
        ReportCat report = isReportExist(update);

        if (report != null) {
            // If Report exist check for ration is set. If ration was not set - return Report, otherwise return null
            ReportCat reportWithoutRation = isRation(report);
            if (reportWithoutRation != null) {
                // If ration was not set check for Update has a text
                if (update.message() != null && update.message().text() != null) {
                    // If update has a text get text from Update
                    String text = ownUpdatesHandler.extractTextFromUpdate(update);
                    LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();
                    // Check if ration from update already is in reports_cats database
                    reportWithoutRation.setRation(text);
                    reportWithoutRation.setUpdatedAt(localDateTime);
                    reportsCatsRepository.save(reportWithoutRation);
                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                    mainMenuHandler.rationSavedOkMessage(chatId, messageId, inlineKeyboardMarkup);
                    return true;
                }
            }
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
        mainMenuHandler.rationAlreadySentMessage(chatId, messageId, inlineKeyboardMarkup);
        return false;
    }

    @Override
    public ReportCat isFeeling(ReportCat report) {
        if (report != null) {
            if (report.getFeeling() != null) {
                return null;
            } else {
                return report;
            }
        }
        return null;
    }

    /**
     * Method that receives self-feeling of the animal and stores it in database
     */
    @Override
    public boolean receiveFeeling(Update update) {

        Long chatId = ownUpdatesHandler.extractChatIdFromUpdate(update);
        int messageId = ownUpdatesHandler.extractMessageIdFromUpdate(update);
        int date = ownUpdatesHandler.extractDateFromUpdate(update);

        // Check for report for today exist. If exist return Report, if not exist return null
        ReportCat report = isReportExist(update);

        if (report != null) {
            // If Report exist check for feeling is set. If feeling was not set - return Report, otherwise return null
            ReportCat reportWithoutFeeling = isFeeling(report);
            if (reportWithoutFeeling != null) {
                // If feeling was not set check for Update has a text
                if (update.message() != null && update.message().text() != null) {
                    // If update has a text get text from Update
                    String text = ownUpdatesHandler.extractTextFromUpdate(update);
                    LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();
                    // Check if feeling from update already is in reports_cats database
                    reportWithoutFeeling.setFeeling(text);
                    reportWithoutFeeling.setUpdatedAt(localDateTime);
                    reportsCatsRepository.save(reportWithoutFeeling);
                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                    mainMenuHandler.feelingSavedOkMessage(chatId, messageId, inlineKeyboardMarkup);
                    return true;
                }
            }
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
        mainMenuHandler.feelingAlreadySentMessage(chatId, messageId, inlineKeyboardMarkup);
        return false;
    }

    @Override
    public ReportCat isChanges(ReportCat report) {
        if (report != null) {
            if (report.getChanges() != null) {
                return null;
            } else {
                return report;
            }
        }
        return null;
    }

    /**
     * Method that receives changes that happened with the animal and stores it in database
     */
    @Override
    public boolean receiveChanges(Update update) {

        Long chatId = ownUpdatesHandler.extractChatIdFromUpdate(update);
        int messageId = ownUpdatesHandler.extractMessageIdFromUpdate(update);
        int date = ownUpdatesHandler.extractDateFromUpdate(update);

        // Check for report for today exist. If exist return Report, if not exist return null
        ReportCat report = isReportExist(update);

        if (report != null) {
            // If Report exist check for changes is set. If changes was not set - return Report, otherwise return null
            ReportCat reportWithoutChanges = isChanges(report);
            if (reportWithoutChanges != null) {
                // If changes was not set check for Update has a text
                if (update.message() != null && update.message().text() != null) {
                    // If update has a text get text from Update
                    String text = ownUpdatesHandler.extractTextFromUpdate(update);
                    LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();
                    // Check if photo from update already is in reports_cats database
                    reportWithoutChanges.setChanges(text);
                    reportWithoutChanges.setUpdatedAt(localDateTime);
                    reportsCatsRepository.save(reportWithoutChanges);
                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                    mainMenuHandler.changesSavedOkMessage(chatId, messageId, inlineKeyboardMarkup);
                    return true;
                }
            }
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
        mainMenuHandler.changesAlreadySentMessage(chatId, messageId, inlineKeyboardMarkup);
        return false;
    }


    /**
     * Method that checks if a probation period for the new owner of the animal set and valid
     */
//    @Override
//    public boolean checkForProbationPeriodSetAndValid(Update update) {
//
//        Long chatId = 0L;
//        String userName = null;
//
//        if (update.message() != null) {
//            chatId = update.message().chat().id();
//            userName = update.message().chat().username();
//        } else if (update.callbackQuery() != null) {
//            chatId = update.callbackQuery().message().chat().id();
//        }
//
//        Cat cat = catsHandler.returnOneCatOnProbation(update);
//
//        if (cat != null) {
//            LocalDate probationStarts = cat.getProbationStarts();
//            LocalDate probationEnds = cat.getProbationEnds();
//
//            String probationStartsString = String.format("%tF", probationStarts);
//            String probationEndsString = String.format("%tF", probationEnds);
//
//
//            if (probationStarts == null) {
//
//                InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForInfoMenuButton();
//
//                telegramBot.execute(new SendMessage(chatId, """
//                        У вас еще не назначен испытательный срок. Вы не можете отправлять отчет. Если вы взяли животное, но видите это сообщение, значит Ваш менеджер не установил Вам испытательный срок.
//                        Мы отправили ему запрос на установление испытательного срока. Попробуйте отослать отчет чуть позже.
//                        Выберите следующую команду:""")
//                        .parseMode(ParseMode.Markdown)
//                        .replyMarkup(inlineKeyboardMarkup));
//
//                OwnerCat ownerCat = cat.getOwnerCat();
//
//                Long volunteerChatId = ownerCat.getVolunteer().getChatId();
//
//                SendMessage sendMessage = new SendMessage(volunteerChatId, String.format("""
//                        Клиент %s с chatId %d пытается отправить Вам 1-й отчет по своему животному, но вы не установили ему испытательный срок в системе.
//                        Пожалуйста,установите ему испытательный срок.
//                        """, userName, chatId));
//                telegramBot.execute(sendMessage);
//
//                return false;
//
//            } else if ((LocalDate.now().isAfter(probationStarts) || LocalDate.now().isEqual(probationStarts)) &&
//                    (LocalDate.now().isBefore(probationEnds) || LocalDate.now().isEqual(probationEnds))) {
//
//                return true;
//
//            } else if (LocalDate.now().isBefore(probationStarts)) {
//
//                InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formPriutMainMenuButton();
//
//                SendMessage sendMessage = new SendMessage(chatId, String.format("""
//                        У вас еще не начался испытательный срок. Вы можете начать отправлять отчет начиная с %s
//                        """, probationStartsString))
//                        .parseMode(ParseMode.Markdown)
//                        .replyMarkup(inlineKeyboardMarkup);
//
//                telegramBot.execute(sendMessage);
//
//                return false;
//
//            } else if (LocalDate.now().isAfter(probationEnds)) {
//
//                InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formPriutMainMenuButton();
//
//                SendMessage sendMessage = new SendMessage(chatId, String.format("""
//                        У вас уже закончился испытательный срок %s. Вам не нужно больше отправлять отчет.
//                        Выберите следующую команду:
//                        """, probationEndsString))
//                        .parseMode(ParseMode.Markdown)
//                        .replyMarkup(inlineKeyboardMarkup);
//
//                telegramBot.execute(sendMessage);
//                return false;
//            }
//        }
//        return false;
//    }


}



