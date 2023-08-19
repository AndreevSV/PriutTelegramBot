package omg.group.priuttelegrambot.handlers.owners.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.entity.pets.Dog;
import omg.group.priuttelegrambot.handlers.menu.DogsMenuHandler;
import omg.group.priuttelegrambot.handlers.owners.OwnersDogsHandler;
import omg.group.priuttelegrambot.repository.owners.OwnersDogsRepository;
import omg.group.priuttelegrambot.service.owners.OwnersDogsService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OwnersDogsHandlerImpl implements OwnersDogsHandler {

    private final TelegramBot telegramBot;
    private final DogsMenuHandler dogsMenuHandler;
    private final OwnersDogsRepository ownersDogsRepository;
    private final OwnersDogsService ownersDogsService;

    public OwnersDogsHandlerImpl(TelegramBot telegramBot,
                                 DogsMenuHandler dogsMenuHandler,
                                 OwnersDogsRepository ownersDogsRepository,
                                 OwnersDogsService ownersDogsService) {
        this.telegramBot = telegramBot;
        this.dogsMenuHandler = dogsMenuHandler;
        this.ownersDogsRepository = ownersDogsRepository;
        this.ownersDogsService = ownersDogsService;
    }

    /**
     * New user for the Dog's shelter registration - put in the database
     */
    @Override
    public void newOwnerRegister(@NotNull Update update) {

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
            telegramUserId = update.callbackQuery().from().id();
        }

        LocalDateTime registrationDate = Instant.ofEpochSecond(date)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        if (!ownersDogsService.findByChatId(chatId)) {
            OwnerDogDto ownerDogDto = new OwnerDogDto();
            ownerDogDto.setUserName(userName);
            ownerDogDto.setName(firstName);
            ownerDogDto.setSurname(lastName);
            ownerDogDto.setCreatedAt(registrationDate);
            ownerDogDto.setIsVolunteer(false);
            ownerDogDto.setChatId(chatId);
            ownerDogDto.setTelegramUserId(telegramUserId);
            ownerDogDto.setVolunteerChatOpened(false);

            ownersDogsService.add(ownerDogDto);
        }
    }

    /**
     * Method checks if Owner of the Dog(s) exists
     */
    @Override
    public OwnerDog checkForOwnerExist(Update update) {

        Long chatId = update.message().from().id();
        Optional<OwnerDog> ownerDog = ownersDogsRepository.findByChatId(chatId);

        if (ownerDog.isPresent()) {
            return ownerDog.get();
        } else {
            InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForTakeMenuButton();
            telegramBot.execute(new SendMessage(chatId, """
                    Вы еще не зарегистрированы как владелец животного.
                    Просмотрите информацию, как взять себе питомца.
                    Для этого нажмите соответствующу кнопку ниже""")
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup));
            return new OwnerDog();
        }
    }

    /**
     * Method checks if Owner has a Dog(s)
     */
    @Override
    public List<Dog> checkForOwnerHasDog(OwnerDog ownerDog) {

        Long chatId = ownerDog.getChatId();

        if (!ownerDog.getDogs().isEmpty()) {
            return ownerDog.getDogs();
        } else {
            InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForTakeMenuButton();
            telegramBot.execute(new SendMessage(chatId, """
                    У Вас еще нет домашнего питомца
                    и Вы не можете отправлять отчет.
                    Просмотрите информацию, как взять себе питомца.
                    Для этого нажмите соответствующу кнопку ниже""")
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup));
            return new ArrayList<>();
        }
    }

    @Override
    public OwnerDog returnOwnerFromUpdate(@NotNull Update update) {

        Long ownerChatId;

        if (update.callbackQuery() != null) {
            ownerChatId = update.callbackQuery().from().id();
        } else {
            ownerChatId = update.message().from().id();
        }

        Optional<OwnerDog> owner = ownersDogsRepository.findByVolunteerIsFalseAndChatId(ownerChatId);

        return owner.orElse(null);
    }

    @Override
    public OwnerDog returnVolunteerFromUpdate(@NotNull Update update) {

        Long volunteerChatId;

        if (update.callbackQuery() != null) {
            volunteerChatId = update.callbackQuery().from().id();
        } else {
            volunteerChatId = update.message().from().id();
        }

        Optional<OwnerDog> volunteerOptional = ownersDogsRepository.findByVolunteerIsTrueAndChatId(volunteerChatId);

        if (volunteerOptional.isPresent()) {

            return volunteerOptional.get().getVolunteer();
        }
        return null;
    }
}
