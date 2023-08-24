package omg.group.priuttelegrambot.handlers.chats.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import omg.group.priuttelegrambot.entity.chats.ChatCats;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.handlers.chats.ChatsCatsHandler;
import omg.group.priuttelegrambot.handlers.menu.CatsMenuHandler;
import omg.group.priuttelegrambot.handlers.menu.MainMenuHandler;
import omg.group.priuttelegrambot.handlers.owners.OwnersCatsHandler;
import omg.group.priuttelegrambot.handlers.updates.OwnUpdatesHandler;
import omg.group.priuttelegrambot.repository.chats.ChatsCatsRepository;
import omg.group.priuttelegrambot.repository.owners.OwnersCatsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ChatsCatsHandlerImpl implements ChatsCatsHandler {
    private final TelegramBot telegramBot;
    private final OwnersCatsRepository ownersCatsRepository;
    private final OwnersCatsHandler ownersCatsHandler;
    private final ChatsCatsRepository chatsCatsRepository;
    private final OwnUpdatesHandler ownUpdatesHandler;
    private final MainMenuHandler mainMenuHandler;
    private final CatsMenuHandler catsMenuHandler;

    public ChatsCatsHandlerImpl(TelegramBot telegramBot,
                                OwnersCatsRepository ownersCatsRepository,
                                OwnersCatsHandler ownersCatsHandler,
                                ChatsCatsRepository chatsCatsRepository,
                                OwnUpdatesHandler ownUpdatesHandler, MainMenuHandler mainMenuHandler, CatsMenuHandler catsMenuHandler) {
        this.telegramBot = telegramBot;
        this.ownersCatsRepository = ownersCatsRepository;
        this.ownersCatsHandler = ownersCatsHandler;
        this.chatsCatsRepository = chatsCatsRepository;
        this.ownUpdatesHandler = ownUpdatesHandler;
        this.mainMenuHandler = mainMenuHandler;
        this.catsMenuHandler = catsMenuHandler;
    }

    /**
     * When client push button "Позвать волонтера" method sends request to the Volunteer to start a chat.
     * Volunteer answers to client clicking on button "Ответить"
     * If chat is already started client receives a message that chat is already started
     * To not duplicate code method works with startingChat method below
     */
    @Override
    public void callVolunteer(Update update) {

        OwnerCat owner = ownersCatsHandler.returnOwnerFromUpdate(update);

        Long ownerChatId = owner.getChatId();
        Optional<ChatCats> chatOptional = chatsCatsRepository.findByOwnerCatChatId(ownerChatId);

        if (chatOptional.isPresent()) {
            //  If owner already has a chat send him a message about it
            catsMenuHandler.chatAlreadySetToOwnerMessage(ownerChatId);
        } else {
            // If owner hasn't a chat searches for a free volunteer
            Optional<OwnerCat> volunteerOptional = ownersCatsRepository.findVolunteerByVolunteerIsTrueAndNoChatsOpened();

            if (volunteerOptional.isEmpty()) {
                // If no free volunteer exist send to owner a message about it
                mainMenuHandler.noFreeVolunteerAvailableMessage(ownerChatId);
            } else {
                // If free volunteer exist
                // 1. Set to Volunteer field volunteer_chat_opened in clients_cats database to true
                OwnerCat volunteer = volunteerOptional.get();
                Long volunteerChatId = volunteer.getChatId();
                volunteer.setVolunteerChatOpened(true);
                ownersCatsRepository.save(volunteer);

                // 2. Set to Owner a Volunteer in clients_cats_database
                owner.setVolunteer(volunteer);
                ownersCatsRepository.save(owner);

                // 3. Put new chat in chat_cats database with Owner and Volunteer
                ChatCats chat = new ChatCats();
                chat.setIsChatting(false); // Till volunteer haven't pushed button /reply chat is not started
                chat.setOwnerCat(owner);
                chat.setVolunteerCat(volunteer);
                chat.setCreatedAt(LocalDateTime.now());
                chatsCatsRepository.save(chat);

                // 4. Send messages to Volunteer and Owner that inquiry was sent
                catsMenuHandler.inquiryToVolunteerForChat(volunteerChatId, ownerChatId);
            }
        }
    }

    /**
     * Method starts a chat when volunteer pushes on button "Ответить" - "/cats_reply" command
     */
    @Override
    public void executeReplyButtonCommandForVolunteer(Update update) {

        Long volunteerChatId = ownUpdatesHandler.extractChatIdFromUpdate(update);

        Optional<ChatCats> chatOptional = chatsCatsRepository.findByVolunteerCatChatId(volunteerChatId);

        if (chatOptional.isPresent()) {
            ChatCats chat = chatOptional.get();
            Long ownerChatId = chat.getOwnerCat().getChatId();
            chat.setIsChatting(true);
            chat.setMessageSentTime(LocalDateTime.now());
            chatsCatsRepository.save(chat);

            mainMenuHandler.volunteerOpenedChatMessage(ownerChatId, volunteerChatId);
        }
    }

    /**
     * Method closes a chat when volunteer or client sent an update "/cats_close" command
     * Depending on who are sent an update: owner or volunteer ->
     *
     * @return volunteerChatId if owner sent an update or ownerChatId if volunteer sent an update
     * Uses to delete a flag.isChatting for the owner and for the volunteer
     */
    @Override
    public Long executeCloseButtonCommand(Update update) {

        OwnerCat owner = ownersCatsHandler.returnOwnerFromUpdate(update);

        if (owner != null) {
            // If "/cats_close" button was pushed by Owner
            // Get a volunteer from owner
            OwnerCat volunteer = owner.getVolunteer();
            Long volunteerChatId = volunteer.getChatId();
            // Set to volunteer field volunteer_chat_opened to false
            volunteer.setVolunteerChatOpened(false);
            volunteer.setUpdatedAt(LocalDateTime.now());
            ownersCatsRepository.save(volunteer);
            //Deleting volunteer out of Owner
            owner.setVolunteer(null);
            owner.setUpdatedAt(LocalDateTime.now());
            ownersCatsRepository.save(owner);
            //Deleting chat in chat_cats database
            Long ownerChatId = owner.getChatId();
            Optional<ChatCats> chatOptional = chatsCatsRepository.findByOwnerCatChatId(ownerChatId);
            if (chatOptional.isPresent()) {
                ChatCats chat = chatOptional.get();
                chatsCatsRepository.delete(chat);
                //Send message to Owner and Volunteer that chat was closed by Owner
                mainMenuHandler.ownerClosedChatMessage(ownerChatId, volunteerChatId);
            } else {
                System.out.println("Чат с таким OwnerChatId не найден");
            }
            return volunteerChatId;
        } else {
            // If "/cats_close" button was pushed by Volunteer
            // Get Volunteer from update
            OwnerCat volunteer = ownersCatsHandler.returnVolunteerFromUpdate(update);
            // Set to volunteer field volunteer_chat_opened to false
            Long volunteerChatId = volunteer.getChatId();
            volunteer.setVolunteerChatOpened(false);
            volunteer.setUpdatedAt(LocalDateTime.now());
            ownersCatsRepository.save(volunteer);
            // Get an owner from volunteer
            Optional<OwnerCat> ownerOptional = ownersCatsRepository.findOwnerCatByVolunteer(volunteer);
            if (ownerOptional.isPresent()) {
                OwnerCat ownerCat = ownerOptional.get();
                Long ownerChatId = ownerCat.getChatId();
                //Deleting volunteer out of Owner
                ownerCat.setVolunteer(null);
                ownerCat.setUpdatedAt(LocalDateTime.now());
                ownersCatsRepository.save(ownerCat);
                //Send message to Volunteer and Owner that chat was closed by Volunteer
                mainMenuHandler.volunteerClosedChatMessage(volunteerChatId, ownerChatId);
                return ownerChatId;
            } else {
                System.out.println("Owner с таким Volunteer не найден");
            }
        }
        return null;
    }


    @Override
    public void forwardMessageReceived(Update update) {

        Long chatId = ownUpdatesHandler.extractChatIdFromUpdate(update);
        String text = ownUpdatesHandler.extractTextFromUpdate(update);

        Optional<ChatCats> chatByOwnerOptional = chatsCatsRepository.findByOwnerCatChatId(chatId);

        if (chatByOwnerOptional.isEmpty()) {
            Optional<ChatCats> chatByVolunteerOptional = chatsCatsRepository.findByVolunteerCatChatId(chatId);
            if (chatByVolunteerOptional.isPresent()) {

                ChatCats chat = chatByVolunteerOptional.get();
                Long ownerChatId = chat.getOwnerCat().getChatId();
                SendMessage message = new SendMessage(ownerChatId, text);
                telegramBot.execute(message);

                chat.setAnswerSentTime(LocalDateTime.now());
                chatsCatsRepository.save(chat);
            }
        } else {
            ChatCats chat = chatByOwnerOptional.get();
            Long volunteerChatId = chat.getVolunteerCat().getChatId();
            SendMessage message = new SendMessage(volunteerChatId, text);
            telegramBot.execute(message);
            chat.setMessageSentTime(LocalDateTime.now());
            chatsCatsRepository.save(chat);
        }
    }
}

