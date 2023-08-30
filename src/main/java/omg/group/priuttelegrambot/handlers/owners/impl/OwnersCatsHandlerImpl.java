package omg.group.priuttelegrambot.handlers.owners.impl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.dto.owners.OwnerCatMapper;
import omg.group.priuttelegrambot.dto.pets.CatDto;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.handlers.menu.CatsMenuHandler;
import omg.group.priuttelegrambot.handlers.menu.MainMenuHandler;
import omg.group.priuttelegrambot.handlers.owners.OwnersCatsHandler;
import omg.group.priuttelegrambot.handlers.updates.OwnUpdatesHandler;
import omg.group.priuttelegrambot.repository.owners.OwnersCatsRepository;
import omg.group.priuttelegrambot.service.owners.OwnersCatsService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class OwnersCatsHandlerImpl implements OwnersCatsHandler {

    private final CatsMenuHandler catsMenuHandler;
    private final OwnersCatsRepository ownersCatsRepository;
    private final OwnersCatsService ownersCatsService;
    private final OwnUpdatesHandler ownUpdatesHandler;
    private final MainMenuHandler mainMenuHandler;

    public OwnersCatsHandlerImpl(CatsMenuHandler catsMenuHandler,
                                 OwnersCatsRepository ownersCatsRepository,
                                 OwnersCatsService ownersCatsService,
                                 OwnUpdatesHandler ownUpdatesHandler,
                                 MainMenuHandler mainMenuHandler) {
        this.catsMenuHandler = catsMenuHandler;
        this.ownersCatsRepository = ownersCatsRepository;
        this.ownersCatsService = ownersCatsService;
        this.ownUpdatesHandler = ownUpdatesHandler;
        this.mainMenuHandler = mainMenuHandler;
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
    public OwnerCatDto checkForOwnerExist(Update update) {

        Long chatId = ownUpdatesHandler.getChatId(update);
        Optional<OwnerCat> ownerCat = ownersCatsRepository.findByChatId(chatId);

        if (ownerCat.isPresent()) {
            OwnerCat owner = ownerCat.get();
            return OwnerCatMapper.toDto(owner);
        } else {
            InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForTakeMenuButton();
            mainMenuHandler.noOwnOfPetRegisteredMessage(chatId, inlineKeyboardMarkup);
            return null;
        }
    }

    /**
     * Method checks if Owner has a Cat(s)
     */
    @Override
    public List<CatDto> checkForOwnerHasCat(OwnerCatDto ownerDto) {

        Long chatId = ownerDto.getChatId();

        if (!ownerDto.getCatsDto().isEmpty()) {
            return ownerDto.getCatsDto();
        } else {
            InlineKeyboardMarkup inlineKeyboardMarkup = catsMenuHandler.formInlineKeyboardForTakeMenuButton();
            mainMenuHandler.noPetMessage(chatId, inlineKeyboardMarkup);
            return null;
        }
    }

    @Override
    public OwnerCatDto returnOwnerCatDtoFromUpdate(Update update) {
        Long ownerChatId = ownUpdatesHandler.getChatId(update);
        Optional<OwnerCat> ownerCat = ownersCatsRepository.findByIsVolunteerIsFalseAndChatId(ownerChatId);

        if (ownerCat.isPresent()) {
            OwnerCat owner = ownerCat.get();
            return OwnerCatMapper.toDto(owner);
        } else {
            return null;
        }
    }

    @Override
    public OwnerCatDto returnVolunteerCatDtoFromUpdate(Update update) {
        Long volunteerChatId = ownUpdatesHandler.getChatId(update);
        Optional<OwnerCat> volunteerCat = ownersCatsRepository.findByIsVolunteerIsTrueAndChatId(volunteerChatId);

        if (volunteerCat.isPresent()) {
            OwnerCat volunteer = volunteerCat.get();
            return OwnerCatMapper.toDto(volunteer);
        } else {
            return null;
        }
    }

    @Override
    public OwnerCatDto returnOwnerOrVolunteerCatDtoByChatId(Long chatId) {
        Optional<OwnerCat> ownerOptional = ownersCatsRepository.findByChatId(chatId);
        if (ownerOptional.isPresent()) {
            OwnerCat owner = ownerOptional.get();
            return OwnerCatMapper.toDto(owner);
        } else {
            return null;
        }
    }
}
