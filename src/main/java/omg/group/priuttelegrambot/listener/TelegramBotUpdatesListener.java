package omg.group.priuttelegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import lombok.Data;
import omg.group.priuttelegrambot.entity.chats.ChatCats;
import omg.group.priuttelegrambot.entity.chats.ChatDogs;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.handlers.pets.CatsHandler;
import omg.group.priuttelegrambot.handlers.pets.DogsHandler;
import omg.group.priuttelegrambot.handlers.owners.OwnersCatsHandler;
import omg.group.priuttelegrambot.handlers.owners.OwnersDogsHandler;
import omg.group.priuttelegrambot.handlers.pets.impl.PetsHandler;
import omg.group.priuttelegrambot.repository.chats.ChatsCatsRepository;
import omg.group.priuttelegrambot.repository.chats.ChatsDogsRepository;
import omg.group.priuttelegrambot.repository.owners.OwnersCatsRepository;
import omg.group.priuttelegrambot.repository.owners.OwnersDogsRepository;
import omg.group.priuttelegrambot.service.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

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
    private final OwnersCatsRepository ownersCatsRepository;
    private final ChatsCatsRepository chatsCatsRepository;
    private final ChatsDogsRepository chatsDogsRepository;
    private final OwnersDogsRepository ownersDogsRepository;

    private Map<Long, Boolean> awaitingCatPhotoMap = new ConcurrentHashMap<>();
    private Map<Long, Boolean> awaitingCatRationMap = new ConcurrentHashMap<>();
    private Map<Long, Boolean> awaitingCatFeelingMap = new ConcurrentHashMap<>();
    private Map<Long, Boolean> awaitingCatChangesMap = new ConcurrentHashMap<>();


    public TelegramBotUpdatesListener(TelegramBot telegramBot,
                                      DogsHandler dogsHandler,
                                      CatsHandler catsHandler,
                                      PetsHandler otherHandler,
                                      OwnersCatsService ownersCatsService,
                                      OwnersDogsService ownersDogsService,
                                      OwnersCatsHandler ownersCatsHandler,
                                      OwnersDogsHandler ownersDogsHandler,
                                      KnowledgebaseDogsService knowledgebaseDogsService,
                                      KnowledgebaseCatsService knowledgebaseCatsService,
                                      OwnersCatsRepository ownersCatsRepository,
                                      ChatsCatsRepository chatsCatsRepository,
                                      ChatsDogsRepository chatsDogsRepository,
                                      OwnersDogsRepository ownersDogsRepository) {
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
        this.ownersCatsRepository = ownersCatsRepository;
        this.chatsCatsRepository = chatsCatsRepository;
        this.chatsDogsRepository = chatsDogsRepository;
        this.ownersDogsRepository = ownersDogsRepository;
    }

    @Override
    public int process(List<Update> updates) {
        updates.stream()
                .filter(update -> update.message() != null || update.callbackQuery() != null)
                .forEach(this::handleUpdate);
        return CONFIRMED_UPDATES_ALL;
    }

    private void handleUpdate(@NotNull Update update) {

        Long chatId = 0L;

        if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
        } else if (update.message() != null) {
            chatId = update.message().chat().id();
        }

//        Optional<OwnerCat> ownerCatOptional = ownersCatsRepository.findByChatId(chatId);
//
//        if (ownerCatOptional.isPresent()) {
//
//            Optional<ChatCats> chatCatsOptional = chatsCatsRepository.findByOwnerCatId(chatId);
//
//            if (chatCatsOptional.isPresent()) {
//                processCatChat(update);
//            }
//        } else {
//            Optional<OwnerDog> ownerDogOptional = ownersDogsRepository.findByChatId(chatId);
//
//            if (ownerDogOptional.isPresent()) {
//
//                Optional<ChatDogs> chatDogsOptional = chatsDogsRepository.findByOwnerDogId(chatId);
//
//                if (chatDogsOptional.isPresent()) {
//                    processDogChat(update);
//                }
//            }
//        }
        if ((update.callbackQuery() != null) ||
                (update.message() != null && update.message().text() != null && update.message().text().startsWith("/"))) {
            processButton(update);
        } else if (update.message().photo() != null && awaitingCatPhotoMap.getOrDefault(chatId, false)) {
            catsHandler.receivePhoto(update);
            awaitingCatPhotoMap.put(chatId, false);
        } else if (awaitingCatRationMap.getOrDefault(chatId, false)) {
            catsHandler.receiveRation(update);
            awaitingCatRationMap.put(chatId, false);
        } else if (awaitingCatFeelingMap.getOrDefault(chatId, false)) {
            catsHandler.receiveFeeling(update);
            awaitingCatFeelingMap.put(chatId, false);
        } else if (awaitingCatChangesMap.getOrDefault(chatId, false)) {
            catsHandler.receiveChanges(update);
            awaitingCatChangesMap.put(chatId, false);
        } else {
            otherHandler.noSuchCommandSendMessage(update);
        }
    }

//    private void handleUpdate(@NotNull Update update) {
//        otherHandler.noSuchCommandSendMessage(update);
//    }

    private void processCatChat(Update update) {

        Long chatId = update.message().chat().id();

        Optional<ChatCats> chat = chatsCatsRepository.findByChatId(chatId);

        if (chat.isPresent()) {
            ownersCatsHandler.sendMessageReceived(update);
        }
    }

    private void processDogChat(Update update) {

        Long chatId = update.message().chat().id();

        Optional<ChatDogs> chat = chatsDogsRepository.findByChatId(chatId);

        if (chat.isPresent()) {
            ownersDogsHandler.sendMessageReceived(update);
        }
    }

    private void processButton(@NotNull Update update) {

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
                InlineKeyboardMarkup inlineKeyboardMarkup = catsHandler.formInlineKeyboardForSendReportButton();
                catsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/cat_send_photo" -> {
                awaitingCatPhotoMap.put(chatId, true);
                InlineKeyboardMarkup inlineKeyboardMarkup = catsHandler.formInlineKeyboardForSendReportButton();
                catsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/cat_send_ration" -> {
                awaitingCatRationMap.put(chatId, true);
                InlineKeyboardMarkup inlineKeyboardMarkup = catsHandler.formInlineKeyboardForSendReportButton();
                catsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/cat_send_feeling" -> {
                awaitingCatFeelingMap.put(chatId, true);
                InlineKeyboardMarkup inlineKeyboardMarkup = catsHandler.formInlineKeyboardForSendReportButton();
                catsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/cat_send_changes" -> {
                awaitingCatChangesMap.put(chatId, true);
                InlineKeyboardMarkup inlineKeyboardMarkup = catsHandler.formInlineKeyboardForSendReportButton();
                catsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/cat_back" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = catsHandler.formPriutMainMenuButton();
                catsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/cat_volunteer" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = catsHandler.formInlineKeyboardForSendReportButton();
                catsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                ownersCatsHandler.callVolunteer(update);
            }
            case "/cats_reply" -> {
                ownersCatsHandler.executeReplyButtonCommandForVolunteer(update);
            }
            case "/cats_close" -> {
                ownersCatsHandler.executeCloseButtonCommand(update);
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
            }
            case "/dog_send_ration" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = dogsHandler.formInlineKeyboardForSendReportButton();
                dogsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/dog_send_feeling" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = dogsHandler.formInlineKeyboardForSendReportButton();
                dogsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/dog_send_changes" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = dogsHandler.formInlineKeyboardForSendReportButton();
                dogsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/dog_back" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = dogsHandler.formPriutMainMenuButton();
                dogsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            case "/dog_volunteer" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = dogsHandler.formInlineKeyboardForSendReportButton();
                dogsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
                ownersDogsHandler.callVolunteer(update);
            }
            case "/dogs_reply" -> {
                ownersDogsHandler.executeReplyButtonCommandForVolunteer(update);
            }
            case "/dogs_close" -> {
                ownersDogsHandler.executeCloseButtonCommand(update);
            }
            case "/dog_receive_contacts" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = dogsHandler.formInlineKeyboardForSendReportButton();
                dogsHandler.executeButtonOrCommand(update, inlineKeyboardMarkup);
            }
            default -> otherHandler.noSuchCommandSendMessage(update);
        }
    }
}


