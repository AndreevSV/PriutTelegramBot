package omg.group.priuttelegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import lombok.Data;
import omg.group.priuttelegrambot.handlers.contacts.impl.ContactsHandler;
import omg.group.priuttelegrambot.handlers.menu.CatsMenuHandler;
import omg.group.priuttelegrambot.handlers.menu.DogsMenuHandler;
import omg.group.priuttelegrambot.handlers.menu.MainMenuHandler;
import omg.group.priuttelegrambot.handlers.owners.OwnersCatsHandler;
import omg.group.priuttelegrambot.handlers.owners.OwnersDogsHandler;
import omg.group.priuttelegrambot.handlers.reports.ReportsCatsHandler;
import omg.group.priuttelegrambot.handlers.reports.ReportsDogsHandler;
import omg.group.priuttelegrambot.handlers.chats.ChatsCatsHandler;
import omg.group.priuttelegrambot.handlers.chats.ChatsDogsHandler;
import omg.group.priuttelegrambot.dto.flags.OwnerCatFlag;
import omg.group.priuttelegrambot.dto.flags.OwnerDogFlag;
import omg.group.priuttelegrambot.handlers.updates.OwnUpdatesHandler;
import omg.group.priuttelegrambot.service.knowledgebases.KnowledgebaseCatsService;
import omg.group.priuttelegrambot.service.knowledgebases.KnowledgebaseDogsService;
import omg.group.priuttelegrambot.service.owners.OwnersCatsService;
import omg.group.priuttelegrambot.service.owners.OwnersDogsService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    private final OwnerCatFlag ownerCatFlag;
    private final OwnerDogFlag ownerDogFlag;

    private final OwnUpdatesHandler ownUpdatesHandler;

    private final ContactsHandler contactsHandler;

    Map<Long, OwnerCatFlag> ownersCatsFlagStatus = new ConcurrentHashMap<>();
    Map<Long, OwnerDogFlag> ownersDogsFlagStatus = new ConcurrentHashMap<>();

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

                                      OwnerCatFlag ownerCatFlag,
                                      OwnerDogFlag ownerDogFlag,

                                      OwnUpdatesHandler ownUpdatesHandler,
                                      ContactsHandler contactsHandler) {
        this.telegramBot = telegramBot;
        this.ownUpdatesHandler = ownUpdatesHandler;
        this.contactsHandler = contactsHandler;
        this.telegramBot.setUpdatesListener(this);

        this.ownersCatsHandler = ownersCatsHandler;
        this.ownersDogsHandler = ownersDogsHandler;

        this.catsMenuHandler = catsMenuHandler;
        this.dogsMenuHandler = dogsMenuHandler;

        this.reportsCatsHandler = reportsCatsHandler;
        this.reportsDogsHandler = reportsDogsHandler;
        this.mainMenuHandler = mainMenuHandler;

        this.ownersCatsService = ownersCatsService;
        this.ownersDogsService = ownersDogsService;

        this.chatsCatsHandler = chatsCatsHandler;
        this.chatsDogsHandler = chatsDogsHandler;

        this.knowledgebaseCatsService = knowledgebaseCatsService;
        this.knowledgebaseDogsService = knowledgebaseDogsService;

        this.ownerCatFlag = ownerCatFlag;
        this.ownerDogFlag = ownerDogFlag;
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

        Long chatId = ownUpdatesHandler.extractChatIdFromUpdate(update);
        String message = ownUpdatesHandler.extractTextFromUpdate(update);

        if (ownersCatsFlagStatus.containsKey(chatId)) {

            OwnerCatFlag ownerCatFlag = ownersCatsFlagStatus.get(chatId);

            boolean waitingForPhoto = ownerCatFlag.isWaitingForPhoto();
            boolean waitingForRation = ownerCatFlag.isWaitingForRation();
            boolean waitingForFeeling = ownerCatFlag.isWaitingForFeeling();
            boolean waitingForChanges = ownerCatFlag.isWaitingForChanges();
            boolean waitingForContacts = ownerCatFlag.isWaitingForContacts();
            boolean chatting = ownerCatFlag.isChatting();

            if (update.callbackQuery() == null && update.message() != null) {

                if (waitingForPhoto) {
                    boolean photo = reportsCatsHandler.receivePhoto(update);
                    if (photo) {
                        ownersCatsFlagStatus.remove(chatId);
                    }
                } else if (waitingForRation) {
                    boolean ration = reportsCatsHandler.receiveRation(update);
                    if (ration) {
                        ownersCatsFlagStatus.remove(chatId);
                    }
                } else if (waitingForChanges) {
                    boolean changes = reportsCatsHandler.receiveChanges(update);
                    if (changes) {
                        ownersCatsFlagStatus.remove(chatId);
                    }
                } else if (waitingForFeeling) {
                    boolean feeling = reportsCatsHandler.receiveFeeling(update);
                    if (feeling) {
                        ownersCatsFlagStatus.remove(chatId);
                    }
                } else if (waitingForContacts) {
//                    boolean contacts = reportsCatsHandler.receiveContacts();
//                    if (contacts) {
//                        ownersCatsFlagStatus.remove(chatId);
//                    }
                } else if (chatting) {
                    if (message.equals("/cats_close")) {
                        chatsCatsHandler.executeCloseButtonCommand(update);
                        ownersCatsFlagStatus.remove(chatId);
                    } else if (message.equals("/cats_reply")) {
                        chatsCatsHandler.executeReplyButtonCommandForVolunteer(update);
                    } else {
                        chatsCatsHandler.sendMessageReceived(update);
                    }
                }
            } else if (update.callbackQuery() != null) {
                if (waitingForRation) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForInfoMenuButton();
                    mainMenuHandler.executeSendRationButton(chatId, inlineKeyboardMarkup);
                } else if (waitingForFeeling) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForInfoMenuButton();
                    mainMenuHandler.executeSendFeelingButton(chatId, inlineKeyboardMarkup);
                } else if (waitingForChanges) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForInfoMenuButton();
                    mainMenuHandler.executeSendChangesButton(chatId, inlineKeyboardMarkup);
                } else if (waitingForPhoto) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForInfoMenuButton();
                    mainMenuHandler.executeSendPhotoButton(chatId, inlineKeyboardMarkup);
                } else if (waitingForContacts) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForInfoMenuButton();
                    mainMenuHandler.executeSendContactsButton(chatId, inlineKeyboardMarkup);
                } else if (chatting) {
                    if (message.equals("/cats_close")) {
                        chatsCatsHandler.executeCloseButtonCommand(update);
                        ownersCatsFlagStatus.remove(chatId);
                } else if (message.equals("/cats_reply")) {
                    chatsCatsHandler.executeReplyButtonCommandForVolunteer(update);
                } else {
                        InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formPriutMainMenuButton();
                        mainMenuHandler.executeCallVolunteerButton(chatId, inlineKeyboardMarkup);
                    }
                }
            }
        } else if (ownersDogsFlagStatus.containsKey(chatId)) {

            OwnerDogFlag ownerDogFlag = ownersDogsFlagStatus.get(chatId);

            boolean waitingForPhoto = ownerDogFlag.isWaitingForPhoto();
            boolean waitingForRation = ownerDogFlag.isWaitingForRation();
            boolean waitingForFeeling = ownerDogFlag.isWaitingForFeeling();
            boolean waitingForChanges = ownerDogFlag.isWaitingForChanges();
            boolean waitingForContacts = ownerDogFlag.isWaitingForContacts();
            boolean isChatting = ownerDogFlag.isChatting();

            if (update.callbackQuery() == null && update.message() != null) {
                if (waitingForRation) {
                    boolean ration = reportsDogsHandler.receiveRation(update);
                    if (ration) {
                        ownersDogsFlagStatus.remove(chatId);
                    }
                } else if (waitingForFeeling) {
                    boolean feeling = reportsDogsHandler.receiveFeeling(update);
                    if (feeling) {
                        ownersDogsFlagStatus.remove(chatId);
                    }
                } else if (waitingForChanges) {
                    boolean changes = reportsDogsHandler.receiveChanges(update);
                    if (changes) {
                        ownersDogsFlagStatus.remove(chatId);
                    }
                } else if (waitingForPhoto) {
                    boolean photo = reportsDogsHandler.receivePhoto(update);
                    if (photo) {
                        ownersDogsFlagStatus.remove(chatId);
                    }
                } else if (waitingForContacts) {
//                    boolean contacts = reportsDogsHandler.receiveContacts();
//                    if (contacts) {
//                        ownersDogsFlagStatus.remove(chatId);
//                    }
                } else if (isChatting) {
                    chatsDogsHandler.sendMessageReceived(update);
                }
            } else if (update.callbackQuery() != null) {
                if (waitingForRation) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForInfoMenuButton();
                    mainMenuHandler.executeSendRationButton(chatId, inlineKeyboardMarkup);
                } else if (waitingForFeeling) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForInfoMenuButton();
                    mainMenuHandler.executeSendFeelingButton(chatId, inlineKeyboardMarkup);
                } else if (waitingForChanges) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForInfoMenuButton();
                    mainMenuHandler.executeSendChangesButton(chatId, inlineKeyboardMarkup);
                } else if (waitingForPhoto) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForInfoMenuButton();
                    mainMenuHandler.executeSendPhotoButton(chatId, inlineKeyboardMarkup);
                } else if (waitingForContacts) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForInfoMenuButton();
                    mainMenuHandler.executeSendContactsButton(chatId, inlineKeyboardMarkup);
                } else if (isChatting) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForInfoMenuButton();
                    mainMenuHandler.executeCallVolunteerButton(chatId, inlineKeyboardMarkup);
                }
            }
        } else {
            processButton(update);
        }
    }

    private void processButton(@NotNull Update update) {

        LOG.info("Получен следующий апдэйт {}", update);

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
                if (reportsCatsHandler.isReportCompleted(update)) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formPriutMainMenuButton();
                    mainMenuHandler.reportAlreadySent(chatId, messageId, inlineKeyboardMarkup);
                } else {
                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                    catsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                }
            }
            case "/cat_send_photo" -> {
                if (reportsCatsHandler.isPhoto(update)) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                    mainMenuHandler.photoAlreadySent(chatId, messageId, inlineKeyboardMarkup);
                } else {
                    OwnerCatFlag flag = new OwnerCatFlag();
                    flag.setWaitingForPhoto(true);
                    ownersCatsFlagStatus.put(chatId, flag);
                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                    catsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                }
            }
            case "/cat_send_ration" -> {
                if (reportsCatsHandler.isRation(update)) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                    mainMenuHandler.rationAlreadySent(chatId, messageId, inlineKeyboardMarkup);
                } else {
                    OwnerCatFlag flag = new OwnerCatFlag();
                    flag.setWaitingForRation(true);
                    ownersCatsFlagStatus.put(chatId, flag);
                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                    catsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                }
            }
            case "/cat_send_feeling" -> {
                if (reportsCatsHandler.isFeeling(update)) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                    mainMenuHandler.feelingAlreadySent(chatId, messageId, inlineKeyboardMarkup);
                } else {
                    OwnerCatFlag flag = new OwnerCatFlag();
                    flag.setWaitingForFeeling(true);
                    ownersCatsFlagStatus.put(chatId, flag);
                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                    catsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                }
            }
            case "/cat_send_changes" -> {
                if (reportsCatsHandler.isChanges(update)) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                    mainMenuHandler.changesAlreadySent(chatId, messageId, inlineKeyboardMarkup);
                } else {
                    OwnerCatFlag flag = new OwnerCatFlag();
                    flag.setWaitingForChanges(true);
                    ownersCatsFlagStatus.put(chatId, flag);
                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForSendReportButton();
                    catsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                }
            }
            case "/cat_back" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formPriutMainMenuButton();
                catsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/cat_volunteer" -> {
                if (ownersCatsFlagStatus.containsKey(chatId) && ownersCatsFlagStatus.get(chatId).isChatting()) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formPriutMainMenuButton();
                    mainMenuHandler.chattingAlready(chatId, messageId, inlineKeyboardMarkup);
                } else {
                    OwnerCatFlag flag = new OwnerCatFlag();
                    flag.setChatting(true);
                    ownersCatsFlagStatus.put(chatId, flag);
                    chatsCatsHandler.callVolunteer(update);
                }
            }
            case "/cat_receive_contacts" -> {
                OwnerCatFlag flag = new OwnerCatFlag();
                flag.setWaitingForContacts(true);
                ownersCatsFlagStatus.put(chatId, flag);
                contactsHandler.askForContact(update);
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
                if (reportsDogsHandler.isReportCompleted(update)) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formPriutMainMenuButton();
                    mainMenuHandler.reportAlreadySent(chatId, messageId, inlineKeyboardMarkup);
                } else {
                    InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                    dogsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                }
            }
            case "/dog_send_photo" -> {
                if (reportsDogsHandler.isPhoto(update)) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                    mainMenuHandler.photoAlreadySent(chatId, messageId, inlineKeyboardMarkup);
                } else {
                    OwnerDogFlag flag = new OwnerDogFlag();
                    flag.setWaitingForPhoto(true);
                    ownersDogsFlagStatus.put(chatId, flag);
                    InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                    dogsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                }
            }
            case "/dog_send_ration" -> {
                if (reportsDogsHandler.isRation(update)) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                    mainMenuHandler.rationAlreadySent(chatId, messageId, inlineKeyboardMarkup);
                } else {
                    OwnerDogFlag flag = new OwnerDogFlag();
                    flag.setWaitingForRation(true);
                    ownersDogsFlagStatus.put(chatId, flag);
                    InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                    dogsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                }
            }
            case "/dog_send_feeling" -> {
                if (reportsDogsHandler.isFeeling(update)) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                    mainMenuHandler.feelingAlreadySent(chatId, messageId, inlineKeyboardMarkup);
                } else {
                    OwnerDogFlag flag = new OwnerDogFlag();
                    flag.setWaitingForFeeling(true);
                    ownersDogsFlagStatus.put(chatId, flag);
                    InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                    dogsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                }
            }
            case "/dog_send_changes" -> {
                if (reportsDogsHandler.isChanges(update)) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                    mainMenuHandler.changesAlreadySent(chatId, messageId, inlineKeyboardMarkup);
                } else {
                    OwnerDogFlag flag = new OwnerDogFlag();
                    flag.setWaitingForChanges(true);
                    ownersDogsFlagStatus.put(chatId, flag);
                    InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                    dogsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                }
            }
            case "/dog_back" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formPriutMainMenuButton();
                dogsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/dog_volunteer" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForSendReportButton();
                dogsMenuHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                chatsDogsHandler.callVolunteer(update);
            }
            case "/dogs_reply" -> {
                chatsDogsHandler.executeReplyButtonCommandForVolunteer(update);
            }
            case "/dogs_close" -> {
                chatsDogsHandler.executeCloseButtonCommand(update);
                ownersDogsFlagStatus.remove(chatId);
            }
            case "/dog_receive_contacts" -> {
                OwnerDogFlag flag = new OwnerDogFlag();
                flag.setWaitingForContacts(true);
                ownersDogsFlagStatus.put(chatId, flag);
                contactsHandler.askForContact(update);
            }
            default -> mainMenuHandler.noSuchCommandSendMessage(update);
        }
    }


}


