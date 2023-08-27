package omg.group.priuttelegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import lombok.Data;
import omg.group.priuttelegrambot.dto.chats.ChatCatsDto;
import omg.group.priuttelegrambot.entity.flags.OwnersCatsFlags;
import omg.group.priuttelegrambot.entity.flags.OwnersDogsFlags;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.entity.pets.Cat;
import omg.group.priuttelegrambot.entity.pets.Dog;
import omg.group.priuttelegrambot.entity.reports.ReportCat;
import omg.group.priuttelegrambot.entity.reports.ReportDog;
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

        Long chatId = ownUpdatesHandler.extractChatIdFromUpdate(update);
        String message = ownUpdatesHandler.extractTextFromUpdate(update);
        int messageId = ownUpdatesHandler.extractMessageIdFromUpdate(update);

        OwnerCat ownerCat = ownersCatsHandler.returnOwnerCatDtoFromUpdate(update);
        OwnerDog ownerDog = ownersDogsHandler.returnOwnerDogDtoFromUpdate(update);

        if (ownerCat != null) {
            OwnersCatsFlags catFlag = ownersCatsFlagsHandler.findOwnersCatsFlagsByOwner(ownerCat);
            if (catFlag != null) {
                OwnerCat volunteerCat = catFlag.getVolunteer();
                boolean waitingForPhoto = catFlag.isWaitingForPhoto();
                boolean waitingForRation = catFlag.isWaitingForRation();
                boolean waitingForFeeling = catFlag.isWaitingForFeeling();
                boolean waitingForChanges = catFlag.isWaitingForChanges();
                boolean waitingForContacts = catFlag.isWaitingForContacts();
                boolean chatting = catFlag.isChatting();

                if (update.callbackQuery() == null && update.message() != null) {
                    if (waitingForPhoto) {
                        boolean photo = reportsCatsHandler.receivePhoto(update);
                        if (photo) {
                            ownersCatsFlagsHandler.removeFlag(catFlag);
                        }
                    } else if (waitingForRation) {
                        boolean ration = reportsCatsHandler.receiveRation(update);
                        if (ration) {
                            ownersCatsFlagsHandler.removeFlag(catFlag);
                        }
                    } else if (waitingForChanges) {
                        boolean changes = reportsCatsHandler.receiveChanges(update);
                        if (changes) {
                            ownersCatsFlagsHandler.removeFlag(catFlag);
                        }
                    } else if (waitingForFeeling) {
                        boolean feeling = reportsCatsHandler.receiveFeeling(update);
                        if (feeling) {
                            ownersCatsFlagsHandler.removeFlag(catFlag);
                        }
                    } else if (waitingForContacts) {
                        Contact contact = update.message().contact();
                        if (contact != null) {
                            InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForInfoMenuButton();
                            mainMenuHandler.contactSavedOkMessage(chatId, inlineKeyboardMarkup);
                            ownersCatsContactsHandler.savePhoneNumberFromContact(update);
                            ownersCatsFlagsHandler.removeFlag(catFlag);
                        } else {
                            InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForInfoMenuButton();
                            mainMenuHandler.waitingForContactMessage(chatId, inlineKeyboardMarkup);
                        }
                    } else if (chatting) {
                        if (message.equals("/Завершить")) {
                            chatsCatsHandler.executeCloseButtonCommand(update);
                            ownersCatsFlagsHandler.removeFlag(catFlag);
                        } else if (message.equals("/Ответить")) {
                            chatsCatsHandler.executeReplyButtonCommandForVolunteer(update);
                            ownersCatsFlagsHandler.createChattingFlag(ownerCat, volunteerCat);
                        } else if (!message.startsWith("/")) {
                            chatsCatsHandler.forwardMessageReceived(update);
                        }
                    }
                } else if (update.callbackQuery() != null) {
                    if (waitingForRation) {
                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForInfoMenuButton();
                        mainMenuHandler.executeSendRationButtonMessage(chatId, messageId, inlineKeyboardMarkup);
                    } else if (waitingForFeeling) {
                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForInfoMenuButton();
                        mainMenuHandler.executeSendFeelingButtonMessage(chatId, messageId, inlineKeyboardMarkup);
                    } else if (waitingForChanges) {
                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForInfoMenuButton();
                        mainMenuHandler.executeSendChangesButtonMessage(chatId, messageId, inlineKeyboardMarkup);
                    } else if (waitingForPhoto) {
                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForInfoMenuButton();
                        mainMenuHandler.executeSendPhotoButtonMessage(chatId, messageId, inlineKeyboardMarkup);
                    } else if (waitingForContacts) {
                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForInfoMenuButton();
                        mainMenuHandler.executeSendContactsButtonMessage(chatId, messageId, inlineKeyboardMarkup);
                    } else if (chatting) {
                        if (message.equals("/Завершить")) {
                            chatsCatsHandler.executeCloseButtonCommand(update);
                            ownersCatsFlagsHandler.removeFlag(catFlag);
                        } else if (message.equals("/Ответить")) {
                            chatsCatsHandler.executeReplyButtonCommandForVolunteer(update);
                            ownersCatsFlagsHandler.createChattingFlag(ownerCat, volunteerCat);
                        } else {
                            InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formPriutMainMenuButton();
                            mainMenuHandler.executeCallVolunteerButtonMessage(chatId, messageId, inlineKeyboardMarkup);
                        }
                    }
                }
            } else if (ownerDog != null) {
                OwnersDogsFlags dogFlag = ownersDogsFlagsHandler.findOwnersDogsFlagsByOwner(ownerDog);
                if (dogFlag != null) {
                    OwnerDog volunteerDog = dogFlag.getVolunteer();
                    boolean waitingForPhoto = dogFlag.isWaitingForPhoto();
                    boolean waitingForRation = dogFlag.isWaitingForRation();
                    boolean waitingForFeeling = dogFlag.isWaitingForFeeling();
                    boolean waitingForChanges = dogFlag.isWaitingForChanges();
                    boolean waitingForContacts = dogFlag.isWaitingForContacts();
                    boolean isChatting = dogFlag.isChatting();

                    if (update.callbackQuery() == null && update.message() != null) {
                        if (waitingForPhoto) {
                            boolean photo = reportsDogsHandler.receivePhoto(update);
                            if (photo) {
                                ownersDogsFlagsHandler.removeFlag(dogFlag);
                            }
                        } else if (waitingForRation) {
                            boolean ration = reportsDogsHandler.receiveRation(update);
                            if (ration) {
                                ownersDogsFlagsHandler.removeFlag(dogFlag);
                            }
                        } else if (waitingForFeeling) {
                            boolean feeling = reportsDogsHandler.receiveFeeling(update);
                            if (feeling) {
                                ownersDogsFlagsHandler.removeFlag(dogFlag);
                            }
                        } else if (waitingForChanges) {
                            boolean changes = reportsDogsHandler.receiveChanges(update);
                            if (changes) {
                                ownersDogsFlagsHandler.removeFlag(dogFlag);
                            }
                        } else if (waitingForContacts) {
                            Contact contact = update.message().contact();
                            if (contact != null) {
                                InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForInfoMenuButton();
                                mainMenuHandler.contactSavedOkMessage(chatId, inlineKeyboardMarkup);
                                ownersDogsContactsHandler.savePhoneNumberFromContact(update);
                                ownersDogsFlagsHandler.removeFlag(dogFlag);
                            } else {
                                InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForInfoMenuButton();
                                mainMenuHandler.waitingForContactMessage(chatId, inlineKeyboardMarkup);
                            }
                        } else if (isChatting) {
                            if (message.equals("/Завершить")) {
                                chatsDogsHandler.executeCloseButtonCommand(update);
                                ownersDogsFlagsHandler.removeFlag(dogFlag);
                            } else if (message.equals("/Ответить")) {
                                chatsDogsHandler.executeReplyButtonCommandForVolunteer(update);
                                ownersDogsFlagsHandler.createChattingFlag(ownerDog, volunteerDog);
                            } else {
                                InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formPriutMainMenuButton();
                                mainMenuHandler.executeCallVolunteerButtonMessage(chatId, messageId, inlineKeyboardMarkup);
                            }
                        }
                    } else if (update.callbackQuery() != null) {
                        if (waitingForRation) {
                            InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForInfoMenuButton();
                            mainMenuHandler.executeSendRationButtonMessage(chatId, messageId, inlineKeyboardMarkup);
                        } else if (waitingForFeeling) {
                            InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForInfoMenuButton();
                            mainMenuHandler.executeSendFeelingButtonMessage(chatId, messageId, inlineKeyboardMarkup);
                        } else if (waitingForChanges) {
                            InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForInfoMenuButton();
                            mainMenuHandler.executeSendChangesButtonMessage(chatId, messageId, inlineKeyboardMarkup);
                        } else if (waitingForPhoto) {
                            InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForInfoMenuButton();
                            mainMenuHandler.executeSendPhotoButtonMessage(chatId, messageId, inlineKeyboardMarkup);
                        } else if (waitingForContacts) {
                            InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForInfoMenuButton();
                            mainMenuHandler.executeSendContactsButtonMessage(chatId, messageId, inlineKeyboardMarkup);
                        } else if (isChatting) {
                            InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForInfoMenuButton();
                            mainMenuHandler.executeCallVolunteerButtonMessage(chatId, messageId, inlineKeyboardMarkup);
                        }
                    }
                }
            } else if (update.callbackQuery() != null || (update.message().text() != null && update.message().text().startsWith("/"))) {
                processButton(update);
            } else {
                mainMenuHandler.noSuchCommandSendMessage(update);
            }
        }
    }

    private void processButton(Update update) {

        Long chatId = ownUpdatesHandler.extractChatIdFromUpdate(update);
        String command = ownUpdatesHandler.extractTextFromUpdate(update);
        int messageId = ownUpdatesHandler.extractMessageIdFromUpdate(update);

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
                ReportCat report = reportsCatsHandler.isReportExist(update);
                if (report != null) {
                    // Check if existed report completed
                    ReportCat reportCompleted = reportsCatsHandler.isReportCompleted(report);
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
                ReportCat report = reportsCatsHandler.returnReportFromUpdate(update);
                if (report != null) {
                    ReportCat photoReport = reportsCatsHandler.isPhoto(report);
                    if (photoReport != null) {
                        // If Report is exist get an OwnerCat and Cat from Report
                        OwnerCat owner = report.getOwner();
                        Cat cat = report.getCat();
                        // Create waiting for photo flag
                        ownersCatsFlagsHandler.createWaitingForPhotoFlag(owner, cat);
                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                        catsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                    } else {
                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                        mainMenuHandler.photoAlreadySentMessage(chatId, messageId, inlineKeyboardMarkup);
                    }
                }
            }
            case "/cat_send_ration" -> {
                ReportCat report = reportsCatsHandler.returnReportFromUpdate(update);
                if (report != null) {
                    ReportCat rationReport = reportsCatsHandler.isRation(report);
                    if (rationReport != null) {
                        // If Report is exist get an OwnerCat and Cat from Report
                        OwnerCat owner = report.getOwner();
                        Cat cat = report.getCat();
                        // Create waiting for ration flag
                        ownersCatsFlagsHandler.createWaitingForRationFlag(owner, cat);
                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                        catsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                    } else {
                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                        mainMenuHandler.rationAlreadySentMessage(chatId, messageId, inlineKeyboardMarkup);
                    }
                }
            }
            case "/cat_send_feeling" -> {
                ReportCat report = reportsCatsHandler.returnReportFromUpdate(update);
                if (report != null) {
                    ReportCat feelingReport = reportsCatsHandler.isFeeling(report);
                    if (feelingReport != null) {
                        // If Report is exist get an OwnerCat and Cat from Report
                        OwnerCat owner = report.getOwner();
                        Cat cat = report.getCat();
                        // Create waiting for ration flag
                        ownersCatsFlagsHandler.createWaitingForFeelingFlag(owner, cat);
                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                        catsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                    } else {
                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                        mainMenuHandler.feelingAlreadySentMessage(chatId, messageId, inlineKeyboardMarkup);
                    }
                }
            }
            case "/cat_send_changes" -> {
                ReportCat report = reportsCatsHandler.returnReportFromUpdate(update);
                if (report != null) {
                    ReportCat changesReport = reportsCatsHandler.isChanges(report);
                    if (changesReport != null) {
                        // If Report is exist get an OwnerCat and Cat from Report
                        OwnerCat owner = report.getOwner();
                        Cat cat = report.getCat();
                        // Create waiting for ration flag
                        ownersCatsFlagsHandler.createWaitingForChangesFlag(owner, cat);
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
                ChatCatsDto chat = chatsCatsHandler.findByOwnerCatChatId(chatId);
                if (chat != null) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formPriutMainMenuButton();
                    mainMenuHandler.chatAlreadySetMessage(chatId, messageId, inlineKeyboardMarkup);
                } else {
                    chatsCatsHandler.callVolunteer(update);
                    ownersCatsFlagsHandler.createChattingFlag();
                    OwnersCatsFlags flag = new OwnersCatsFlags();
                    flag.setChatting(true);
                    ownersCatsFlagStatus.put(chatId, flag);
                    chatsCatsHandler.callVolunteer(update);
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
                    OwnerCat ownerCat = ownersCatsHandler.returnOwnerCatDtoFromUpdate(update);
                    // Create waiting for contacts flag and form inquery for contact
                    ownersCatsFlagsHandler.createWaitingForContactsFlag(ownerCat);
                    contactsHandler.askForContact(update);
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
                ReportDog report = reportsDogsHandler.isReportExist(update);
                if (report != null) {
                    // Check if existed report completed
                    ReportDog reportCompleted = reportsDogsHandler.isReportCompleted(report);
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
                ReportDog report = reportsDogsHandler.returnReportFromUpdate(update);
                if (report != null) {
                    ReportDog photoReport = reportsDogsHandler.isPhoto(report);
                    if (photoReport != null) {
                        // If Report is exist get an OwnerCat and Cat from Report
                        OwnerDog owner = report.getOwnerDog();
                        Dog dog = report.getDog();
                        // Create waiting for photo flag
                        ownersDogsFlagsHandler.createWaitingForPhotoFlag(owner, dog);
                        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                        dogsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                    } else {
                        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                        mainMenuHandler.photoAlreadySentMessage(chatId, messageId, inlineKeyboardMarkup);
                    }
                }
            }
            case "/dog_send_ration" -> {
                ReportDog report = reportsDogsHandler.returnReportFromUpdate(update);
                if (report != null) {
                    ReportDog rationReport = reportsDogsHandler.isRation(report);
                    if (rationReport != null) {
                        // If Report is exist get an OwnerCat and Cat from Report
                        OwnerDog owner = report.getOwnerDog();
                        Dog dog = report.getDog();
                        // Create waiting for ration flag
                        ownersDogsFlagsHandler.createWaitingForRationFlag(owner, dog);
                        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                        dogsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                    } else {
                        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                        mainMenuHandler.rationAlreadySentMessage(chatId, messageId, inlineKeyboardMarkup);
                    }
                }
            }
            case "/dog_send_feeling" -> {
                ReportDog report = reportsDogsHandler.returnReportFromUpdate(update);
                if (report != null) {
                    ReportDog feelingReport = reportsDogsHandler.isFeeling(report);
                    if (feelingReport != null) {
                        // If Report is exist get an OwnerCat and Cat from Report
                        OwnerDog owner = report.getOwnerDog();
                        Dog dog = report.getDog();
                        // Create waiting for ration flag
                        ownersDogsFlagsHandler.createWaitingForFeelingFlag(owner, dog);
                        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                        dogsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                    } else {
                        InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                        mainMenuHandler.feelingAlreadySentMessage(chatId, messageId, inlineKeyboardMarkup);
                    }
                }
            }
            case "/dog_send_changes" -> {
                ReportDog report = reportsDogsHandler.returnReportFromUpdate(update);
                if (report != null) {
                    ReportDog changesReport = reportsDogsHandler.isChanges(report);
                    if (changesReport != null) {
                        // If Report is exist get an OwnerCat and Cat from Report
                        OwnerDog owner = report.getOwnerDog();
                        Dog dog = report.getDog();
                        // Create waiting for ration flag
                        ownersDogsFlagsHandler.createWaitingForChangesFlag(owner, dog);
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
                if (ownersDogsFlagStatus.containsKey(chatId) && ownersDogsFlagStatus.get(chatId).isChatting()) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formPriutMainMenuButton();
                    mainMenuHandler.chatAlreadySetMessage(chatId, messageId, inlineKeyboardMarkup);
                } else {
                    Flags flag = new Flags();
                    flag.setChatting(true);
                    ownersDogsFlagStatus.put(chatId, flag);
                    chatsDogsHandler.callVolunteer(update);
                }
            }
            case "/dog_receive_contacts" -> {
                OwnerDog ownerDog = ownersDogsHandler.returnOwnerDogDtoFromUpdate(update);
                ownersDogsFlagsHandler.createWaitingForContactsFlag(ownerDog);
                contactsHandler.askForContact(update);
            }
            default -> mainMenuHandler.noSuchCommandSendMessage(update);
        }
    }
}


