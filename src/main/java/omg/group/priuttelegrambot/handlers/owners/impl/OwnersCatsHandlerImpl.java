package omg.group.priuttelegrambot.handlers.owners.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.entity.chats.ChatCats;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.entity.pets.Cat;
import omg.group.priuttelegrambot.handlers.owners.OwnersCatsHandler;
import omg.group.priuttelegrambot.handlers.pets.CatsHandler;
import omg.group.priuttelegrambot.repository.chats.ChatsCatsRepository;
import omg.group.priuttelegrambot.repository.owners.OwnersCatsRepository;
import omg.group.priuttelegrambot.service.OwnersCatsService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OwnersCatsHandlerImpl implements OwnersCatsHandler {
    private final TelegramBot telegramBot;
    private final OwnersCatsService ownersCatsService;
    private final OwnersCatsRepository ownersCatsRepository;
    private final ChatsCatsRepository chatsCatsRepository;
    private final CatsHandler catsHandler;

    public OwnersCatsHandlerImpl(TelegramBot telegramBot,
                                 OwnersCatsService ownersCatsService,
                                 OwnersCatsRepository ownersCatsRepository,
                                 ChatsCatsRepository chatsCatsRepository,
                                 CatsHandler catsHandler) {
        this.telegramBot = telegramBot;
        this.ownersCatsService = ownersCatsService;
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
            ownerChatId = update.callbackQuery().from().id();
        } else if (update.message() != null) {
            ownerChatId = update.message().from().id();
        }

        Optional<OwnerCat> ownerCat = ownersCatsRepository.findByChatId(ownerChatId);

        if (ownerCat.isPresent()) {

            startingChat(ownerCat.get());

        } else {

            catsHandler.newOwnerRegister(update);

            Optional<OwnerCat> ownerCatNew = ownersCatsRepository.findByChatId(ownerChatId);

            startingChat(ownerCatNew.get());
        }
    }


    @Override
    public void startingChat(OwnerCat ownerCat) {

        if (ownerCat != null) {

            Long ownerId = ownerCat.getId();
            Long ownerChatId = ownerCat.getChatId();

            Optional<ChatCats> chatCats = chatsCatsRepository.findByOwnerCatId(ownerId);

            if (chatCats.isPresent()) {

                SendMessage sendMessage = new SendMessage(ownerChatId, """
                        У вас уже открыт чат с волонтером.
                        Пожалуйста, дождитесь ответа, либо
                        напишите свой вопрос.
                        Чтобы закрыть чат введите команду
                        /close
                        """)
                        .parseMode(ParseMode.Markdown);

                telegramBot.execute(sendMessage);

            } else {
                OwnerCatDto volunteerDto = ownersCatsService.findCatsVolunteer();
                Long volunteerChatId = volunteerDto.getChatId();

                OwnerCat volunteer = ownersCatsService.constructOwner(volunteerDto);

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

                ChatCats chat = new ChatCats();

                chat.setIsChatting(false); // Till volunteer haven't pushed button /reply chat is not started
                chat.setOwnerCat(ownerCat);
                chat.setVolunteerCat(volunteer);

                chatsCatsRepository.save(chat);
            }
        }
    }


    /**
     * Method starts a chat when volunteer clicks on button "Ответить" - "/cats_reply" command
     */
    @Override
    public void executeReplyButtonCommandForVolunteer(Update update) {

        OwnerCat volunteer = returnVolunteerFromUpdate(update);

        if (volunteer != null) {

            int chatsOpened = volunteer.getChatsOpened();
            chatsOpened = chatsOpened + 1;
            volunteer.setChatsOpened(chatsOpened);

            ownersCatsRepository.save(volunteer); // Increase chats where volunteer is answers

            Long volunteerId = volunteer.getId();

            List<ChatCats> chats = chatsCatsRepository.findByIsChattingFalseAndVolunteerCatId(volunteerId);

            if (!chats.isEmpty()) {
                if (chats.size() == 1) {

                    Long ownerChatId = chats.get(0).getOwnerCat().getChatId();

                    Long volunteerChatId = chats.get(0).getVolunteerCat().getChatId();

                    ChatCats chat = chats.get(0);

                    chat.setIsChatting(true);

                    chatsCatsRepository.save(chat);

                    SendMessage messageToOwner = new SendMessage(ownerChatId, """
                            Волонтер подтвердил готовность
                            проконсультровать Вас. Введите
                            сообщение и нажмите кнопку отправить.
                            Для завершения консультации отправьте
                            команду /close
                            """)
                            .parseMode(ParseMode.Markdown);

                    telegramBot.execute(messageToOwner);

                    SendMessage sendMessage = new SendMessage(volunteerChatId, """
                            Вы подтвердили чат с клиентом.
                            Дождитесь ответа от него. Для
                            завершения консультации введите
                            команду /close
                            """)
                            .parseMode(ParseMode.Markdown);

                    telegramBot.execute(sendMessage);

                } else {

                    Long ownerId;
                    String userName;

                    StringBuilder stringBuilder = new StringBuilder();

                    String startMessage = """
                            У Вас несколько запросов
                            от клиентов на консультацию:
                            """;

                    for (ChatCats chat : chats) {

                        ownerId = chat.getOwnerCat().getId();
                        userName = chat.getOwnerCat().getUserName();

                        String string = String.format("""
                                Клиент # - %d, имя пользователя - %s
                                """, ownerId, userName);

                        stringBuilder.append(string);
                    }

                    Long volunteerChatId = chats.get(0).getVolunteerCat().getChatId();

                    SendMessage messageToVolunteer = new SendMessage(volunteerChatId,
                            startMessage + "\n " + stringBuilder +
                                    "\n Отправьте номер клиента или его " +
                                    "имя пользователя для старта чата");

                    telegramBot.execute(messageToVolunteer);
                }
            }
        }
    }

    @Override
    public OwnerCat returnOwnerFromUpdate(@NotNull Update update) {

        Long ownerCatChatId;

        if (update.callbackQuery() != null) {
            ownerCatChatId = update.callbackQuery().from().id();
        } else {
            ownerCatChatId = update.message().from().id();
        }

        Optional<OwnerCat> ownerCat = ownersCatsRepository.findByChatId(ownerCatChatId);

        return ownerCat.orElse(null);
    }

    @Override
    public OwnerCat returnVolunteerFromUpdate(@NotNull Update update) {

        Long volunteerChatId;

        if (update.callbackQuery() != null) {
            volunteerChatId = update.callbackQuery().from().id();
        } else {
            volunteerChatId = update.message().from().id();
        }

        Optional<OwnerCat> volunteerOptional = ownersCatsRepository.findByVolunteerIsTrueAndChatId(volunteerChatId);

        if (volunteerOptional.isPresent()) {

            return volunteerOptional.get().getVolunteer();
        }
        return null;
    }

    /**
     * Method closes a chat when volunteer or client sends - "/close" command
     */
    @Override
    public void executeCloseButtonCommand(Update update) {

        OwnerCat ownerCat = returnOwnerFromUpdate(update);

        if (ownerCat != null) {

            Long ownerId = ownerCat.getId();

            Optional<ChatCats> chatOptional = chatsCatsRepository.findByIsChattingTrueAndOwnerCatId(ownerId);

            if (chatOptional.isPresent()) {

                Long ownerChatId = chatOptional.get().getOwnerCat().getChatId();

                String userName = chatOptional.get().getOwnerCat().getUserName();

                Long volunteerChatId = chatOptional.get().getVolunteerCat().getChatId();

                chatsCatsRepository.delete(chatOptional.get());

                OwnerCat volunteer = returnVolunteerFromUpdate(update);

                if (volunteer != null) {

                    int chatsOpened = volunteer.getChatsOpened();
                    chatsOpened = chatsOpened - 1;
                    volunteer.setChatsOpened(chatsOpened);

                    ownersCatsRepository.save(volunteer); // Decrease chats where volunteer is answers

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
            } else {

                OwnerCat volunteer = returnVolunteerFromUpdate(update);

                if (volunteer != null) {

                    Long volunteerId = volunteer.getId();

                    List<ChatCats> chats = chatsCatsRepository.findByIsChattingTrueAndVolunteerCatId(volunteerId);

                    if (!chats.isEmpty()) {

                        if (chats.size() == 1) {

                            Long ownerChatId = chats.get(0).getOwnerCat().getChatId();

                            Long volunteerChatId = chats.get(0).getVolunteerCat().getChatId();

                            ChatCats chat = chats.get(0);

                            chatsCatsRepository.delete(chat);

                            SendMessage messageToOwner = new SendMessage(ownerChatId, """
                                    Волонтер закрыл чат с вами.
                                    Для новой консультации нажмите
                                    кнопку *Позвать волонтера*
                                    """)
                                    .parseMode(ParseMode.Markdown);

                            telegramBot.execute(messageToOwner);

                            String userName = chat.getOwnerCat().getUserName();

                            SendMessage sendMessage = new SendMessage(volunteerChatId, String.format("""
                                    Вы закрыли чат со следующим
                                    клиентом: %s
                                    """, userName))
                                    .parseMode(ParseMode.Markdown);

                            telegramBot.execute(sendMessage);

                        } else {

                            String userName;

                            StringBuilder stringBuilder = new StringBuilder();

                            String startMessage = """
                                    У Вас несколько чатов в работе.
                                    Укажите номер или имя пользователя,
                                    с которым хотите завершить чат.
                                    """;

                            for (ChatCats chat : chats) {

                                ownerId = chat.getOwnerCat().getId();
                                userName = chat.getOwnerCat().getUserName();
                                String text = chat.getMessageText();

                                String string = String.format("""
                                        Клиент # - %d, имя пользователя - %s, последнее сообщение:
                                        %s
                                                                                
                                        """, ownerId, userName, text);

                                stringBuilder.append(string);
                            }

                            Long volunteerChatId = volunteer.getChatId();

                            SendMessage messageToVolunteer = new SendMessage(volunteerChatId,
                                    startMessage + "\n " + stringBuilder +
                                            "\n Отправьте номер клиента или его " +
                                            "имя пользователя для старта чата");

                            telegramBot.execute(messageToVolunteer);

                        }
                    }
                }
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
            String text = update.message().text();

            OwnerCat ownerOrVolunteer = returnOwnerFromUpdate(update);

            Long ownerOrVolunteerId = ownerOrVolunteer.getId();

            if (chatsCatsRepository.findByIsChattingTrueAndOwnerCatId(ownerOrVolunteerId).isPresent()) {

                Optional<ChatCats> chat = chatsCatsRepository.findByIsChattingTrueAndOwnerCatId(ownerOrVolunteerId);

                if (chat.isPresent()) {

                    chat.get().setLastMessageId(messageId);
                    chat.get().setMessageText(text);

                    chatsCatsRepository.save(chat.get());

                    Long volunteerChatId = chat.get().getVolunteerCat().getChatId();

                    SendMessage messageToVolunteer = new SendMessage(volunteerChatId, text);

                    telegramBot.execute(messageToVolunteer);
                }

            } else if (!chatsCatsRepository.findByVolunteerCatId(ownerOrVolunteerId).isEmpty()) {

                List<ChatCats> chats = chatsCatsRepository.findByVolunteerCatId(ownerOrVolunteerId);

                if (chats.size() == 1) {

                    Long ownerChatId = chats.get(0).getOwnerCat().getChatId();

                    int lastMessageId = chats.get(0).getLastMessageId();

                    SendMessage messageToOwner = new SendMessage(ownerChatId, text)
                            .replyToMessageId(lastMessageId);

                    telegramBot.execute(messageToOwner);

                } else {

                    StringBuilder stringBuilder = new StringBuilder();

                    String startMessage = """
                            У Вас несколько чатов в работе.
                            Укажите номер или имя пользователя,
                            которому отвечаете.
                             """;

                    for (ChatCats chat : chats) {

                        Long ownerId = chat.getOwnerCat().getId();
                        String userName = chat.getOwnerCat().getUserName();
                        String savedText = chat.getMessageText();

                        String string = String.format("""
                                Клиент # - %d, имя пользователя - %s, последнее сообщение от клиента:
                                %s
                                                                        
                                """, ownerId, userName, savedText);

                        stringBuilder.append(string);
                    }

                    Long volunteerChatId = chats.get(0).getVolunteerCat().getChatId();

                    SendMessage messageToVolunteer = new SendMessage(volunteerChatId,
                            startMessage + "\n " +
                                    stringBuilder + "\n" + """
                                    Для отправки сообщения отправьте
                                    номер клиента или имя пользователя.
                                    Номер или имя начните со знака *
                                    и без пробела номер, либо имя.
                                    """);

                    telegramBot.execute(messageToVolunteer);
                }
            }
        }
    }

    @Override
    public ChatCats returnOneChatToAnswer(Update update) {


    }

}
