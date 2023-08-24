package omg.group.priuttelegrambot.handlers.owners.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.entity.pets.Cat;
import omg.group.priuttelegrambot.handlers.menu.CatsMenuHandler;
import omg.group.priuttelegrambot.handlers.owners.OwnersCatsHandler;
import omg.group.priuttelegrambot.handlers.updates.OwnUpdatesHandler;
import omg.group.priuttelegrambot.repository.owners.OwnersCatsRepository;
import omg.group.priuttelegrambot.service.owners.OwnersCatsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OwnersCatsHandlerImpl implements OwnersCatsHandler {

    private final TelegramBot telegramBot;
    private final CatsMenuHandler catsMenuHandler;
    private final OwnersCatsRepository ownersCatsRepository;
    private final OwnersCatsService ownersCatsService;
    private final OwnUpdatesHandler ownUpdatesHandler;

    public OwnersCatsHandlerImpl(TelegramBot telegramBot,
                                 CatsMenuHandler catsMenuHandler,
                                 OwnersCatsRepository ownersCatsRepository,
                                 OwnersCatsService ownersCatsService, OwnUpdatesHandler ownUpdatesHandler) {
        this.telegramBot = telegramBot;
        this.catsMenuHandler = catsMenuHandler;
        this.ownersCatsRepository = ownersCatsRepository;
        this.ownersCatsService = ownersCatsService;
        this.ownUpdatesHandler = ownUpdatesHandler;
    }

    /**
     * New user for the Cat's shelter registration - put in the database
     */
    @Override
    public void newOwnerRegister(Update update) {

        String userName = "";
        String firstName = "";
        String lastName = "";
        int date = 0;
        Long chatId = 0L;
        Long telegramUserId = 0L;

        if (update.message() != null) {
            userName = update.message().from().username();
            firstName = update.message().from().firstName();
            lastName = update.message().from().lastName();
            date = update.message().date();
            chatId = update.message().chat().id();
            telegramUserId = update.message().from().id();
        } else if (update.callbackQuery() != null) {
            userName = update.callbackQuery().from().username();
            firstName = update.callbackQuery().from().firstName();
            lastName = update.callbackQuery().from().lastName();
            date = update.callbackQuery().message().date();
            chatId = update.callbackQuery().message().chat().id();
            telegramUserId = update.callbackQuery().message().from().id();
        }

        LocalDateTime registrationDate = Instant.ofEpochSecond(date)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        if (!ownersCatsService.findByChatId(chatId)) {
            OwnerCatDto ownerCatDto = new OwnerCatDto();
            ownerCatDto.setUserName(userName);
            ownerCatDto.setName(firstName);
            ownerCatDto.setSurname(lastName);
            ownerCatDto.setCreatedAt(registrationDate);
            ownerCatDto.setIsVolunteer(false);
            ownerCatDto.setChatId(chatId);
            ownerCatDto.setTelegramUserId(telegramUserId);
            ownerCatDto.setVolunteerChatOpened(false);

            ownersCatsService.add(ownerCatDto);
        }
    }

    /**
     * Method checks if Owner of the Cat(s) exists
     */
    @Override
    public OwnerCat checkForOwnerExist(Update update) {

        Long chatId = ownUpdatesHandler.extractChatIdFromUpdate(update);

        Optional<OwnerCat> ownerCat = ownersCatsRepository.findByChatId(chatId);

        if (ownerCat.isPresent()) {
            return ownerCat.get();
        } else {
            InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForTakeMenuButton();
            telegramBot.execute(new SendMessage(chatId, """
                    Вы еще не зарегистрированы как владелец животного. Просмотрите информацию, как взять себе питомца.
                    Для этого нажмите соответствующу кнопку ниже""")
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup));
            return new OwnerCat();
        }
    }

    /**
     * Method checks if Owner has a Cat(s)
     */
    @Override
    @Transactional
    public List<Cat> checkForOwnerHasCat(OwnerCat ownerCat) {

        Long chatId = ownerCat.getChatId();

        if (!ownerCat.getCats().isEmpty()) {
            return ownerCat.getCats();
        } else {
            InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForTakeMenuButton();
            telegramBot.execute(new SendMessage(chatId, """
                    У Вас еще нет домашнего питомца и Вы не можете отправлять отчет. Просмотрите информацию, как взять себе питомца. 
                    Для этого нажмите соответствующу кнопку ниже
                    """)
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup));
            return new ArrayList<>();
        }
    }

    @Override
    public OwnerCat returnOwnerFromUpdate(Update update) {

        Long ownerChatId = ownUpdatesHandler.extractChatIdFromUpdate(update);

        Optional<OwnerCat> owner = ownersCatsRepository.findByIsVolunteerIsFalseAndChatId(ownerChatId);

        return owner.orElse(null);
    }

    @Override
    public OwnerCat returnVolunteerFromUpdate(Update update) {

        Long volunteerChatId = ownUpdatesHandler.extractChatIdFromUpdate(update);

        Optional<OwnerCat> volunteer = ownersCatsRepository.findByVolunteerIsTrueAndChatId(volunteerChatId);

        return volunteer.orElse(null);
    }
}
