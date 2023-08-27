package omg.group.priuttelegrambot.handlers.chats.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import omg.group.priuttelegrambot.dto.chats.ChatDogsDto;
import omg.group.priuttelegrambot.dto.chats.ChatDogsMapper;
import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;
import omg.group.priuttelegrambot.dto.owners.OwnerDogMapper;
import omg.group.priuttelegrambot.entity.chats.ChatDogs;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.handlers.chats.ChatsDogsHandler;
import omg.group.priuttelegrambot.handlers.menu.MainMenuHandler;
import omg.group.priuttelegrambot.handlers.owners.OwnersDogsHandler;
import omg.group.priuttelegrambot.handlers.updates.OwnUpdatesHandler;
import omg.group.priuttelegrambot.repository.chats.ChatsDogsRepository;
import omg.group.priuttelegrambot.repository.owners.OwnersDogsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ChatsDogsHandlerImpl implements ChatsDogsHandler {

    private final TelegramBot telegramBot;
    private final OwnersDogsRepository ownersDogsRepository;
    private final OwnersDogsHandler ownersDogsHandler;
    private final ChatsDogsRepository chatsDogsRepository;
    private final OwnUpdatesHandler ownUpdatesHandler;
    private final MainMenuHandler mainMenuHandler;

    public ChatsDogsHandlerImpl(TelegramBot telegramBot,
                                OwnersDogsRepository ownersDogsRepository,
                                OwnersDogsHandler ownersDogsHandler,
                                ChatsDogsRepository chatsDogsRepository, OwnUpdatesHandler ownUpdatesHandler, MainMenuHandler mainMenuHandler) {
        this.telegramBot = telegramBot;
        this.ownersDogsRepository = ownersDogsRepository;
        this.ownersDogsHandler = ownersDogsHandler;
        this.chatsDogsRepository = chatsDogsRepository;
        this.ownUpdatesHandler = ownUpdatesHandler;
        this.mainMenuHandler = mainMenuHandler;
    }

    /**
     * When client push button "Позвать волонтера" method sends request to the volunteer to start a chat.
     * Volunteer answers to client clicking on button "Ответить"
     * If chat is already started client receives a message that chat is already started
     * To not duplicate code method works with startingChat method below
     */
    @Override
    public OwnerDogDto callVolunteer(Update update) {

        OwnerDogDto ownerDogDto = ownersDogsHandler.returnOwnerDogDtoFromUpdate(update);

        Long ownerChatId = ownerDogDto.getChatId();
        Optional<ChatDogs> chatOptional = chatsDogsRepository.findByOwnerDogChatId(ownerChatId);

        if (chatOptional.isPresent()) {
            //  If owner already has a chat send him a message about it
            mainMenuHandler.chatAlreadySetToOwnerMessage(ownerChatId);
            return null;
        } else {
            // If owner hasn't a chat searches for a free volunteer
            Optional<OwnerDog> volunteerOptional = ownersDogsRepository.findVolunteerByVolunteerIsTrueAndNoChatsOpened();

            if (volunteerOptional.isEmpty()) {
                // If no free volunteer exist send to owner a message about it
                mainMenuHandler.noFreeVolunteerAvailableMessage(ownerChatId);
                return null;
            } else {
                // If free volunteer exist
                // 1. Set to Volunteer field volunteer_chat_opened in clients_cats database to true
                OwnerDog volunteer = volunteerOptional.get();
                Long volunteerChatId = volunteer.getChatId();
                volunteer.setVolunteerChatOpened(true);
                ownersDogsRepository.save(volunteer);

                // 2. Set to Owner a Volunteer in clients_cats_database
                ownerDogDto.setVolunteer(volunteer);
                ownerDogDto.setUpdatedAt(LocalDateTime.now());
                OwnerDog owner = OwnerDogMapper.toEntity(ownerDogDto);
                ownersDogsRepository.save(owner);

                // 3. Put new chat in chat_cats database with Owner and Volunteer
                ChatDogs chat = new ChatDogs();
                chat.setIsChatting(false); // Till volunteer haven't pushed button /reply chat is not started
                chat.setOwner(owner);
                chat.setVolunteer(volunteer);
                chat.setCreatedAt(LocalDateTime.now());
                chatsDogsRepository.save(chat);

                // 4. Send messages to Volunteer and Owner that inquiry was sent
                mainMenuHandler.inquiryToVolunteerForChat(volunteerChatId, ownerChatId);

                // 5. Get volunteer and create VolunteerDto
                Optional<OwnerDog> volunteerOptionalByChatId = ownersDogsRepository.findByChatId(volunteerChatId);
                if (volunteerOptionalByChatId.isPresent()) {
                    OwnerDog volunteerDog = volunteerOptionalByChatId.get();
                    return OwnerDogMapper.toDto(volunteerDog);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Method starts a chat when volunteer pushes on button "Ответить" - "/cats_reply" command
     */
    @Override
    public void executeReplyButtonCommandForVolunteer(Update update) {
        Long volunteerChatId = ownUpdatesHandler.extractChatIdFromUpdate(update);
        Optional<ChatDogs> chatOptional = chatsDogsRepository.findByVolunteerDogChatId(volunteerChatId);

        if (chatOptional.isPresent()) {
            ChatDogs chat = chatOptional.get();
            Long ownerChatId = chat.getOwner().getChatId();
            chat.setIsChatting(true);
            chat.setMessageSentTime(LocalDateTime.now());
            chatsDogsRepository.save(chat);

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

        OwnerDogDto ownerDto = ownersDogsHandler.returnOwnerDogDtoFromUpdate(update);

        if (ownerDto != null) {
            // If "/cats_close" button was pushed by Owner
            // Get a volunteer from owner
            OwnerDog volunteer = ownerDto.getVolunteer();
            Long volunteerChatId = volunteer.getChatId();
            // Set to volunteer field volunteer_chat_opened to false
            volunteer.setVolunteerChatOpened(false);
            volunteer.setUpdatedAt(LocalDateTime.now());
            ownersDogsRepository.save(volunteer);
            //Deleting volunteer out of Owner
            ownerDto.setVolunteer(null);
            ownerDto.setUpdatedAt(LocalDateTime.now());
            OwnerDog owner = OwnerDogMapper.toEntity(ownerDto);
            ownersDogsRepository.save(owner);
            //Deleting chat in chat_cats database
            Long ownerChatId = ownerDto.getChatId();
            Optional<ChatDogs> chatOptional = chatsDogsRepository.findByOwnerDogChatId(ownerChatId);
            if (chatOptional.isPresent()) {
                ChatDogs chat = chatOptional.get();
                chatsDogsRepository.delete(chat);
                //Send message to Owner and Volunteer that chat was closed by Owner
                mainMenuHandler.ownerClosedChatMessage(ownerChatId, volunteerChatId);
            } else {
                System.out.println("Чат с таким OwnerChatId не найден");
            }
            return volunteerChatId;
        } else {
            // If "/cats_close" button was pushed by Volunteer
            // Get Volunteer from update
            OwnerDogDto volunteerDto = ownersDogsHandler.returnVolunteerDogDtoFromUpdate(update);
            // Set to volunteer field volunteer_chat_opened to false
            Long volunteerChatId = volunteerDto.getChatId();
            volunteerDto.setVolunteerChatOpened(false);
            volunteerDto.setUpdatedAt(LocalDateTime.now());
            OwnerDog volunteer = OwnerDogMapper.toEntity(volunteerDto);
            ownersDogsRepository.save(volunteer);
            // Get an owner from volunteer
            Optional<OwnerDog> ownerOptional = ownersDogsRepository.findOwnerDogByVolunteer(volunteer);
            if (ownerOptional.isPresent()) {
                OwnerDog ownerDog = ownerOptional.get();
                Long ownerChatId = ownerDog.getChatId();
                //Deleting volunteer out of Owner
                ownerDog.setVolunteer(null);
                ownerDog.setUpdatedAt(LocalDateTime.now());
                ownersDogsRepository.save(ownerDog);
                //Send message to Volunteer and Owner that chat was closed by Volunteer
                mainMenuHandler.volunteerClosedChatMessage(volunteerChatId, ownerChatId);
                return ownerChatId;
            } else {
                System.out.println("Owner с таким Volunteer не найден");
            }
        }
        return null;
    }

    /**
     * Method that receives message and sand it to client or volunteer
     */
    @Override
    public void forwardMessageReceived(Update update) {

        Long chatId = ownUpdatesHandler.extractChatIdFromUpdate(update);
        String text = ownUpdatesHandler.extractTextFromUpdate(update);

        Optional<ChatDogs> chatByOwnerOptional = chatsDogsRepository.findByOwnerDogChatId(chatId);

        if (chatByOwnerOptional.isEmpty()) {
            Optional<ChatDogs> chatByVolunteerOptional = chatsDogsRepository.findByVolunteerDogChatId(chatId);
            if (chatByVolunteerOptional.isPresent()) {

                ChatDogs chat = chatByVolunteerOptional.get();
                Long ownerChatId = chat.getOwner().getChatId();
                SendMessage message = new SendMessage(ownerChatId, text);
                telegramBot.execute(message);

                chat.setAnswerSentTime(LocalDateTime.now());
                chatsDogsRepository.save(chat);
            }
        } else {
            ChatDogs chat = chatByOwnerOptional.get();
            Long volunteerChatId = chat.getVolunteer().getChatId();
            SendMessage message = new SendMessage(volunteerChatId, text);
            telegramBot.execute(message);
            chat.setMessageSentTime(LocalDateTime.now());
            chatsDogsRepository.save(chat);
        }
    }

    @Override
    public ChatDogsDto findByOwnerDogChatId(Long ownerDogChatId) {
        Optional<ChatDogs> chatDogs = chatsDogsRepository.findByOwnerDogChatId(ownerDogChatId);
        if (chatDogs.isPresent()) {
            ChatDogs chat = chatDogs.get();
            return ChatDogsMapper.toDto(chat);
        } else {
            return null;
        }
    }
}