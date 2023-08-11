package omg.group.priuttelegrambot.handlers.owners.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import omg.group.priuttelegrambot.entity.chats.ChatCats;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.handlers.owners.OwnersCatsHandler;
import omg.group.priuttelegrambot.handlers.pets.CatsHandler;
import omg.group.priuttelegrambot.repository.chats.ChatsCatsRepository;
import omg.group.priuttelegrambot.repository.owners.OwnersCatsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OwnersCatsHandlerImpl implements OwnersCatsHandler {
    private final TelegramBot telegramBot;
    private final OwnersCatsRepository ownersCatsRepository;
    private final ChatsCatsRepository chatsCatsRepository;
    private final CatsHandler catsHandler;

    public OwnersCatsHandlerImpl(TelegramBot telegramBot,
                                 OwnersCatsRepository ownersCatsRepository,
                                 ChatsCatsRepository chatsCatsRepository,
                                 CatsHandler catsHandler) {
        this.telegramBot = telegramBot;
        this.ownersCatsRepository = ownersCatsRepository;
        this.chatsCatsRepository = chatsCatsRepository;
        this.catsHandler = catsHandler;
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
            ownerChatId = update.callbackQuery().message().chat().id();
        } else if (update.message() != null) {
            ownerChatId = update.message().chat().id();
        }

        Optional<OwnerCat> owner = ownersCatsRepository.findByChatId(ownerChatId);

        if (owner.isPresent()) {
            // If owner exist in clients_cats database - immediately call a volunteer

            startingChat(owner.get());

        } else {
            // If owner doesn't exist in clients_cats database than register him as new user and send a call to volunteer

            catsHandler.newOwnerRegister(update);

            Optional<OwnerCat> ownerCatNew = ownersCatsRepository.findByChatId(ownerChatId);

            if (ownerCatNew.isPresent()) {

                startingChat(ownerCatNew.get());
            }
        }
    }

    // Method sends inquiry to volunteer for starting a chat
    @Override
    public void startingChat(OwnerCat owner) {

        Long ownerChatId = owner.getChatId();
        Long ownerId = owner.getTelegramUserId();

        Optional<ChatCats> chatOptional = chatsCatsRepository.findByOwnerCatId(ownerId);

        if (chatOptional.isPresent()) {
            //  If owner already has a chat send him a message about it

            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

            inlineKeyboardMarkup.addRow(
                    new InlineKeyboardButton("Завершить").callbackData("/cats_close"));

            SendMessage messageToOwner = new SendMessage(ownerChatId, """
                    Вы уже ведете чат с волонтером.
                    Введите свой вопрос. Для
                    прекращения нажмите *Завершить*
                    или введите команду */cats_close*
                    """)
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup);

            telegramBot.execute(messageToOwner);

        } else {
            // If owner hasn't a chat searches for a free volunteer

            Optional<OwnerCat> volunteerOptional = ownersCatsRepository
                    .findVolunteerByVolunteerIsTrueAndNoChatsOpened();

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

                OwnerCat volunteer = volunteerOptional.get();

                Long volunteerChatId = volunteer.getChatId();

                volunteer.setVolunteerChatOpened(true);

                ownersCatsRepository.save(volunteer);

                // Set new chat in chat_cats database

                ChatCats chat = new ChatCats();

                chat.setIsChatting(false); // Till volunteer haven't pushed button /reply chat is not started
                chat.setOwnerCat(owner);
                chat.setVolunteerCat(volunteer);
                chat.setCreatedAt(LocalDateTime.now());

                chatsCatsRepository.save(chat);

                // Send message to volunteer

                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

                inlineKeyboardMarkup.addRow(
                        new InlineKeyboardButton("Ответить").callbackData("/cats_reply"),
                        new InlineKeyboardButton("Завершить").callbackData("/cats_close"));

                SendMessage messageToVolunteer = new SendMessage(volunteerChatId, """
                        Клиенту требуется консультация.
                        Пожалуйста, свяжитесь с ним в
                        ближайшее время, нажав *"Ответить"*.
                        По завершению чата нажмите *Завершить*
                        или введите команду */cats_close*
                        """)
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(inlineKeyboardMarkup);

                telegramBot.execute(messageToVolunteer);

                // Send message to owner

                SendMessage messageToOwner = new SendMessage(ownerChatId, """
                        Волонтеру направлен запрос на чат
                        с вами. Пожалуйста, дождитесь
                        ответа волонтера, либо введите
                        команду */cats_close* для завершения.
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

        Long volunteerChatId = update.callbackQuery().message().chat().id();
        Long volunteerTelegramUserId = update.callbackQuery().message().from().id();

        Optional<ChatCats> chatOptional = chatsCatsRepository.findByVolunteerCatId(volunteerChatId);

        if (chatOptional.isPresent()) {

            ChatCats chat = chatOptional.get();

            Long ownerTelegramUserId = chatOptional.get().getOwnerCat().getTelegramUserId();
            Long ownerChatId = chatOptional.get().getOwnerCat().getChatId();

            SendMessage groupMessage = new SendMessage(ownerTelegramUserId, "/newgroup@priut_OMG_bot " + volunteerTelegramUserId);
            SendResponse sendResponse = telegramBot.execute(groupMessage);

            Message groupChat = sendResponse.message();

            if (groupChat != null && groupChat.chat() != null) {

                Long chatId = groupChat.chat().id();

                chat.setIsChatting(true);
                chat.setChatId(chatId);

                chatsCatsRepository.save(chat);

                SendMessage messageToOwner = new SendMessage(ownerChatId, """
                        Волонтер подтвердил готовность
                        проконсультровать Вас. Для
                        этого был создан новый чат.
                        Напишите ему свой вопрос.
                        Для завершения отправьте команду
                        */cats_close* в созданном чате.
                        """)
                        .parseMode(ParseMode.Markdown);

                telegramBot.execute(messageToOwner);

                SendMessage sendMessage = new SendMessage(volunteerChatId, """
                        Вы подтвердили чат с клиентом.
                        Для этого был создан новый чат.
                        Дождитесь вопроса от него.
                        Для завершения отправьте команду
                        */cats_close* в созданном чате.
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

        Long ownerOrVolunteerId = update.message().from().id();

        Optional<ChatCats> chatByOwnerOptional = chatsCatsRepository.findByOwnerCatId(ownerOrVolunteerId);

        if (chatByOwnerOptional.isEmpty()) {

            Optional<ChatCats> chatByVolunteerOptional = chatsCatsRepository
                    .findByVolunteerCatId(ownerOrVolunteerId);

            if (chatByVolunteerOptional.isPresent()) {

                ChatCats chat = chatByVolunteerOptional.get();
                Long ownerChatId = chat.getOwnerCat().getTelegramUserId();
                String userName = chat.getOwnerCat().getUserName();
                Long volunteerChatId = chat.getVolunteerCat().getTelegramUserId();
                Long chatId = chat.getChatId();

                chatsCatsRepository.delete(chat);


                Optional<OwnerCat> volunteerOptional = ownersCatsRepository
                        .findByVolunteerIsTrueAndChatId(volunteerChatId);

                if (volunteerOptional.isPresent()) {

                    OwnerCat volunteer = volunteerOptional.get();
                    volunteer.setVolunteerChatOpened(false);
                    ownersCatsRepository.save(volunteer);

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

            ChatCats chat = chatByOwnerOptional.get();
            Long ownerChatId = chat.getOwnerCat().getTelegramUserId();
            String userName = chat.getOwnerCat().getUserName();
            Long volunteerChatId = chat.getVolunteerCat().getTelegramUserId();
            Long chatId = chat.getChatId();

            chatsCatsRepository.delete(chat);

            Optional<OwnerCat> volunteerOptional = ownersCatsRepository
                    .findByVolunteerIsTrueAndChatId(volunteerChatId);

            if (volunteerOptional.isPresent()) {

                OwnerCat volunteer = volunteerOptional.get();
                volunteer.setVolunteerChatOpened(false);
                ownersCatsRepository.save(volunteer);

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

        Long ownerOrVolunteerId = update.message().from().id();

        Optional<ChatCats> chatByOwnerOptional = chatsCatsRepository.findByOwnerCatId(ownerOrVolunteerId);

        if (chatByOwnerOptional.isEmpty()) {

            Optional<ChatCats> chatByVolunteerOptional = chatsCatsRepository
                    .findByVolunteerCatId(ownerOrVolunteerId);

            if (chatByVolunteerOptional.isPresent()) {

                String text = update.message().text();

                ChatCats chat = chatByVolunteerOptional.get();
                Long chatId = chat.getChatId();

                SendMessage message = new SendMessage(chatId, text);

                telegramBot.execute(message);
            }
        } else {

            String text = update.message().text();

            ChatCats chat = chatByOwnerOptional.get();
            Long chatId = chat.getChatId();

            SendMessage message = new SendMessage(chatId, text);

            telegramBot.execute(message);
        }
    }

}

