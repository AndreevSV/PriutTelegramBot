package omg.group.priuttelegrambot.handlers.reports.impl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import omg.group.priuttelegrambot.dto.pets.DogDto;
import omg.group.priuttelegrambot.dto.pets.DogsMapper;
import omg.group.priuttelegrambot.dto.reports.ReportsDogsDto;
import omg.group.priuttelegrambot.dto.reports.ReportsDogsMapper;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.entity.pets.Dog;
import omg.group.priuttelegrambot.entity.reports.ReportDog;
import omg.group.priuttelegrambot.handlers.menu.DogsMenuHandler;
import omg.group.priuttelegrambot.handlers.menu.MainMenuHandler;
import omg.group.priuttelegrambot.handlers.pets.DogsHandler;
import omg.group.priuttelegrambot.handlers.media.impl.PhotoHandlerImpl;
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
    private final PhotoHandlerImpl photoHandler;
    private final MainMenuHandler mainMenuHandler;
    private final OwnUpdatesHandler ownUpdatesHandler;

    public ReportsDogsHandlerImpl(DogsHandler dogsHandler,
                                  ReportsDogsRepository reportsDogsRepository,
                                  DogsMenuHandler dogsMenuHandler,
                                  PhotoHandlerImpl photoHandler,
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
    public ReportsDogsDto isReportExist(Update update) {

        DogDto dogDto = dogsHandler.returnOneDogOnProbation(update);
        if (dogDto != null) {
            Dog dog = DogsMapper.toEntity(dogDto);
            OwnerDog owner = dog.getOwner();

            Optional<ReportDog> reportOptional = reportsDogsRepository.findByOwnerAndPetAndDateOfReport(owner, dog, LocalDate.now());

            if (reportOptional.isPresent()) {
                ReportDog report = reportOptional.get();
                return ReportsDogsMapper.toDto(report);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public ReportsDogsDto isReportCompleted(ReportsDogsDto reportDto) {

        if (reportDto != null) {
            if (reportDto.getFileId() != null &&
                    reportDto.getRation() != null &&
                    reportDto.getFeeling() != null &&
                    reportDto.getChanges() != null) {
                return null;
            } else {
                return reportDto;
            }
        }
        return null;
    }

    @Override
    public ReportsDogsDto returnReportFromUpdate(Update update) {
        ReportsDogsDto reportDto = isReportExist(update);
        if (reportDto != null) {
            ReportsDogsDto reportCompletedDto = isReportCompleted(reportDto);
            if (reportCompletedDto != null) {
                return reportCompletedDto;
            }
        }
        return null;
    }

    @Override
    public ReportsDogsDto isPhoto(ReportsDogsDto reportDto) {
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
        ReportsDogsDto reportDto = isReportExist(update);

        if (reportDto != null) {
            // If Report exist check for photo is set. If photo was not set - return Report, otherwise return null
            ReportsDogsDto reportWithoutPhotoDto = isPhoto(reportDto);
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
                    // Check if photo from update already is in reports_dogs database
                    if (reportsDogsRepository.findByHashCodeOfPhoto(hashOfPhoto).isPresent()) {
                        //If photo already set in reports_dogs database sent appropriate message
                        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                        mainMenuHandler.photoDublicateSentMessage(chatId, messageId, inlineKeyboardMarkup);
                        return false;
                    } else {
                        reportWithoutPhotoDto.setFileId(fileId);
                        reportWithoutPhotoDto.setUpdatedAt(localDateTime);
                        reportWithoutPhotoDto.setHashCodeOfPhoto(hashOfPhoto);
                        ReportDog report = ReportsDogsMapper.toEntity(reportWithoutPhotoDto);
                        reportsDogsRepository.save(report);
                        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                        mainMenuHandler.photoSavedOkMessage(chatId, messageId, inlineKeyboardMarkup);
                        return true;
                    }
                }
            }
            InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
            mainMenuHandler.photoAlreadySentMessage(chatId, messageId, inlineKeyboardMarkup);
            return false;
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
        mainMenuHandler.notPhotoMessage(chatId, messageId, inlineKeyboardMarkup);
        return false;
    }

    @Override
    public ReportsDogsDto isRation(ReportsDogsDto reportDto) {
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
        ReportsDogsDto reportDto = isReportExist(update);

        if (reportDto != null) {
            // If Report exist check for ration is set. If ration was not set - return Report, otherwise return null
            ReportsDogsDto reportWithoutRationDto = isRation(reportDto);
            if (reportWithoutRationDto != null) {
                // If ration was not set check for Update has a text
                if (update.message() != null && update.message().text() != null) {
                    // If update has a text get text from Update
                    String text = ownUpdatesHandler.getText(update);
                    LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();
                    // Check if ration from update already is in reports_dogs database
                    reportWithoutRationDto.setRation(text);
                    reportWithoutRationDto.setUpdatedAt(localDateTime);
                    ReportDog report = ReportsDogsMapper.toEntity(reportWithoutRationDto);
                    reportsDogsRepository.save(report);
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
    public ReportsDogsDto isFeeling(ReportsDogsDto reportDto) {
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
        ReportsDogsDto reportDto = isReportExist(update);

        if (reportDto != null) {
            // If Report exist check for feeling is set. If feeling was not set - return Report, otherwise return null
            ReportsDogsDto reportWithoutFeelingDto = isFeeling(reportDto);
            if (reportWithoutFeelingDto != null) {
                // If feeling was not set check for Update has a text
                if (update.message() != null && update.message().text() != null) {
                    // If update has a text get text from Update
                    String text = ownUpdatesHandler.getText(update);
                    LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();
                    // Check if feeling from update already is in reports_dogs database
                    reportWithoutFeelingDto.setFeeling(text);
                    reportWithoutFeelingDto.setUpdatedAt(localDateTime);
                    ReportDog report = ReportsDogsMapper.toEntity(reportWithoutFeelingDto);
                    reportsDogsRepository.save(report);
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
    public ReportsDogsDto isChanges(ReportsDogsDto reportDto) {
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
        ReportsDogsDto reportDto = isReportExist(update);

        if (reportDto != null) {
            // If Report exist check for changes is set. If changes was not set - return Report, otherwise return null
            ReportsDogsDto reportWithoutChangesDto = isChanges(reportDto);
            if (reportWithoutChangesDto != null) {
                // If changes was not set check for Update has a text
                if (update.message() != null && update.message().text() != null) {
                    // If update has a text get text from Update
                    String text = ownUpdatesHandler.getText(update);
                    LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();
                    // Check if photo from update already is in reports_dogs database
                    reportWithoutChangesDto.setChanges(text);
                    reportWithoutChangesDto.setUpdatedAt(localDateTime);
                    ReportDog report = ReportsDogsMapper.toEntity(reportWithoutChangesDto);
                    reportsDogsRepository.save(report);
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