package omg.group.priuttelegrambot.handlers.owners.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;
import omg.group.priuttelegrambot.entity.chats.ChatDogs;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.handlers.owners.OwnersDogsHandler;
import omg.group.priuttelegrambot.handlers.pets.DogsHandler;
import omg.group.priuttelegrambot.repository.chats.ChatsDogsRepository;
import omg.group.priuttelegrambot.repository.owners.OwnersDogsRepository;
import omg.group.priuttelegrambot.service.OwnersDogsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OwnersDogsHandlerImpl implements OwnersDogsHandler {

    private final TelegramBot telegramBot;
    private final OwnersDogsService ownersDogsService;
    private final OwnersDogsRepository ownersDogsRepository;
    private final ChatsDogsRepository chatsDogsRepository;
    private final DogsHandler dogsHandler;


    public OwnersDogsHandlerImpl(TelegramBot telegramBot,
                                 OwnersDogsService ownersDogsService,
                                 OwnersDogsRepository ownersDogsRepository,
                                 ChatsDogsRepository chatsDogsRepository,
                                 DogsHandler dogsHandler) {
        this.telegramBot = telegramBot;
        this.ownersDogsService = ownersDogsService;
        this.ownersDogsRepository = ownersDogsRepository;
        this.chatsDogsRepository = chatsDogsRepository;
        this.dogsHandler = dogsHandler;
    }

    /**
     * Method sends request to the volunteer to set a chat.
     * Volunteer answers to user clicking on button "Ответить"
     */
    @Override
    public void callVolunteer(Update update) {

        Long ownerChatId = 0L;

        if (update.callbackQuery() != null) {
            ownerChatId = update.callbackQuery().from().id();
        } else if (update.message() != null) {
            ownerChatId = update.message().from().id();
        }

        Optional<OwnerDog> ownerDog = ownersDogsRepository.findByChatId(ownerChatId);

        if (ownerDog.isPresent()) {

            settingChat(ownerDog);

        } else {

            dogsHandler.newOwnerRegister(update);

            Optional<OwnerDog> ownerDogNew = ownersDogsRepository.findByChatId(ownerChatId);

            settingChat(ownerDogNew);
        }
    }

    @Override
    public void settingChat(Optional<OwnerDog> ownerDog) {

        if (ownerDog.isPresent()) {

            OwnerDogDto volunteerDto = ownersDogsService.findDogsVolunteer();
            Long volunteerChatId = volunteerDto.getChatId();

            OwnerDog volunteer = ownersDogsService.constructOwner(volunteerDto);

            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            inlineKeyboardMarkup.addRow(
                    new InlineKeyboardButton("Ответить").callbackData("/cats_reply"),
                    new InlineKeyboardButton("Завершить").callbackData("/cats_close"));

            SendMessage sendMessage = new SendMessage(volunteerChatId, """
                Клиенту требуется консультация.
                Пожалуйста, свяжитесь с ним в
                ближайшее время, нажав *"Ответить"*.
                По кончанию диалога нажмите *Завершить*
                """)
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup);

            telegramBot.execute(sendMessage);

            ChatDogs chat = new ChatDogs();

            chat.setIsChatting(true);
            chat.setOwnerDog(ownerDog.get());
            chat.setVolunteerDog(volunteer);

            chatsDogsRepository.save(chat);
        }

    }

    /**
     * Method sends request to the volunteer to set a chat.
     * Volunteer answers to user clicking on button "Ответить"
     */
    @Override
    public void executeReplyButtonCommandForVolunteer(Update update) {

        if (update.callbackQuery().data().equals("/cats_reply") ||
                update.message().text().equals("/cats_reply")) {

            Long volunteerDogId;

            if (update.callbackQuery() != null) {
                volunteerDogId = update.callbackQuery().from().id();
            } else {
                volunteerDogId = update.message().from().id();
            }

            Optional<OwnerDog> volunteer = ownersDogsRepository.findByVolunteerIsTrueAndChatId(volunteerDogId);

            if (volunteer.isPresent()) {

                int chatsOpened = volunteer.get().getChatsOpened();
                chatsOpened = chatsOpened + 1;
                volunteer.get().setChatsOpened(chatsOpened);

                ownersDogsRepository.save(volunteer.get());

                Optional<ChatDogs> chat = chatsDogsRepository.findByVolunteerDogId(volunteerDogId);

                if (chat.isPresent()) {
                    Long chatId = chat.get().getOwnerDog().getChatId();

                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                    inlineKeyboardMarkup.addRow(
                            new InlineKeyboardButton("Ответить").callbackData("/reply"),
                            new InlineKeyboardButton("Завершить").callbackData("/close"));

                    SendMessage sendMessage = new SendMessage(chatId, """
                            Волонтер подтвердил готовность
                            проконсультровать Вас. Введите
                            сообщение и нажмите кнопку отправить.
                            Для завершения консультации отправьте
                            команду /close" или нажмите "Завершить"
                            """)
                            .parseMode(ParseMode.Markdown)
                            .replyMarkup(inlineKeyboardMarkup);

                    telegramBot.execute(sendMessage);
                }
            }
        }
    }

    @Override
    public OwnerDog returnOwnerDogFromUpdate(Update update) {

        Long ownerDogChatId;

        if (update.callbackQuery() != null) {
            ownerDogChatId = update.callbackQuery().from().id();
        } else {
            ownerDogChatId = update.message().from().id();
        }

        Optional<OwnerDog> ownerDog = ownersDogsRepository.findByChatId(ownerDogChatId);

        return ownerDog.orElseGet(OwnerDog::new);
    }

    @Override
    public OwnerDog returnVolunteerDogFromUpdate(Update update) {

        Long volunteerDogChatId;

        if (update.callbackQuery() != null) {
            volunteerDogChatId = update.callbackQuery().from().id();
        } else {
            volunteerDogChatId = update.message().from().id();
        }

        Optional<OwnerDog> volunteerDog = ownersDogsRepository.findByChatId(volunteerDogChatId);

        if (volunteerDog.isPresent()) {

            return volunteerDog.get().getVolunteer();
        }
        return new OwnerDog();
    }


//    @Override
//    private void executeCloseButtonCommand(Update update, Long chatId) {
//        String text = update.message().text();
//        SendMessage sendMessage = new SendMessage(chatId, text);
//    }
//
//    private void startChatWithOwner(Update update) {
//        while (!update.callbackQuery().data().equals("/close") || !update.message().text().equals("/close")) {
//            String text = update.message().text();
//
//            OwnerCatDto catsVolunteer = ownersCatsService.findCatsVolunteer();
//
//            Long chatId = catsVolunteer.getChatId();
//
//            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//            inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Ответить").callbackData("/answer"));
//
//            SendMessage sendMessage = new SendMessage(chatId, """
//                    Клиенту требуется консультация.
//                    Пожалуйста, свяжитесь с ним в ближайшее время,
//                    нажав на кнопу *"Ответить"*.
//                    """)
//                    .parseMode(ParseMode.Markdown)
//                    .replyMarkup(inlineKeyboardMarkup);
//
//            telegramBot.execute(sendMessage);
//        }
//
//    }

}