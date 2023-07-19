package omg.group.priuttelegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import lombok.Data;
import omg.group.priuttelegrambot.entity.chats.ChatDogs;
import omg.group.priuttelegrambot.entity.report.ReportCatBoolean;
import omg.group.priuttelegrambot.entity.report.ReportDogBoolean;
import omg.group.priuttelegrambot.handlers.pets.CatsHandler;
import omg.group.priuttelegrambot.handlers.pets.DogsHandler;
import omg.group.priuttelegrambot.handlers.owners.OwnersCatsHandler;
import omg.group.priuttelegrambot.handlers.owners.OwnersDogsHandler;
import omg.group.priuttelegrambot.handlers.pets.impl.PetsHandler;
import omg.group.priuttelegrambot.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class TelegramBotUpdatesListener implements UpdatesListener {

    private static final Logger LOG = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final DogsHandler dogsHandler;
    private final CatsHandler catsHandler;
    private final PetsHandler otherHandler;
    private final OwnersDogsService ownersDogsService;
    private final OwnersCatsService ownersCatsService;
    private final OwnersCatsHandler ownersCatsHandler;
    private final OwnersDogsHandler ownersDogsHandler;
    private final KnowledgebaseCatsService knowledgebaseCatsService;
    private final KnowledgebaseDogsService knowledgebaseDogsService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot,
                                      DogsHandler dogsHandler,
                                      CatsHandler catsHandler,
                                      PetsHandler otherHandler,
                                      OwnersCatsService ownersCatsService,
                                      OwnersDogsService ownersDogsService,
                                      OwnersCatsHandler ownersCatsHandler,
                                      OwnersDogsHandler ownersDogsHandler,
                                      KnowledgebaseDogsService knowledgebaseDogsService,
                                      KnowledgebaseCatsService knowledgebaseCatsService) {
        this.telegramBot = telegramBot;
        this.telegramBot.setUpdatesListener(this);
        this.dogsHandler = dogsHandler;
        this.catsHandler = catsHandler;
        this.otherHandler = otherHandler;
        this.ownersCatsService = ownersCatsService;
        this.ownersDogsService = ownersDogsService;
        this.ownersCatsHandler = ownersCatsHandler;
        this.ownersDogsHandler = ownersDogsHandler;
        this.knowledgebaseCatsService = knowledgebaseCatsService;
        this.knowledgebaseDogsService = knowledgebaseDogsService;
    }

    @Override
    public int process(List<Update> updates) {
        updates.stream()
                .filter(update -> update.message() != null || update.callbackQuery() != null)
                .forEach(this::handleUpdate);
        return CONFIRMED_UPDATES_ALL;
    }

    private void handleUpdate(Update update) {

        if (update.callbackQuery().data().equals("/cats_reply") ||
                update.message().text().equals("/cats_reply")) {
           ownersCatsHandler.executeReplyButtonCommandForVolunteer(update);


            if (update.callbackQuery().data().equals("/close") ||
                    update.message().text().equals("/close")) {
                ownersCatsHandler.ex
            }


            if (update.message().text() != null &&
                    !update.message().text().contains("/start") &&
                    update.message().text().contains("/close")) {
                ownersCatsHandler.sendMessageReceived
            }


            if ((update.callbackQuery() != null &&
                (!update.callbackQuery().data().equals("/answer") || !update.callbackQuery().data().equals("/close"))) ||
                (update.message().text() != null && update.message().text().startsWith("/") &&
                        (!update.message().text().equals("/answer") || !update.message().text().equals("/close")))) {
            //&&
//                        !ownersCatsStatus.containsKey(update.message().from().id()) &&
//                        !ownersDogsStatus.containsKey(update.message().from().id())
            processButton(update);
        } else if ((update.callbackQuery() != null && update.callbackQuery().data().equals("/replay")) ||
                (update.message().text() != null && update.message().text().equals("/replay"))) {

        } else if ((update.callbackQuery() != null && update.callbackQuery().data().equals("/close")) ||
                (update.message().text() != null && update.message().text().equals("/close"))) {

        } else if (update.message() != null && ownersCatsStatus.containsKey(update.message().from().id())) {
            if (ownersCatsStatus.get(update.message().from().id()).equals(reportCatBoolean.photoIsNecessary())) {
                catsHandler.receivePhoto(update);
                ownersCatsStatus.remove(update.message().from().id());
            } else if (ownersCatsStatus.get(update.message().from().id()).equals(reportCatBoolean.rationIsNecessary())) {
                catsHandler.receiveRation(update);
                ownersCatsStatus.remove(update.message().from().id());
            } else if (ownersCatsStatus.get(update.message().from().id()).equals(reportCatBoolean.feelingIsNecessary())) {
                catsHandler.receiveFeeling(update);
                ownersCatsStatus.remove(update.message().from().id());
            } else if (ownersCatsStatus.get(update.message().from().id()).equals(reportCatBoolean.changesIsNecessary())) {
                catsHandler.receiveChanges(update);
                ownersCatsStatus.remove(update.message().from().id());
            }
        } else if (update.message() != null && ownersDogsStatus.containsKey(update.message().from().id())) {
            if (ownersDogsStatus.get(update.message().from().id()).equals(reportDogBoolean.photoIsNecessary())) {
                dogsHandler.receivePhoto(update);
                ownersDogsStatus.remove(update.message().from().id());
            } else if (ownersDogsStatus.get(update.message().from().id()).equals(reportDogBoolean.rationIsNecessary())) {
                dogsHandler.receiveRation(update);
                ownersDogsStatus.remove(update.message().from().id());
            } else if (ownersDogsStatus.get(update.message().from().id()).equals(reportDogBoolean.feelingIsNecessary())) {
                dogsHandler.receiveFeeling(update);
                ownersDogsStatus.remove(update.message().from().id());
            } else if (ownersDogsStatus.get(update.message().from().id()).equals(reportDogBoolean.changesIsNecessary())) {
                dogsHandler.receiveChanges(update);
                ownersDogsStatus.remove(update.message().from().id());
            }
        } else {
            otherHandler.noSuchCommandSendMessage(update);
        }
    }

    private void processButton(Update update) {

        Long chatId = 0L;
        String command = "";

        if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
            command = update.callbackQuery().data();
        } else if (update.message() != null && update.message().photo() == null) {
            chatId = update.message().chat().id();
            command = update.message().text();
        } else if (update.message() != null && update.message().photo() != null) {
            chatId = update.message().chat().id();
        }

        LOG.info("Получен следующий апдэйт {}", update);

        switch (command) {
            case "/start" -> {
                otherHandler.executeStartMenuButton(update);
            }
            case "/cat" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = catsHandler.formPriutMainMenuButton();
                catsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                catsHandler.newOwnerRegister(update);
            }
            case "/cat_info", "/cat_about", "/cat_timetable", "/cat_admission", "/cat_safety_measures" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = catsHandler.formInlineKeyboardForInfoMenuButton();
                catsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/cat_take", "/cat_connection_rules", "/cat_documents", "/cat_transportation", "/cat_kitty_at_home",
                    "/cat_at_home", "/cat_disability", "/cat_refusal_reasons" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = catsHandler.formInlineKeyboardForTakeMenuButton();
                catsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/cat_send_report" -> {
                catsHandler.checkForProbationPeriodSetAndValid(update);

                InlineKeyboardMarkup inlineKeyboardMarkup = catsHandler.formInlineKeyboardForSendReportButton();
                catsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);

            }
            case "/cat_send_photo" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = catsHandler.formInlineKeyboardForSendReportButton();
                catsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                ReportCatBoolean rcb = reportCatBoolean.photoIsNecessary();
                ownersCatsStatus.put(chatId, rcb);
            }
            case "/cat_send_ration" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = catsHandler.formInlineKeyboardForSendReportButton();
                catsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                ReportCatBoolean rcb = reportCatBoolean.rationIsNecessary();
                ownersCatsStatus.put(chatId, rcb);
            }
            case "/cat_send_feeling" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = catsHandler.formInlineKeyboardForSendReportButton();
                catsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                ReportCatBoolean rcb = reportCatBoolean.feelingIsNecessary();
                ownersCatsStatus.put(chatId, rcb);
            }
            case "/cat_send_changes" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = catsHandler.formInlineKeyboardForSendReportButton();
                catsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                ReportCatBoolean rcb = reportCatBoolean.changesIsNecessary();
                ownersCatsStatus.put(chatId, rcb);
            }
            case "/cat_back" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = catsHandler.formPriutMainMenuButton();
                catsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/cat_volunteer" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = catsHandler.formInlineKeyboardForSendReportButton();
                catsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);

                ownersCatsHandler.callVolunteer(update); // Message to user that volunteer is called and message to volunteer that someone called him
            }
            case "/cat_receive_contacts" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = catsHandler.formInlineKeyboardForSendReportButton();
                catsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }

            case "/dog" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = dogsHandler.formPriutMainMenuButton();
                dogsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                dogsHandler.newOwnerRegister(update);

            }
            case "/dog_info", "/dog_about", "/dog_timetable", "/dog_admission", "/dog_safety_measures" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = dogsHandler.formInlineKeyboardForInfoMenuButton();
                dogsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/dog_take", "/dog_connection_rules", "/dog_documents", "/dog_transportation", "/dog_puppy_at_home",
                    "/dog_at_home", "/dog_disability", "/dog_recommendations", "/dog_cynologist", "/dog_refusal_reasons" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = dogsHandler.formInlineKeyboardForTakeMenuButton();
                dogsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/dog_send_report" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = dogsHandler.formInlineKeyboardForSendReportButton();
                dogsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/dog_send_photo" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = dogsHandler.formInlineKeyboardForSendReportButton();
                dogsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                ReportDogBoolean rdb = reportDogBoolean.photoIsNecessary();
                ownersDogsStatus.put(chatId, rdb);
            }
            case "/dog_send_ration" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = dogsHandler.formInlineKeyboardForSendReportButton();
                dogsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                ReportDogBoolean rdb = reportDogBoolean.rationIsNecessary();
                ownersDogsStatus.put(chatId, rdb);

            }
            case "/dog_send_feeling" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = dogsHandler.formInlineKeyboardForSendReportButton();
                dogsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                ReportDogBoolean rdb = reportDogBoolean.feelingIsNecessary();
                ownersDogsStatus.put(chatId, rdb);
            }
            case "/dog_send_changes" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = dogsHandler.formInlineKeyboardForSendReportButton();
                dogsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                ReportDogBoolean rdb = reportDogBoolean.changesIsNecessary();
                ownersDogsStatus.put(chatId, rdb);
            }
            case "/dog_back" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = dogsHandler.formPriutMainMenuButton();
                dogsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/dog_volunteer" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = dogsHandler.formInlineKeyboardForSendReportButton();
                dogsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);

                ownersDogsHandler.callVolunteer(update);
                ChatDogs chatDogs = new ChatDogs();
                chatDogs.setIsChatting(true);
                volunteerDogsChatBooleanMap.put(chatId, chatDogs);
            }
            case "/dog_receive_contacts" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = dogsHandler.formInlineKeyboardForSendReportButton();
                dogsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            default -> otherHandler.noSuchCommandSendMessage(update);
        }
    }

    private void processChat(Update update) {

    }

    private void processReport(Update update) {

    }


}
