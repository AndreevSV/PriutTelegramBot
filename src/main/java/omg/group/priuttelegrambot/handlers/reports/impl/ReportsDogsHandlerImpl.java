package omg.group.priuttelegrambot.handlers.reports.impl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.entity.pets.Dog;
import omg.group.priuttelegrambot.entity.reports.ReportDog;
import omg.group.priuttelegrambot.handlers.menu.DogsMenuHandler;
import omg.group.priuttelegrambot.handlers.menu.MainMenuHandler;
import omg.group.priuttelegrambot.handlers.pets.DogsHandler;
import omg.group.priuttelegrambot.handlers.photo.PhotoHandler;
import omg.group.priuttelegrambot.handlers.reports.ReportsDogsHandler;
import omg.group.priuttelegrambot.handlers.updates.OwnUpdatesHandler;
import omg.group.priuttelegrambot.repository.reports.ReportsDogsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class ReportsDogsHandlerImpl implements ReportsDogsHandler {
    private final DogsHandler dogsHandler;
    private final ReportsDogsRepository reportsDogsRepository;
    private final DogsMenuHandler dogsMenuHandler;
    private final PhotoHandler photoHandler;
    private final MainMenuHandler mainMenuHandler;
    private final OwnUpdatesHandler ownUpdatesHandler;

    public ReportsDogsHandlerImpl(DogsHandler dogsHandler,
                                  ReportsDogsRepository reportsDogsRepository,
                                  DogsMenuHandler dogsMenuHandler,
                                  PhotoHandler photoHandler,
                                  MainMenuHandler mainMenuHandler,
                                  OwnUpdatesHandler ownUpdatesHandler) {
        this.dogsHandler = dogsHandler;
        this.reportsDogsRepository = reportsDogsRepository;
        this.dogsMenuHandler = dogsMenuHandler;
        this.photoHandler = photoHandler;
        this.mainMenuHandler = mainMenuHandler;
        this.ownUpdatesHandler = ownUpdatesHandler;
    }

    /**
     * Method checks if report for today exist in ReportsDogsRepository
     */
    @Override
    public ReportDog isReportExist(Update update) {

        Dog dog = dogsHandler.returnOneDogOnProbation(update);
        OwnerDog owner = dog.getOwner();

        Optional<ReportDog> report = reportsDogsRepository.findReportDogByOwnerDogAndDogAndDateReport(owner, dog, LocalDate.now());

        return report.orElse(null);
    }

    @Override
    public ReportDog isReportCompleted(ReportDog report) {

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
    public ReportDog returnReportFromUpdate(Update update) {
        ReportDog report = isReportExist(update);
        if (report != null) {
            ReportDog reportCompleted = isReportCompleted(report);
            if (reportCompleted != null) {
                return reportCompleted;
            }
        }
        return null;
    }

    @Override
    public ReportDog isPhoto(ReportDog report) {
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
        ReportDog report = isReportExist(update);

        if (report != null) {
            // If Report exist check for photo is set. If photo was not set - return Report, otherwise return null
            ReportDog reportWithoutPhoto = isPhoto(report);
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
                    // Check if photo from update already is in reports_dogs database
                    if (reportsDogsRepository.findByHashOfPhoto(hashOfPhoto).isPresent()) {
                        //If photo already set in reports_dogs database sent appropriate message
                        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                        mainMenuHandler.photoDublicateSentMessage(chatId, messageId, inlineKeyboardMarkup);
                        return false;
                    } else {
                        reportWithoutPhoto.setFileId(fileId);
                        reportWithoutPhoto.setUpdatedAt(localDateTime);
                        reportWithoutPhoto.setHashOfPhoto(hashOfPhoto);
                        reportsDogsRepository.save(reportWithoutPhoto);
                        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                        mainMenuHandler.photoSavedOkMessage(chatId, messageId, inlineKeyboardMarkup);
                        return true;
                    }
                }
            }
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
        mainMenuHandler.notPhotoMessage(chatId, messageId, inlineKeyboardMarkup);
        return false;
    }

    @Override
    public ReportDog isRation(ReportDog report) {
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
        ReportDog report = isReportExist(update);

        if (report != null) {
            // If Report exist check for ration is set. If ration was not set - return Report, otherwise return null
            ReportDog reportWithoutRation = isRation(report);
            if (reportWithoutRation != null) {
                // If ration was not set check for Update has a text
                if (update.message() != null && update.message().text() != null) {
                    // If update has a text get text from Update
                    String text = ownUpdatesHandler.extractTextFromUpdate(update);
                    LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();
                    // Check if ration from update already is in reports_dogs database
                    reportWithoutRation.setRation(text);
                    reportWithoutRation.setUpdatedAt(localDateTime);
                    reportsDogsRepository.save(reportWithoutRation);
                    InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                    mainMenuHandler.rationSavedOkMessage(chatId, messageId, inlineKeyboardMarkup);
                    return true;
                }
            }
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
        mainMenuHandler.rationAlreadySentMessage(chatId, messageId, inlineKeyboardMarkup);
        return false;
    }

    @Override
    public ReportDog isFeeling(ReportDog report) {
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
        ReportDog report = isReportExist(update);

        if (report != null) {
            // If Report exist check for feeling is set. If feeling was not set - return Report, otherwise return null
            ReportDog reportWithoutFeeling = isFeeling(report);
            if (reportWithoutFeeling != null) {
                // If feeling was not set check for Update has a text
                if (update.message() != null && update.message().text() != null) {
                    // If update has a text get text from Update
                    String text = ownUpdatesHandler.extractTextFromUpdate(update);
                    LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();
                    // Check if feeling from update already is in reports_dogs database
                    reportWithoutFeeling.setFeeling(text);
                    reportWithoutFeeling.setUpdatedAt(localDateTime);
                    reportsDogsRepository.save(reportWithoutFeeling);
                    InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                    mainMenuHandler.feelingSavedOkMessage(chatId, messageId, inlineKeyboardMarkup);
                    return true;
                }
            }
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
        mainMenuHandler.feelingAlreadySentMessage(chatId, messageId, inlineKeyboardMarkup);
        return false;
    }

    @Override
    public ReportDog isChanges(ReportDog report) {
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
        ReportDog report = isReportExist(update);

        if (report != null) {
            // If Report exist check for changes is set. If changes was not set - return Report, otherwise return null
            ReportDog reportWithoutChanges = isChanges(report);
            if (reportWithoutChanges != null) {
                // If changes was not set check for Update has a text
                if (update.message() != null && update.message().text() != null) {
                    // If update has a text get text from Update
                    String text = ownUpdatesHandler.extractTextFromUpdate(update);
                    LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();
                    // Check if photo from update already is in reports_dogs database
                    reportWithoutChanges.setChanges(text);
                    reportWithoutChanges.setUpdatedAt(localDateTime);
                    reportsDogsRepository.save(reportWithoutChanges);
                    InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                    mainMenuHandler.changesSavedOkMessage(chatId, messageId, inlineKeyboardMarkup);
                    return true;
                }
            }
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
        mainMenuHandler.changesAlreadySentMessage(chatId, messageId, inlineKeyboardMarkup);
        return false;
    }

}