package omg.group.priuttelegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import lombok.Data;
import omg.group.priuttelegrambot.dto.chats.ChatCatsDto;
import omg.group.priuttelegrambot.dto.chats.ChatDogsDto;
import omg.group.priuttelegrambot.dto.flags.OwnersCatsFlagsDto;
import omg.group.priuttelegrambot.dto.flags.OwnersDogsFlagsDto;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;
import omg.group.priuttelegrambot.dto.pets.CatDto;
import omg.group.priuttelegrambot.dto.pets.DogDto;
import omg.group.priuttelegrambot.dto.reports.ReportsCatsDto;
import omg.group.priuttelegrambot.dto.reports.ReportsDogsDto;
import omg.group.priuttelegrambot.handlers.contacts.ContactsHandler;
import omg.group.priuttelegrambot.handlers.contacts.OwnersCatsContactsHandler;
import omg.group.priuttelegrambot.handlers.contacts.OwnersDogsContactsHandler;
import omg.group.priuttelegrambot.handlers.flags.OwnersCatsFlagsHandler;
import omg.group.priuttelegrambot.handlers.flags.OwnersDogsFlagsHandler;
import omg.group.priuttelegrambot.handlers.menu.CatsMenuHandler;
import omg.group.priuttelegrambot.handlers.menu.DogsMenuHandler;
import omg.group.priuttelegrambot.handlers.menu.MainMenuHandler;
import omg.group.priuttelegrambot.handlers.owners.OwnersCatsHandler;
import omg.group.priuttelegrambot.handlers.owners.OwnersDogsHandler;
import omg.group.priuttelegrambot.handlers.reports.ReportsCatsHandler;
import omg.group.priuttelegrambot.handlers.reports.ReportsDogsHandler;
import omg.group.priuttelegrambot.handlers.chats.ChatsCatsHandler;
import omg.group.priuttelegrambot.handlers.chats.ChatsDogsHandler;
import omg.group.priuttelegrambot.handlers.updates.OwnUpdatesHandler;
import omg.group.priuttelegrambot.service.knowledgebases.KnowledgebaseCatsService;
import omg.group.priuttelegrambot.service.knowledgebases.KnowledgebaseDogsService;
import omg.group.priuttelegrambot.service.owners.OwnersCatsService;
import omg.group.priuttelegrambot.service.owners.OwnersDogsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class TelegramBotUpdatesListener implements UpdatesListener {

    private static final Logger LOG = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;

    private final ReportsCatsHandler reportsCatsHandler;
    private final ReportsDogsHandler reportsDogsHandler;

    private final MainMenuHandler mainMenuHandler;
    private final CatsMenuHandler catsMenuHandler;
    private final DogsMenuHandler dogsMenuHandler;

    private final OwnersCatsService ownersCatsService;
    private final OwnersDogsService ownersDogsService;

    private final ChatsCatsHandler chatsCatsHandler;
    private final ChatsDogsHandler chatsDogsHandler;

    private final KnowledgebaseCatsService knowledgebaseCatsService;
    private final KnowledgebaseDogsService knowledgebaseDogsService;

    private final OwnersCatsHandler ownersCatsHandler;
    private final OwnersDogsHandler ownersDogsHandler;

    private final OwnUpdatesHandler ownUpdatesHandler;

    private final ContactsHandler contactsHandler;
    private final OwnersCatsContactsHandler ownersCatsContactsHandler;
    private final OwnersDogsContactsHandler ownersDogsContactsHandler;

    private final OwnersCatsFlagsHandler ownersCatsFlagsHandler;
    private final OwnersDogsFlagsHandler ownersDogsFlagsHandler;

    public TelegramBotUpdatesListener(TelegramBot telegramBot,

                                      ReportsCatsHandler reportsCatsHandler,
                                      ReportsDogsHandler reportsDogsHandler,

                                      MainMenuHandler mainMenuHandler,
                                      CatsMenuHandler catsMenuHandler,
                                      DogsMenuHandler dogsMenuHandler,

                                      OwnersCatsService ownersCatsService,
                                      OwnersDogsService ownersDogsService,

                                      ChatsCatsHandler chatsCatsHandler,
                                      ChatsDogsHandler chatsDogsHandler,

                                      KnowledgebaseCatsService knowledgebaseCatsService,
                                      KnowledgebaseDogsService knowledgebaseDogsService,

                                      OwnersCatsHandler ownersCatsHandler,
                                      OwnersDogsHandler ownersDogsHandler,

                                      OwnUpdatesHandler ownUpdatesHandler,

                                      ContactsHandler contactsHandler,
                                      OwnersCatsContactsHandler ownersCatsContactsHandler,
                                      OwnersDogsContactsHandler ownersDogsContactsHandler,

                                      OwnersCatsFlagsHandler ownersCatsFlagsHandler,
                                      OwnersDogsFlagsHandler ownersDogsFlagsHandler) {
        this.telegramBot = telegramBot;
        this.telegramBot.setUpdatesListener(this);


        this.ownersCatsHandler = ownersCatsHandler;
        this.ownersDogsHandler = ownersDogsHandler;

        this.mainMenuHandler = mainMenuHandler;
        this.catsMenuHandler = catsMenuHandler;
        this.dogsMenuHandler = dogsMenuHandler;

        this.reportsCatsHandler = reportsCatsHandler;
        this.reportsDogsHandler = reportsDogsHandler;

        this.ownersCatsService = ownersCatsService;
        this.ownersDogsService = ownersDogsService;

        this.chatsCatsHandler = chatsCatsHandler;
        this.chatsDogsHandler = chatsDogsHandler;

        this.knowledgebaseCatsService = knowledgebaseCatsService;
        this.knowledgebaseDogsService = knowledgebaseDogsService;

        this.ownUpdatesHandler = ownUpdatesHandler;

        this.contactsHandler = contactsHandler;
        this.ownersCatsContactsHandler = ownersCatsContactsHandler;
        this.ownersDogsContactsHandler = ownersDogsContactsHandler;

        this.ownersCatsFlagsHandler = ownersCatsFlagsHandler;
        this.ownersDogsFlagsHandler = ownersDogsFlagsHandler;
    }

    @Override
    public int process(List<Update> updates) {
        updates.stream()
                .filter(update -> update.message() != null || update.callbackQuery() != null)
                .forEach(this::handleUpdate);
        return CONFIRMED_UPDATES_ALL;
    }

//    private void handleUpdate(Update update) {}

    private void handleUpdate(Update update) {

        LOG.info("Получен следующий апдэйт {}", update);

        Long chatId = ownUpdatesHandler.getChatId(update);
        String text = ownUpdatesHandler.getText(update);
        int messageId = ownUpdatesHandler.getMessageId(update);

        OwnerCatDto ownerCatDtoFromUpdate = ownersCatsHandler.returnOwnerCatDtoFromUpdate(update);
        OwnerCatDto volunteerCatDtoFromUpdate = ownersCatsHandler.returnVolunteerCatDtoFromUpdate(update);
        OwnerDogDto ownerDogDtoFromUpdate = ownersDogsHandler.returnOwnerDogDtoFromUpdate(update);
        OwnerDogDto volunteerDogDtoFromUpdate = ownersDogsHandler.returnVolunteerDogDtoFromUpdate(update);

        OwnersCatsFlagsDto catFlagDtoByOwner = (ownerCatDtoFromUpdate != null) ? ownersCatsFlagsHandler.findFlagByOwner(ownerCatDtoFromUpdate) : null;
        OwnersCatsFlagsDto catFlagDtoByVolunteer = (volunteerCatDtoFromUpdate != null) ? ownersCatsFlagsHandler.findFlagByVolunteer(volunteerCatDtoFromUpdate) : null;
        OwnersDogsFlagsDto dogFlagDtoByOwner = (ownerDogDtoFromUpdate != null) ? ownersDogsFlagsHandler.findFlagByOwner(ownerDogDtoFromUpdate) : null;
        OwnersDogsFlagsDto dogFlagDtoByVolunteer = (volunteerDogDtoFromUpdate != null) ? ownersDogsFlagsHandler.findFlagByVolunteer(volunteerDogDtoFromUpdate) : null;

        OwnersCatsFlagsDto catFlagDto;
        if (catFlagDtoByOwner != null) {
            catFlagDto = catFlagDtoByOwner;
        } else catFlagDto = catFlagDtoByVolunteer;

        OwnersDogsFlagsDto dogFlagDto;
        if (dogFlagDtoByOwner != null) {
            dogFlagDto = dogFlagDtoByOwner;
        } else dogFlagDto = dogFlagDtoByVolunteer;

            if (catFlagDto != null) {
//                if (update.callbackQuery() == null && update.message() != null) {
                if (catFlagDto.getIsChatting()) {
                    if ("Завершить".equals(text)) {
                        chatsCatsHandler.executeCloseButtonCommand(update);
                        ownersCatsFlagsHandler.removeFlag(catFlagDto);
                    } else if ("Ответить".equals(text)) {
                        chatsCatsHandler.executeReplyButtonCommandForVolunteer(update);
//                        OwnerCatDto ownerCatDto = catFlagDto.getOwnerDto();
//                        OwnerCatDto volunteerCatDto = catFlagDto.getVolunteerDto();
//                        ownersCatsFlagsHandler.createChattingFlag(ownerCatDto, volunteerCatDto);
                    } else if (text != null && text.startsWith("/")) {
                        processButton(update);
                    } else {
                        chatsCatsHandler.forwardMessageReceived(update);
                    }
                }
                if (catFlagDto.getIsWaitingForPhoto()) {
                    boolean photo = reportsCatsHandler.receivePhoto(update);
                    if (photo) {
                        ownersCatsFlagsHandler.removeFlag(catFlagDto);
                    }
                } else if (catFlagDto.getIsWaitingForRation()) {
                    boolean ration = reportsCatsHandler.receiveRation(update);
                    if (ration) {
                        ownersCatsFlagsHandler.removeFlag(catFlagDto);
                    }
                } else if (catFlagDto.getIsWaitingForFeeling()) {
                    boolean feeling = reportsCatsHandler.receiveFeeling(update);
                    if (feeling) {
                        ownersCatsFlagsHandler.removeFlag(catFlagDto);
                    }
                } else if (catFlagDto.getIsWaitingForChanges()) {
                    boolean changes = reportsCatsHandler.receiveChanges(update);
                    if (changes) {
                        ownersCatsFlagsHandler.removeFlag(catFlagDto);
                    }
                } else if (catFlagDto.getIsWaitingForContacts()) {
                    Contact contact = update.message().contact();
                    if (contact != null) {
                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForInfoMenuButton();
                        ownersCatsContactsHandler.savePhoneNumberFromContact(update);
                        ownersCatsFlagsHandler.removeFlag(catFlagDto);
                        mainMenuHandler.contactSavedOkMessage(chatId, inlineKeyboardMarkup);
                    } else {
                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForInfoMenuButton();
                        mainMenuHandler.waitingForContactMessage(chatId, inlineKeyboardMarkup);
                    }
                }
//                } else if (update.callbackQuery() != null) {
//                    if (waitingForRation) {
//                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForInfoMenuButton();
//                        mainMenuHandler.executeSendRationButtonMessage(chatId, messageId, inlineKeyboardMarkup);
//                    } else if (waitingForFeeling) {
//                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForInfoMenuButton();
//                        mainMenuHandler.executeSendFeelingButtonMessage(chatId, messageId, inlineKeyboardMarkup);
//                    } else if (waitingForChanges) {
//                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForInfoMenuButton();
//                        mainMenuHandler.executeSendChangesButtonMessage(chatId, messageId, inlineKeyboardMarkup);
//                    } else if (waitingForPhoto) {
//                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForInfoMenuButton();
//                        mainMenuHandler.executeSendPhotoButtonMessage(chatId, messageId, inlineKeyboardMarkup);
//                    } else if (waitingForContacts) {
//                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForInfoMenuButton();
//                        mainMenuHandler.executeSendContactsButtonMessage(chatId, messageId, inlineKeyboardMarkup);
//                    } else if (chatting) {
//                        if (message.equals("/Close")) {
//                            chatsCatsHandler.executeCloseButtonCommand(update);
//                            ownersCatsFlagsHandler.removeFlag(catFlagDto);
//                        } else if (message.equals("/Reply")) {
//                            chatsCatsHandler.executeReplyButtonCommandForVolunteer(update);
//                            ownersCatsFlagsHandler.createChattingFlag(ownerCatDto, volunteerCat);
//                        } else {
//                            InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formPriutMainMenuButton();
//                            mainMenuHandler.executeCallVolunteerButtonMessage(chatId, messageId, inlineKeyboardMarkup);
//                        }
//                    }
            } else if (dogFlagDto != null) {
//                    if (update.callbackQuery() == null && update.message() != null) {
                if (dogFlagDto.getIsChatting()) {
                    if ("Завершить".equals(text)) {
                        chatsDogsHandler.executeCloseButtonCommand(update);
                        ownersDogsFlagsHandler.removeFlag(dogFlagDto);
                    } else if ("Ответить".equals(text)) {
                        chatsDogsHandler.executeReplyButtonCommandForVolunteer(update);
                        OwnerDogDto ownerDogDto = dogFlagDto.getOwnerDto();
                        OwnerDogDto volunteerDogDto = dogFlagDto.getVolunteerDto();
                        ownersDogsFlagsHandler.createChattingFlag(ownerDogDto, volunteerDogDto);
                    } else if (!text.startsWith("/")) {
                        chatsDogsHandler.forwardMessageReceived(update);
                    }
                }
                if (dogFlagDto.getIsWaitingForPhoto()) {
                    boolean photo = reportsDogsHandler.receivePhoto(update);
                    if (photo) {
                        ownersDogsFlagsHandler.removeFlag(dogFlagDto);
                    }
                } else if (dogFlagDto.getIsWaitingForRation()) {
                    boolean ration = reportsDogsHandler.receiveRation(update);
                    if (ration) {
                        ownersDogsFlagsHandler.removeFlag(dogFlagDto);
                    }
                } else if (dogFlagDto.getIsWaitingForFeeling()) {
                    boolean feeling = reportsDogsHandler.receiveFeeling(update);
                    if (feeling) {
                        ownersDogsFlagsHandler.removeFlag(dogFlagDto);
                    }
                } else if (dogFlagDto.getIsWaitingForChanges()) {
                    boolean changes = reportsDogsHandler.receiveChanges(update);
                    if (changes) {
                        ownersDogsFlagsHandler.removeFlag(dogFlagDto);
                    }
                } else if (dogFlagDto.getIsWaitingForContacts()) {
                    Contact contact = update.message().contact();
                    if (contact != null) {
                        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForInfoMenuButton();
                        mainMenuHandler.contactSavedOkMessage(chatId, inlineKeyboardMarkup);
                        ownersDogsFlagsHandler.removeFlag(dogFlagDto);
                        ownersDogsContactsHandler.savePhoneNumberFromContact(update);
                    } else {
                        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForInfoMenuButton();
                        mainMenuHandler.waitingForContactMessage(chatId, inlineKeyboardMarkup);
                    }
                }
//                    } else if (update.callbackQuery() != null) {
//                        if (waitingForRation) {
//                            InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForInfoMenuButton();
//                            mainMenuHandler.executeSendRationButtonMessage(chatId, messageId, inlineKeyboardMarkup);
//                        } else if (waitingForFeeling) {
//                            InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForInfoMenuButton();
//                            mainMenuHandler.executeSendFeelingButtonMessage(chatId, messageId, inlineKeyboardMarkup);
//                        } else if (waitingForChanges) {
//                            InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForInfoMenuButton();
//                            mainMenuHandler.executeSendChangesButtonMessage(chatId, messageId, inlineKeyboardMarkup);
//                        } else if (waitingForPhoto) {
//                            InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForInfoMenuButton();
//                            mainMenuHandler.executeSendPhotoButtonMessage(chatId, messageId, inlineKeyboardMarkup);
//                        } else if (waitingForContacts) {
//                            InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForInfoMenuButton();
//                            mainMenuHandler.executeSendContactsButtonMessage(chatId, messageId, inlineKeyboardMarkup);
//                        } else if (isChatting) {
//                            InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForInfoMenuButton();
//                            mainMenuHandler.executeCallVolunteerButtonMessage(chatId, messageId, inlineKeyboardMarkup);
//                        }
//                    }
//                }
//            }
//        }
            } else {
                if (update.callbackQuery() != null || (update.message().text() != null && update.message().text().startsWith("/"))) {
                    processButton(update);
                } else {
                    mainMenuHandler.noSuchCommandSendMessage(update);
                }
            }
        }



    private void processButton(Update update) {

        Long chatId = ownUpdatesHandler.getChatId(update);
        String command = ownUpdatesHandler.getText(update);
        int messageId = ownUpdatesHandler.getMessageId(update);

        switch (command) {
            case "/start" -> {
                mainMenuHandler.executeStartMenuButton(update);
            }
            case "/cat" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formPriutMainMenuButton();
                catsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                ownersCatsHandler.newOwnerRegister(update);
            }
            case "/cat_info", "/cat_about", "/cat_timetable", "/cat_admission", "/cat_safety_measures" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForInfoMenuButton();
                catsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/cat_take", "/cat_connection_rules", "/cat_documents", "/cat_transportation", "/cat_kitty_at_home",
                    "/cat_at_home", "/cat_disability", "/cat_refusal_reasons" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForTakeMenuButton();
                catsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/cat_send_report" -> {
                // Check for report for today exist
                ReportsCatsDto reportDto = reportsCatsHandler.isReportExist(update);
                if (reportDto != null) {
                    // Check if existed report completed
                    ReportsCatsDto reportCompleted = reportsCatsHandler.isReportCompleted(reportDto);
                    if (reportCompleted == null) {
                        // If report is completed send message that report already was sent
                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formPriutMainMenuButton();
                        mainMenuHandler.reportAlreadySentMessage(chatId, messageId, inlineKeyboardMarkup);
                    } else {
                        // If report is not completed show Send Report Menu buttons
                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                        catsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                    }
                    // If report is not exist, send No report exist message and show Main menu buttons
                } else {
                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formPriutMainMenuButton();
                    mainMenuHandler.noReportExistMessage(chatId, messageId, inlineKeyboardMarkup);
                }
            }
            case "/cat_send_photo" -> {
                ReportsCatsDto reportDto = reportsCatsHandler.returnReportDtoFromUpdate(update);
                if (reportDto != null) {
                    ReportsCatsDto photoReportDto = reportsCatsHandler.isPhoto(reportDto);
                    if (photoReportDto != null) {
                        // If Report is exist get an OwnerCat and Cat from Report
                        OwnerCatDto ownerDto = reportDto.getOwnerDto();
                        CatDto catDto = reportDto.getPetDto();
                        // Create waiting for photo flag
                        ownersCatsFlagsHandler.createWaitingForPhotoFlag(ownerDto, catDto);
                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                        catsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                    } else {
                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                        mainMenuHandler.photoAlreadySentMessage(chatId, messageId, inlineKeyboardMarkup);
                    }
                }
            }
            case "/cat_send_ration" -> {
                ReportsCatsDto reportDto = reportsCatsHandler.returnReportDtoFromUpdate(update);
                if (reportDto != null) {
                    ReportsCatsDto rationReportDto = reportsCatsHandler.isRation(reportDto);
                    if (rationReportDto != null) {
                        // If Report is exist get an OwnerCat and Cat from Report
                        OwnerCatDto ownerDto = reportDto.getOwnerDto();
                        CatDto catDto = reportDto.getPetDto();
                        // Create waiting for ration flag
                        ownersCatsFlagsHandler.createWaitingForRationFlag(ownerDto, catDto);
                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                        catsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                    } else {
                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                        mainMenuHandler.rationAlreadySentMessage(chatId, messageId, inlineKeyboardMarkup);
                    }
                }
            }
            case "/cat_send_feeling" -> {
                ReportsCatsDto reportDto = reportsCatsHandler.returnReportDtoFromUpdate(update);
                if (reportDto != null) {
                    ReportsCatsDto feelingReportDto = reportsCatsHandler.isFeeling(reportDto);
                    if (feelingReportDto != null) {
                        // If Report is exist get an OwnerCat and Cat from Report
                        OwnerCatDto ownerDto = reportDto.getOwnerDto();
                        CatDto catDto = reportDto.getPetDto();
                        // Create waiting for ration flag
                        ownersCatsFlagsHandler.createWaitingForFeelingFlag(ownerDto, catDto);
                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                        catsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                    } else {
                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                        mainMenuHandler.feelingAlreadySentMessage(chatId, messageId, inlineKeyboardMarkup);
                    }
                }
            }
            case "/cat_send_changes" -> {
                ReportsCatsDto reportDto = reportsCatsHandler.returnReportDtoFromUpdate(update);
                if (reportDto != null) {
                    ReportsCatsDto changesReportDto = reportsCatsHandler.isChanges(reportDto);
                    if (changesReportDto != null) {
                        // If Report is exist get an OwnerCat and Cat from Report
                        OwnerCatDto ownerDto = reportDto.getOwnerDto();
                        CatDto catDto = reportDto.getPetDto();
                        // Create waiting for ration flag
                        ownersCatsFlagsHandler.createWaitingForChangesFlag(ownerDto, catDto);
                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                        catsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                    } else {
                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                        mainMenuHandler.changesAlreadySentMessage(chatId, messageId, inlineKeyboardMarkup);
                    }
                }
            }
            case "/cat_back" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formPriutMainMenuButton();
                catsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/cat_volunteer" -> {
                ChatCatsDto chatDto = chatsCatsHandler.findByOwnerCatChatId(chatId);
                if (chatDto != null) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formPriutMainMenuButton();
                    mainMenuHandler.chatAlreadySetMessage(chatId, messageId, inlineKeyboardMarkup);
                } else {
                    OwnerCatDto volunteerCatDto = chatsCatsHandler.callVolunteer(update);
                    if (volunteerCatDto != null) {
                        OwnerCatDto ownerCatDto = ownersCatsHandler.returnOwnerCatDtoFromUpdate(update);
                        ownersCatsFlagsHandler.createChattingFlag(ownerCatDto, volunteerCatDto);
                    } else {
                        mainMenuHandler.noFreeVolunteerAvailableMessage(chatId);
                        catsMenuHandler.formPriutMainMenuButton();
                    }
                }
            }
            case "/cat_receive_contacts" -> {
                // Check if contacts already saved in OwnerCat
                if (ownersCatsContactsHandler.isTelephone(update)) {
                    // If phone number already saved in OwnerCat - send an appropriate message and show Form Main Menu buttons
                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formPriutMainMenuButton();
                    mainMenuHandler.telephoneAlreadySetMessage(chatId, messageId, inlineKeyboardMarkup);
                } else {
                    // If phone number absent in OwnerCat - return Report
                    OwnerCatDto ownerCatDto = ownersCatsHandler.returnOwnerCatDtoFromUpdate(update);
                    // Create waiting for contacts flag and form inquery for contact
                    contactsHandler.askForContact(update);
                    ownersCatsFlagsHandler.createWaitingForContactsFlag(ownerCatDto);
                }
            }
            case "/dog" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formPriutMainMenuButton();
                dogsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                ownersDogsHandler.newOwnerRegister(update);
            }
            case "/dog_info", "/dog_about", "/dog_timetable", "/dog_admission", "/dog_safety_measures" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForInfoMenuButton();
                dogsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/dog_take", "/dog_connection_rules", "/dog_documents", "/dog_transportation", "/dog_puppy_at_home",
                    "/dog_at_home", "/dog_disability", "/dog_recommendations", "/dog_cynologist", "/dog_refusal_reasons" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForTakeMenuButton();
                dogsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/dog_send_report" -> {
                // Check for report for today exist
                ReportsDogsDto reportDto = reportsDogsHandler.isReportExist(update);
                if (reportDto != null) {
                    // Check if existed report completed
                    ReportsDogsDto reportCompleted = reportsDogsHandler.isReportCompleted(reportDto);
                    if (reportCompleted == null) {
                        // If report is completed send message that report already was sent
                        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formPriutMainMenuButton();
                        mainMenuHandler.reportAlreadySentMessage(chatId, messageId, inlineKeyboardMarkup);
                    } else {
                        // If report is not completed show Send Report Menu buttons
                        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                        dogsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                    }
                    // If report is not exist, send No report exist message and show Main menu buttons
                } else {
                    InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formPriutMainMenuButton();
                    mainMenuHandler.noReportExistMessage(chatId, messageId, inlineKeyboardMarkup);
                }
            }
            case "/dog_send_photo" -> {
                ReportsDogsDto reportDto = reportsDogsHandler.returnReportFromUpdate(update);
                if (reportDto != null) {
                    ReportsDogsDto photoReportDto = reportsDogsHandler.isPhoto(reportDto);
                    if (photoReportDto != null) {
                        // If Report is exist get an OwnerCat and Cat from Report
                        OwnerDogDto ownerDto = reportDto.getOwnerDto();
                        DogDto dogDto = reportDto.getPetDto();
                        // Create waiting for photo flag
                        ownersDogsFlagsHandler.createWaitingForPhotoFlag(ownerDto, dogDto);
                        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                        dogsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                    } else {
                        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                        mainMenuHandler.photoAlreadySentMessage(chatId, messageId, inlineKeyboardMarkup);
                    }
                }
            }
            case "/dog_send_ration" -> {
                ReportsDogsDto reportsDto = reportsDogsHandler.returnReportFromUpdate(update);
                if (reportsDto != null) {
                    ReportsDogsDto rationReportDto = reportsDogsHandler.isRation(reportsDto);
                    if (rationReportDto != null) {
                        // If Report is exist get an OwnerCat and Cat from Report
                        OwnerDogDto ownerDto = reportsDto.getOwnerDto();
                        DogDto dogDto = reportsDto.getPetDto();
                        // Create waiting for ration flag
                        ownersDogsFlagsHandler.createWaitingForRationFlag(ownerDto, dogDto);
                        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                        dogsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                    } else {
                        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                        mainMenuHandler.rationAlreadySentMessage(chatId, messageId, inlineKeyboardMarkup);
                    }
                }
            }
            case "/dog_send_feeling" -> {
                ReportsDogsDto reportDto = reportsDogsHandler.returnReportFromUpdate(update);
                if (reportDto != null) {
                    ReportsDogsDto feelingReportDto = reportsDogsHandler.isFeeling(reportDto);
                    if (feelingReportDto != null) {
                        // If Report is exist get an OwnerCat and Cat from Report
                        OwnerDogDto ownerDto = reportDto.getOwnerDto();
                        DogDto dogDto = reportDto.getPetDto();
                        // Create waiting for ration flag
                        ownersDogsFlagsHandler.createWaitingForFeelingFlag(ownerDto, dogDto);
                        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                        dogsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                    } else {
                        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                        mainMenuHandler.feelingAlreadySentMessage(chatId, messageId, inlineKeyboardMarkup);
                    }
                }
            }
            case "/dog_send_changes" -> {
                ReportsDogsDto reportDto = reportsDogsHandler.returnReportFromUpdate(update);
                if (reportDto != null) {
                    ReportsDogsDto changesReportDto = reportsDogsHandler.isChanges(reportDto);
                    if (changesReportDto != null) {
                        // If Report is exist get an OwnerCat and Cat from Report
                        OwnerDogDto ownerDto = reportDto.getOwnerDto();
                        DogDto dogDto = reportDto.getPetDto();
                        // Create waiting for ration flag
                        ownersDogsFlagsHandler.createWaitingForChangesFlag(ownerDto, dogDto);
                        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                        dogsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                    } else {
                        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                        mainMenuHandler.changesAlreadySentMessage(chatId, messageId, inlineKeyboardMarkup);
                    }
                }
            }
            case "/dog_back" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formPriutMainMenuButton();
                dogsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/dog_volunteer" -> {
                ChatDogsDto chatDto = chatsDogsHandler.findByOwnerDogChatId(chatId);
                if (chatDto != null) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formPriutMainMenuButton();
                    mainMenuHandler.chatAlreadySetMessage(chatId, messageId, inlineKeyboardMarkup);
                } else {
                    OwnerDogDto volunteerDogDto = chatsDogsHandler.callVolunteer(update);
                    if (volunteerDogDto != null) {
                        OwnerDogDto ownerDogDto = ownersDogsHandler.returnOwnerDogDtoFromUpdate(update);
                        ownersDogsFlagsHandler.createChattingFlag(ownerDogDto, volunteerDogDto);
                    } else {
                        mainMenuHandler.noFreeVolunteerAvailableMessage(chatId);
                        dogsMenuHandler.formPriutMainMenuButton();
                    }
                }
            }
            case "/dog_receive_contacts" -> {
                // Check if contacts already saved in OwnerCat
                if (ownersDogsContactsHandler.isTelephone(update)) {
                    // If phone number already saved in OwnerCat - send an appropriate message and show Form Main Menu buttons
                    InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formPriutMainMenuButton();
                    mainMenuHandler.telephoneAlreadySetMessage(chatId, messageId, inlineKeyboardMarkup);
                } else {
                    // If phone number absent in OwnerCat - return Report
                    OwnerDogDto ownerDogDto = ownersDogsHandler.returnOwnerDogDtoFromUpdate(update);
                    // Create waiting for contacts flag and form inquery for contact
                    contactsHandler.askForContact(update);
                    ownersDogsFlagsHandler.createWaitingForContactsFlag(ownerDogDto);
                }
            }
            default -> mainMenuHandler.noSuchCommandSendMessage(update);
        }
    }
}


