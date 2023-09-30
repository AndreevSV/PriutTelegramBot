package omg.group.priuttelegrambot.handlers.reports.impl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import omg.group.priuttelegrambot.dto.pets.CatDto;
import omg.group.priuttelegrambot.dto.pets.CatsMapper;
import omg.group.priuttelegrambot.dto.reports.ReportsCatsDto;
import omg.group.priuttelegrambot.dto.reports.ReportsCatsMapper;
import omg.group.priuttelegrambot.entity.pets.Cat;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.entity.reports.ReportCat;
import omg.group.priuttelegrambot.handlers.menu.CatsMenuHandler;
import omg.group.priuttelegrambot.handlers.menu.MainMenuHandler;
import omg.group.priuttelegrambot.handlers.pets.CatsHandler;
import omg.group.priuttelegrambot.handlers.media.impl.PhotoHandlerImpl;
import omg.group.priuttelegrambot.handlers.reports.ReportsCatsHandler;
import omg.group.priuttelegrambot.handlers.updates.OwnUpdatesHandler;
import omg.group.priuttelegrambot.repository.reports.ReportsCatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class ReportsCatsHandlerImpl implements ReportsCatsHandler {
    private final CatsHandler catsHandler;
    private final ReportsCatsRepository reportsCatsRepository;
    private final CatsMenuHandler catsMenuHandler;
    private final PhotoHandlerImpl photoHandler;
    private final MainMenuHandler mainMenuHandler;
    private final OwnUpdatesHandler ownUpdatesHandler;

    public ReportsCatsHandlerImpl(CatsHandler catsHandler,
                                  ReportsCatsRepository reportsCatsRepository,
                                  CatsMenuHandler catsMenuHandler,
                                  PhotoHandlerImpl photoHandler,
                                  MainMenuHandler mainMenuHandler,
                                  OwnUpdatesHandler ownUpdatesHandler
    ) {
        this.catsHandler = catsHandler;
        this.catsMenuHandler = catsMenuHandler;
        this.reportsCatsRepository = reportsCatsRepository;
        this.photoHandler = photoHandler;
        this.mainMenuHandler = mainMenuHandler;
        this.ownUpdatesHandler = ownUpdatesHandler;
    }

    /**
     * Method checks if report for today exist in ReportsCatsRepository
     */
    @Override
    public ReportsCatsDto isReportExist(Update update) {

        CatDto catDto = catsHandler.returnOneCatOnProbation(update);
        if (catDto != null) {
            Cat cat = CatsMapper.toEntity(catDto);
            OwnerCat owner = cat.getOwner();

            Optional<ReportCat> reportOptional = reportsCatsRepository.findByOwnerAndPetAndDateOfReport(owner, cat, LocalDate.now());

            if (reportOptional.isPresent()) {
                ReportCat report = reportOptional.get();
                return ReportsCatsMapper.toDto(report);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public ReportsCatsDto isReportCompleted(ReportsCatsDto reportDto) {

        if (reportDto != null) {
            if (reportDto.getFileId() != null &&
                    reportDto.getRation() != null &&
                    reportDto.getFeeling() != null &&
                    reportDto.getChanges() != null) {
                return null;
            } else {
                return reportDto;
            }
        } else {
            return null;
        }
    }

    @Override
    public ReportsCatsDto returnReportDtoFromUpdate(Update update) {
        ReportsCatsDto reportDto = isReportExist(update);
        if (reportDto != null) {
            return isReportCompleted(reportDto);
        }
        return null;
    }

    @Override
    public ReportsCatsDto isPhoto(ReportsCatsDto reportDto) {
        if (reportDto != null) {
            if (reportDto.getFileId() != null) {
                return null;
            } else {
                return reportDto;
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

        Long chatId = ownUpdatesHandler.getChatId(update);
        int messageId = ownUpdatesHandler.getMessageId(update);
        int date = ownUpdatesHandler.getDate(update);

        // Check for report for today exist. If exist return Report, if not exist return null
        ReportsCatsDto reportDto = isReportExist(update);

        if (reportDto != null) {
            // If Report exist check for photo is set. If photo was not set - return Report, otherwise return null
            ReportsCatsDto reportWithoutPhotoDto = isPhoto(reportDto);
            if (reportWithoutPhotoDto != null) {
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
                    if (reportsCatsRepository.findByHashCodeOfPhoto(hashOfPhoto).isPresent()) {
                        //If photo already set in reports_cats database sent appropriate message
                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                        mainMenuHandler.photoDublicateSentMessage(chatId, messageId, inlineKeyboardMarkup);
                        return false;
                    } else {
                        reportWithoutPhotoDto.setFileId(fileId);
                        reportWithoutPhotoDto.setUpdatedAt(localDateTime);
                        reportWithoutPhotoDto.setHashCodeOfPhoto(hashOfPhoto);
                        ReportCat report = ReportsCatsMapper.toEntity(reportWithoutPhotoDto);
                        reportsCatsRepository.save(report);
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
    public ReportsCatsDto isRation(ReportsCatsDto reportDto) {
        if (reportDto != null) {
            if (reportDto.getRation() != null) {
                return null;
            } else {
                return reportDto;
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

        Long chatId = ownUpdatesHandler.getChatId(update);
        int messageId = ownUpdatesHandler.getMessageId(update);
        int date = ownUpdatesHandler.getDate(update);

        // Check for report for today exist. If exist return Report, if not exist return null
        ReportsCatsDto reportDto = isReportExist(update);

        if (reportDto != null) {
            // If Report exist check for ration is set. If ration was not set - return Report, otherwise return null
            ReportsCatsDto reportWithoutRationDto = isRation(reportDto);
            if (reportWithoutRationDto != null) {
                // If ration was not set check for Update has a text
                if (update.message() != null && update.message().text() != null) {
                    // If update has a text get text from Update
                    String text = ownUpdatesHandler.getText(update);
                    LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();
                    // Check if ration from update already is in reports_cats database
                    reportWithoutRationDto.setRation(text);
                    reportWithoutRationDto.setUpdatedAt(localDateTime);
                    ReportCat report = ReportsCatsMapper.toEntity(reportWithoutRationDto);
                    reportsCatsRepository.save(report);
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
    public ReportsCatsDto isFeeling(ReportsCatsDto reportDto) {
        if (reportDto != null) {
            if (reportDto.getFeeling() != null) {
                return null;
            } else {
                return reportDto;
            }
        }
        return null;
    }

    /**
     * Method that receives self-feeling of the animal and stores it in database
     */
    @Override
    public boolean receiveFeeling(Update update) {

        Long chatId = ownUpdatesHandler.getChatId(update);
        int messageId = ownUpdatesHandler.getMessageId(update);
        int date = ownUpdatesHandler.getDate(update);

        // Check for report for today exist. If exist return Report, if not exist return null
        ReportsCatsDto reportDto = isReportExist(update);

        if (reportDto != null) {
            // If Report exist check for feeling is set. If feeling was not set - return Report, otherwise return null
            ReportsCatsDto reportWithoutFeelingDto = isFeeling(reportDto);
            if (reportWithoutFeelingDto != null) {
                // If feeling was not set check for Update has a text
                if (update.message() != null && update.message().text() != null) {
                    // If update has a text get text from Update
                    String text = ownUpdatesHandler.getText(update);
                    LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();
                    // Check if feeling from update already is in reports_cats database
                    reportWithoutFeelingDto.setFeeling(text);
                    reportWithoutFeelingDto.setUpdatedAt(localDateTime);
                    ReportCat report = ReportsCatsMapper.toEntity(reportWithoutFeelingDto);
                    reportsCatsRepository.save(report);
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
    public ReportsCatsDto isChanges(ReportsCatsDto reportDto) {
        if (reportDto != null) {
            if (reportDto.getChanges() != null) {
                return null;
            } else {
                return reportDto;
            }
        }
        return null;
    }

    /**
     * Method that receives changes that happened with the animal and stores it in database
     */
    @Override
    public boolean receiveChanges(Update update) {

        Long chatId = ownUpdatesHandler.getChatId(update);
        int messageId = ownUpdatesHandler.getMessageId(update);
        int date = ownUpdatesHandler.getDate(update);

        // Check for report for today exist. If exist return Report, if not exist return null
        ReportsCatsDto reportDto = isReportExist(update);

        if (reportDto != null) {
            // If Report exist check for changes is set. If changes was not set - return Report, otherwise return null
            ReportsCatsDto reportWithoutChangesDto = isChanges(reportDto);
            if (reportWithoutChangesDto != null) {
                // If changes was not set check for Update has a text
                if (update.message() != null && update.message().text() != null) {
                    // If update has a text get text from Update
                    String text = ownUpdatesHandler.getText(update);
                    LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();
                    // Check if photo from update already is in reports_cats database
                    reportWithoutChangesDto.setChanges(text);
                    reportWithoutChangesDto.setUpdatedAt(localDateTime);
                    ReportCat report = ReportsCatsMapper.toEntity(reportWithoutChangesDto);
                    reportsCatsRepository.save(report);
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
}



