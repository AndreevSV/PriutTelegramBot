package omg.group.priuttelegrambot.handlers.owners.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import omg.group.priuttelegrambot.entity.chats.ChatDogs;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.handlers.owners.OwnersDogsHandler;
import omg.group.priuttelegrambot.handlers.pets.DogsHandler;
import omg.group.priuttelegrambot.repository.chats.ChatsDogsRepository;
import omg.group.priuttelegrambot.repository.owners.OwnersDogsRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OwnersDogsHandlerImpl implements OwnersDogsHandler {

    private final TelegramBot telegramBot;
    private final OwnersDogsRepository ownersDogsRepository;
    private final ChatsDogsRepository chatsDogsRepository;
    private final DogsHandler dogsHandler;


    public OwnersDogsHandlerImpl(TelegramBot telegramBot,
                                 OwnersDogsRepository ownersDogsRepository,
                                 ChatsDogsRepository chatsDogsRepository,
                                 DogsHandler dogsHandler) {
        this.telegramBot = telegramBot;
        this.ownersDogsRepository = ownersDogsRepository;
        this.chatsDogsRepository = chatsDogsRepository;
        this.dogsHandler = dogsHandler;
    }

    /**
     * When client push button "Позвать волонтера" method sends request to the volunteer to start a chat.
     * Volunteer answers to client clicking on button "Ответить"
     * If chat is already started client receives a message that chat is already started
     * To not duplicate code method works with startingChat method below
     */
    @Override
    public void callVolunteer(Update update) {

        Long ownerChatId = 0L;

        if (update.callbackQuery() != null) {
            ownerChatId = update.callbackQuery().from().id();
        } else if (update.message() != null) {
            ownerChatId = update.message().from().id();
        }

        Optional<OwnerDog> owner = ownersDogsRepository.findByChatId(ownerChatId);

        if (owner.isPresent()) {
            // If owner exist in clients_cats database - immediately call a volunteer

            startingChat(owner.get());

        } else {
            // If owner doesn't exist in clients_cats database than register him as new user and send a call to volunteer

            dogsHandler.newOwnerRegister(update);

            Optional<OwnerDog> ownerDogNew = ownersDogsRepository.findByChatId(ownerChatId);

            if (ownerDogNew.isPresent()) {

                startingChat(ownerDogNew.get());
            }

        }
    }

    // Method sends inquiry to volunteer for starting a chat
    @Override
    public void startingChat(OwnerDog owner) {

        Long ownerChatId = owner.getChatId();
        Long ownerId = owner.getId();

        Optional<ChatDogs> chatByOwner = chatsDogsRepository.findByOwnerDogId(ownerId);

        if (chatByOwner.isPresent()) {
            //  If owner already has a chat send him a message about it

            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

            inlineKeyboardMarkup.addRow(
                    new InlineKeyboardButton("Завершить").callbackData("/cats_close"));

            SendMessage messageToOwner = new SendMessage(ownerChatId, """
                    Вы уже ведете чат с волонтером.
                    Введите свой вопрос. Для
                    прекращения нажмите *Завершить*
                    или введите команду */close*
                    """)
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup);

            telegramBot.execute(messageToOwner);

        } else {
            // If owner hasn't a chat searches for a free volunteer

            Optional<OwnerDog> volunteerOptional = ownersDogsRepository.findVolunteerByVolunteerIsTrueAndNoChatsOpened();

            if (volunteerOptional.isEmpty()) {

                // If no free volunteer exist send to owner a message about it

                SendMessage messageToOwner = new SendMessage(ownerChatId, """
                        К сожалению в настоящее время
                        нет свободных волонтеров, которые
                        могут вас проконсультировать.
                        Пожалуйста, обратитесь позднее.
                        """)
                        .parseMode(ParseMode.Markdown);

                telegramBot.execute(messageToOwner);

            } else {

                // If free volunteer exist set a chat with him

                Long volunteerChatId = volunteerOptional.get().getChatId();

//                volunteerOptional.get().setChatsOpened(1);

                ownersDogsRepository.save(volunteerOptional.get());

                // Set new chat in chat_cats database

                ChatDogs chat = new ChatDogs();

                chat.setIsChatting(false); // Till volunteer haven't pushed button /reply chat is not started
                chat.setOwnerDog(owner);
                chat.setVolunteerDog(volunteerOptional.get());
                chat.setCreatedAt(LocalDateTime.now());

                chatsDogsRepository.save(chat);

                // Send message to volunteer

                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

                inlineKeyboardMarkup.addRow(
                        new InlineKeyboardButton("Ответить").callbackData("/dogs_reply"),
                        new InlineKeyboardButton("Завершить").callbackData("/dogs_close"));

                SendMessage messageToVolunteer = new SendMessage(volunteerChatId, """
                        Клиенту требуется консультация.
                        Пожалуйста, свяжитесь с ним в
                        ближайшее время, нажав *"Ответить"*.
                        По завершению чата нажмите *Завершить*
                        или введите команду */close*
                        """)
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(inlineKeyboardMarkup);

                telegramBot.execute(messageToVolunteer);

                // Send message to owner

                SendMessage messageToOwner = new SendMessage(ownerChatId, """
                        Волонтеру направлен запрос на чат
                        с вами. Пожалуйста, дождитесь
                        ответа волонтера, либо введите
                        команду */close* для завершения.
                        """)
                        .parseMode(ParseMode.Markdown);

                telegramBot.execute(messageToOwner);

            }
        }
    }

    /**
     * Method starts a chat when volunteer pushes on button "Ответить" - "/cats_reply" command
     */
    @Override
    public void executeReplyButtonCommandForVolunteer(Update update) {

        OwnerDog volunteer = returnVolunteerFromUpdate(update);

        if (volunteer != null) {

            Long volunteerId = volunteer.getId();

            Optional<ChatDogs> chatByVolunteer = chatsDogsRepository.findByVolunteerDogId(volunteerId);

            if (chatByVolunteer.isPresent()) {

                chatByVolunteer.get().setIsChatting(true);

                chatsDogsRepository.save(chatByVolunteer.get());

                Long ownerChatId = chatByVolunteer.get().getOwnerDog().getChatId();

                Long volunteerChatId = chatByVolunteer.get().getVolunteerDog().getChatId();

                SendMessage messageToOwner = new SendMessage(ownerChatId, """
                        Волонтер подтвердил готовность
                        проконсультровать Вас. Отправьте
                        ему свой вопрос. Для завершения
                        консультации отправьте
                        команду */close*
                        """)
                        .parseMode(ParseMode.Markdown);

                telegramBot.execute(messageToOwner);

                SendMessage sendMessage = new SendMessage(volunteerChatId, """
                        Вы подтвердили чат с клиентом.
                        Дождитесь вопроса от него. Для
                        завершения консультации введите
                        команду */close*
                        """)
                        .parseMode(ParseMode.Markdown);

                telegramBot.execute(sendMessage);

            }
        }
    }

    /**
     * Method closes a chat when volunteer or client sends - "/close" command
     */
    @Override
    public void executeCloseButtonCommand(Update update) {

        OwnerDog owner = returnOwnerFromUpdate(update);

        if (owner == null) {
            // If volunteer pushed close button
            OwnerDog volunteer = returnVolunteerFromUpdate(update);

            if (volunteer != null) {

                Long volunteerId = volunteer.getId();
                Long volunteerChatId = volunteer.getChatId();

                Optional<ChatDogs> chatOptional = chatsDogsRepository.findByVolunteerDogId(volunteerId);

                if (chatOptional.isPresent()) {

                    String userName = chatOptional.get().getOwnerDog().getUserName();
                    Long ownerChatId = chatOptional.get().getOwnerDog().getChatId();

                    chatsDogsRepository.delete(chatOptional.get());

//                    volunteer.setChatsOpened(0);

                    ownersDogsRepository.save(volunteer);

                    SendMessage sendMessage = new SendMessage(volunteerChatId, String.format("""
                            Вы закрыли чат со следующим
                            клиентом: %s
                            """, userName))
                            .parseMode(ParseMode.Markdown);

                    telegramBot.execute(sendMessage);


                    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Вставить клавиатуру!!!!!!!!!!!!!!!!!!!!!!!!!)

                    SendMessage messageToOwner = new SendMessage(ownerChatId, """
                            Волонтер закрыл чат с вами.
                            Для новой консультации нажмите
                            кнопку *Позвать волонтера*
                            """)
                            .parseMode(ParseMode.Markdown);

                    telegramBot.execute(messageToOwner);
                }
            }
        } else {
            // If owner pushed "close" button

            Long ownerId = owner.getId();

            Optional<ChatDogs> chatOptional = chatsDogsRepository.findByOwnerDogId(ownerId);

            if (chatOptional.isPresent()) {

                Long ownerChatId = chatOptional.get().getOwnerDog().getChatId();
                String userName = chatOptional.get().getOwnerDog().getUserName();

                Long volunteerChatId = chatOptional.get().getVolunteerDog().getChatId();
                OwnerDog volunteer = chatOptional.get().getVolunteerDog();

                chatsDogsRepository.delete(chatOptional.get());

//                volunteer.setChatsOpened(0);

                ownersDogsRepository.save(volunteer);

                SendMessage messageToOwner = new SendMessage(ownerChatId, """
                        Вы завершили чат с волонтером.
                        """)
                        .parseMode(ParseMode.Markdown);

                telegramBot.execute(messageToOwner);

                SendMessage messageToVolunteer = new SendMessage(volunteerChatId, String.format("""
                        Пользователь %s завершил чат с вами.
                        Диалог закрыт и удален из базы.
                        """, userName))
                        .parseMode(ParseMode.Markdown);

                telegramBot.execute(messageToVolunteer);
            }
        }
    }

    /**
     * Method that receives message and sand it to client or volunteer
     */
    @Override
    public void sendMessageReceived(Update update) {

        if (update.message().text() != null) {

            Integer messageId = update.message().messageId();
            String messageText = update.message().text();

            OwnerDog owner = returnOwnerFromUpdate(update);

            if (owner == null) {

                OwnerDog volunteer = returnVolunteerFromUpdate(update);
                Long volunteerId = volunteer.getId();

                Optional<ChatDogs> chatOptional = chatsDogsRepository.findByVolunteerDogId(volunteerId);

                if (chatOptional.isPresent()) {

//                    chatOptional.get().setAnswerText(messageText);
//                    chatOptional.get().setLastMessageId(messageId);
                    chatOptional.get().setAnswerSentTime(LocalDateTime.now());

                    chatsDogsRepository.save(chatOptional.get());

                    Long ownerChatId = chatOptional.get().getOwnerDog().getChatId();

                    SendMessage messageToOwner = new SendMessage(ownerChatId, messageText);

                    telegramBot.execute(messageToOwner);

                }
            } else {

                Long ownerId = owner.getId();

                Optional<ChatDogs> chatOptional = chatsDogsRepository.findByOwnerDogId(ownerId);

                if (chatOptional.isPresent()) {

//                    chatOptional.get().setMessageText(messageText);
//                    chatOptional.get().setLastMessageId(messageId);
                    chatOptional.get().setMessageSentTime(LocalDateTime.now());

                    chatsDogsRepository.save(chatOptional.get());

                    Long volunteerChatId = chatOptional.get().getVolunteerDog().getChatId();

                    SendMessage messageToVolunteer = new SendMessage(volunteerChatId, messageText);

                    telegramBot.execute(messageToVolunteer);

                }
            }
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