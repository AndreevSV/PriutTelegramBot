package omg.group.priuttelegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import lombok.Data;
import omg.group.priuttelegrambot.handlers.AnimalHandler;
import omg.group.priuttelegrambot.handlers.impl.OtherHandlers;
import omg.group.priuttelegrambot.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class TelegramBotUpdatesListener implements UpdatesListener {
    private Long chatId;

    private int messageId;

    private String command;

    private String text;

    private String userName;

    private String firstName;

    private String lastName;

    private static final Logger LOG = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;

    private final KnowledgebaseCatsService knowledgebaseCatsService;

    private final KnowledgebaseDogsService knowledgebaseDogsService;

    private final OwnersDogsService ownersDogsService;

    private final OwnersCatsService ownersCatsService;

    private final AnimalHandler dogsHandler;

    private final AnimalHandler catsHandler;

    private final OtherHandlers otherHandler;

    public TelegramBotUpdatesListener(TelegramBot telegramBot,
                                      KnowledgebaseDogsService knowledgebaseDogsService,
                                      KnowledgebaseCatsService knowledgebaseCatsService,
                                      OwnersCatsService ownersCatsService,
                                      OwnersDogsService ownersDogsService,
                                      @Qualifier("dogsHandler") AnimalHandler dogsHandler,
                                      @Qualifier("catsHandler") AnimalHandler catsHandler,
                                      OtherHandlers otherHandler) {
        this.telegramBot = telegramBot;
        this.dogsHandler = dogsHandler;
        this.catsHandler = catsHandler;
        this.otherHandler = otherHandler;
        this.telegramBot.setUpdatesListener(this);
        this.knowledgebaseCatsService = knowledgebaseCatsService;
        this.knowledgebaseDogsService = knowledgebaseDogsService;
        this.ownersCatsService = ownersCatsService;
        this.ownersDogsService = ownersDogsService;
    }

    @Override
    public int process(List<Update> updates) {
        updates.stream().filter(update -> update.message() != null || update.callbackQuery() != null).forEach(this::handleUpdate);
        return CONFIRMED_UPDATES_ALL;
    }

    private void handleUpdate(Update update) {
        if (update.message() != null && update.message().text() != null || update.callbackQuery() != null) {
            processCommand(update);
        } else {
            otherHandler.noSuchCommandSendMessage(update);
        }
    }

    public void extractDataFromUpdate(Update update) {
        if (update.message() != null) {
            chatId = update.message().chat().id();
            messageId = update.message().messageId();
            command = update.message().text();
            userName = update.message().from().username();
            firstName = update.message().from().firstName();
            lastName = update.message().from().lastName();
        } else if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
            messageId = update.callbackQuery().message().messageId();
            command = update.callbackQuery().data();
            userName = update.callbackQuery().from().username();
            firstName = update.callbackQuery().from().firstName();
            lastName = update.callbackQuery().from().lastName();
        }
    }


    private void processCommand(Update update) {

        LOG.info("Получен следующий апдэйт {}", update);

        extractDataFromUpdate(update);

        switch (command) {
            case "/start" -> otherHandler.executeStartMenuButton(update);
            case "/cat" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = catsHandler.formPriutMainMenuButton();
                catsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
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
                InlineKeyboardMarkup inlineKeyboardMarkup = catsHandler.formInlineKeyboardForSendReportButton();
                catsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/cat_send_photo" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = catsHandler.formInlineKeyboardForSendReportButton();
                catsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                catsHandler.receivePhoto(update);
            }
            case "/cat_send_ration" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = catsHandler.formInlineKeyboardForSendReportButton();
                catsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                catsHandler.receiveRation(update);

            }
            case "/cat_send_feeling" -> {

                InlineKeyboardMarkup inlineKeyboardMarkup = catsHandler.formInlineKeyboardForSendReportButton();
                catsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                catsHandler.receiveFeeling(update);
            }
            case "/cat_send_changes" -> {

                InlineKeyboardMarkup inlineKeyboardMarkup = catsHandler.formInlineKeyboardForSendReportButton();
                catsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                catsHandler.receiveChanges(update);
            }
            case "/cat_back" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = catsHandler.formInlineKeyboardForSendReportButton();
                catsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/cat_volunteer" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = catsHandler.formInlineKeyboardForSendReportButton();
                catsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
//                callCatsVolunteer();
            }
            case "/cat_receive_contacts" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = catsHandler.formInlineKeyboardForSendReportButton();
                catsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }

            case "/dog" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = dogsHandler.formPriutMainMenuButton();
                dogsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
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

            }
            case "/dog_send_photo" -> {
            }
            case "/dog_send_ration" -> {
            }
            case "/dog_send_feeling" -> {
            }
            case "/dog_send_changes" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = dogsHandler.formInlineKeyboardForSendReportButton();
                dogsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/dog_back" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = dogsHandler.formPriutMainMenuButton();
                dogsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/dog_volunteer" -> {/////////////////////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                {
                    InlineKeyboardMarkup inlineKeyboardMarkup = dogsHandler.formInlineKeyboardForSendReportButton();
                    dogsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                }
//                callDogsVolunteer();
            }
            case "/dog_receive_contacts" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = dogsHandler.formInlineKeyboardForSendReportButton();
                dogsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }

            default -> otherHandler.noSuchCommandSendMessage(update);
        }
    }

}
