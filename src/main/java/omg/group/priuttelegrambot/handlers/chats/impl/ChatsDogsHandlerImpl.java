package omg.group.priuttelegrambot.handlers.chats.impl;

import com.pengrad.telegrambot.model.Update;
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
    private final OwnersDogsRepository ownersDogsRepository;
    private final OwnersDogsHandler ownersDogsHandler;
    private final ChatsDogsRepository chatsDogsRepository;
    private final OwnUpdatesHandler ownUpdatesHandler;
    private final MainMenuHandler mainMenuHandler;

    public ChatsDogsHandlerImpl(OwnersDogsRepository ownersDogsRepository,
                                OwnersDogsHandler ownersDogsHandler,
                                ChatsDogsRepository chatsDogsRepository,
                                OwnUpdatesHandler ownUpdatesHandler,
                                MainMenuHandler mainMenuHandler) {
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
        OwnerDog owner = OwnerDogMapper.toEntity(ownerDogDto);
        Optional<ChatDogs> chatOptional = chatsDogsRepository.findByOwner(owner);

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
                ownerDogDto.setVolunteerDto(OwnerDogMapper.toDto(volunteer));
                ownerDogDto.setUpdatedAt(LocalDateTime.now());
                OwnerDog ownerRenewed = OwnerDogMapper.toEntity(ownerDogDto);
                ownersDogsRepository.save(ownerRenewed);

                // 3. Put new chat in chat_cats database with Owner and Volunteer
                ChatDogs chat = new ChatDogs();
                chat.setIsChatting(false); // Till volunteer haven't pushed button /reply chat is not started
                chat.setOwner(ownerRenewed);
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
        Long volunteerChatId = ownUpdatesHandler.getChatId(update);
        Optional<OwnerDog> volunteerOptional = ownersDogsRepository.findByChatId(volunteerChatId);
        if (volunteerOptional.isPresent()) {
            OwnerDog volunteer = volunteerOptional.get();
            Optional<ChatDogs> chatOptional = chatsDogsRepository.findByVolunteer(volunteer);
            if (chatOptional.isPresent()) {
                ChatDogs chat = chatOptional.get();
                Long ownerChatId = chat.getOwner().getChatId();
                chat.setIsChatting(true);
                chat.setMessageSentTime(LocalDateTime.now());
                chatsDogsRepository.save(chat);

                mainMenuHandler.volunteerOpenedChatMessage(ownerChatId, volunteerChatId);
            }
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
            OwnerDogDto volunteerDto = ownerDto.getVolunteerDto();
            Long volunteerChatId = volunteerDto.getChatId();
            // Set to volunteer field volunteer_chat_opened to false
            volunteerDto.setVolunteerChatOpened(false);
            volunteerDto.setUpdatedAt(LocalDateTime.now());
            OwnerDog volunteer = OwnerDogMapper.toEntity(volunteerDto);
            ownersDogsRepository.save(volunteer);
            //Deleting volunteer out of Owner
            ownerDto.setVolunteerDto(null);
            ownerDto.setUpdatedAt(LocalDateTime.now());
            OwnerDog owner = OwnerDogMapper.toEntity(ownerDto);
            ownersDogsRepository.save(owner);
            //Deleting chat in chat_cats database
            Long ownerChatId = ownerDto.getChatId();
            Optional<ChatDogs> chatOptional = chatsDogsRepository.findByOwner(owner);
            if (chatOptional.isPresent()) {
                ChatDogs chat = chatOptional.get();
                chatsDogsRepository.delete(chat);
                //Send message to Owner and Volunteer that chat was closed by Owner
                mainMenuHandler.ownerClosedChatMessage(ownerChatId, volunteerChatId);
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
                //Deleting chat in chat_cats database
                Optional<ChatDogs> chatDogsOptional = chatsDogsRepository.findByVolunteer(volunteer);
                if (chatDogsOptional.isPresent()) {
                    ChatDogs chat = chatDogsOptional.get();
                    chatsDogsRepository.delete(chat);
                    //Send message to Volunteer and Owner that chat was closed by Volunteer
                    mainMenuHandler.volunteerClosedChatMessage(volunteerChatId, ownerChatId);
                    return ownerChatId;
                }
            }
        }
        return null;
    }

    /**
     * Method that receives message and sand it to client or volunteer
     */
    @Override
    public void forwardMessageReceived(Update update) {
        Long chatId = ownUpdatesHandler.getChatId(update);
        ChatDogsDto chatDogsDtoByOwner = findByVolunteerDogChatId(chatId);
        if (chatDogsDtoByOwner != null) {
            Long volunteerChatId = chatDogsDtoByOwner.getVolunteerDto().getChatId();
            mainMenuHandler.chatForwardMessageWithCloseButton(volunteerChatId, update);
            chatDogsDtoByOwner.setMessageSentTime(LocalDateTime.now());
            ChatDogs chat = ChatDogsMapper.toEntity(chatDogsDtoByOwner);
            chatsDogsRepository.save(chat);
        } else {
            ChatDogsDto chatDogsDtoByVolunteer = findByVolunteerDogChatId(chatId);
            if (chatDogsDtoByVolunteer != null) {
                Long ownerChatId = chatDogsDtoByVolunteer.getOwnerDto().getChatId();
                mainMenuHandler.chatForwardMessageWithCloseButton(ownerChatId, update);
                chatDogsDtoByVolunteer.setAnswerSentTime(LocalDateTime.now());
                ChatDogs chat = ChatDogsMapper.toEntity(chatDogsDtoByVolunteer);
                chatsDogsRepository.save(chat);
            }
        }
    }

    @Override
    public ChatDogsDto findByOwnerDogChatId(Long ownerDogChatId) {
        OwnerDogDto ownerDto = ownersDogsHandler.returnOwnerOrVolunteerDogDtoByChatId(ownerDogChatId);
        OwnerDog owner = OwnerDogMapper.toEntity(ownerDto);
        Optional<ChatDogs> chatDogs = chatsDogsRepository.findByOwner(owner);
        if (chatDogs.isPresent()) {
            ChatDogs chat = chatDogs.get();
            return ChatDogsMapper.toDto(chat);
        } else {
            return null;
        }
    }

    @Override
    public ChatDogsDto findByVolunteerDogChatId(Long volunteerDogChatId) {
        OwnerDogDto volunteerDto = ownersDogsHandler.returnOwnerOrVolunteerDogDtoByChatId(volunteerDogChatId);
        OwnerDog volunteer = OwnerDogMapper.toEntity(volunteerDto);
        Optional<ChatDogs> chatDogs = chatsDogsRepository.findByVolunteer(volunteer);
        if (chatDogs.isPresent()) {
            ChatDogs chat = chatDogs.get();
            return ChatDogsMapper.toDto(chat);
        } else {
            return null;
        }
    }
}