package omg.group.priuttelegrambot.handlers.owners.impl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;
import omg.group.priuttelegrambot.dto.owners.OwnerDogMapper;
import omg.group.priuttelegrambot.dto.pets.DogDto;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.handlers.menu.DogsMenuHandler;
import omg.group.priuttelegrambot.handlers.menu.MainMenuHandler;
import omg.group.priuttelegrambot.handlers.owners.OwnersDogsHandler;
import omg.group.priuttelegrambot.handlers.updates.OwnUpdatesHandler;
import omg.group.priuttelegrambot.repository.owners.OwnersDogsRepository;
import omg.group.priuttelegrambot.service.owners.OwnersDogsService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class OwnersDogsHandlerImpl implements OwnersDogsHandler {

    private final DogsMenuHandler dogsMenuHandler;
    private final OwnersDogsRepository ownersDogsRepository;
    private final OwnersDogsService ownersDogsService;
    private final OwnUpdatesHandler ownUpdatesHandler;
    private final MainMenuHandler mainMenuHandler;

    public OwnersDogsHandlerImpl(DogsMenuHandler dogsMenuHandler,
                                 OwnersDogsRepository ownersDogsRepository,
                                 OwnersDogsService ownersDogsService,
                                 OwnUpdatesHandler ownUpdatesHandler,
                                 MainMenuHandler mainMenuHandler) {
        this.dogsMenuHandler = dogsMenuHandler;
        this.ownersDogsRepository = ownersDogsRepository;
        this.ownersDogsService = ownersDogsService;
        this.ownUpdatesHandler = ownUpdatesHandler;
        this.mainMenuHandler = mainMenuHandler;
    }

    /**
     * New user for the Dog's shelter registration - put in the database
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
    public OwnerDogDto checkForOwnerExist(Update update) {

        Long chatId = ownUpdatesHandler.getChatId(update);
        Optional<OwnerDog> ownerDog = ownersDogsRepository.findByChatId(chatId);

        if (ownerDog.isPresent()) {
            OwnerDog owner = ownerDog.get();
            return OwnerDogMapper.toDto(owner);
        } else {
            InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForTakeMenuButton();
            mainMenuHandler.noOwnOfPetRegisteredMessage(chatId, inlineKeyboardMarkup);
            return null;
        }
    }

    /**
     * Method checks if Owner has a Dog(s)
     */
    @Override
    public List<DogDto> checkForOwnerHasDog(OwnerDogDto ownerDto) {

        Long chatId = ownerDto.getChatId();

        if (!ownerDto.getDogsDto().isEmpty()) {
            return ownerDto.getDogsDto();
        } else {
            InlineKeyboardMarkup inlineKeyboardMarkup = dogsMenuHandler.formInlineKeyboardForTakeMenuButton();
            mainMenuHandler.noPetMessage(chatId, inlineKeyboardMarkup);
            return null;
        }
    }

    @Override
    public OwnerDogDto returnOwnerDogDtoFromUpdate(Update update) {
        Long ownerChatId = ownUpdatesHandler.getChatId(update);
        Optional<OwnerDog> ownerDog = ownersDogsRepository.findByIsVolunteerIsFalseAndChatId(ownerChatId);

        if (ownerDog.isPresent()) {
            OwnerDog owner = ownerDog.get();
            return OwnerDogMapper.toDto(owner);
        } else {
            return null;
        }
    }

    @Override
    public OwnerDogDto returnVolunteerDogDtoFromUpdate(Update update) {
        Long volunteerChatId = ownUpdatesHandler.getChatId(update);;
        Optional<OwnerDog> volunteerDog = ownersDogsRepository.findByIsVolunteerIsTrueAndChatId(volunteerChatId);

        if (volunteerDog.isPresent()) {
            OwnerDog volunteer = volunteerDog.get();
            return OwnerDogMapper.toDto(volunteer);
        } else {
            return null;
        }
    }

    @Override
    public OwnerDogDto returnOwnerOrVolunteerDogDtoByChatId(Long chatId) {
        Optional<OwnerDog> ownerOptional = ownersDogsRepository.findByChatId(chatId);
        if (ownerOptional.isPresent()) {
            OwnerDog owner = ownerOptional.get();
            return OwnerDogMapper.toDto(owner);
        } else {
            return null;
        }
    }
}
