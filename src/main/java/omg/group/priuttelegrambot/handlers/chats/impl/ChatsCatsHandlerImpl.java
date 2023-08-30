package omg.group.priuttelegrambot.handlers.chats.impl;

import com.pengrad.telegrambot.model.Update;
import omg.group.priuttelegrambot.dto.chats.ChatCatsDto;
import omg.group.priuttelegrambot.dto.chats.ChatCatsMapper;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.dto.owners.OwnerCatMapper;
import omg.group.priuttelegrambot.entity.chats.ChatCats;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.handlers.chats.ChatsCatsHandler;
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
    private final OwnersCatsRepository ownersCatsRepository;
    private final OwnersCatsHandler ownersCatsHandler;
    private final ChatsCatsRepository chatsCatsRepository;
    private final OwnUpdatesHandler ownUpdatesHandler;
    private final MainMenuHandler mainMenuHandler;

    public ChatsCatsHandlerImpl(OwnersCatsRepository ownersCatsRepository,
                                OwnersCatsHandler ownersCatsHandler,
                                ChatsCatsRepository chatsCatsRepository,
                                OwnUpdatesHandler ownUpdatesHandler,
                                MainMenuHandler mainMenuHandler) {
        this.ownersCatsRepository = ownersCatsRepository;
        this.ownersCatsHandler = ownersCatsHandler;
        this.chatsCatsRepository = chatsCatsRepository;
        this.ownUpdatesHandler = ownUpdatesHandler;
        this.mainMenuHandler = mainMenuHandler;
    }

    /**
     * When client push button "Позвать волонтера" method sends request to the Volunteer to start a chat.
     * Volunteer answers to client clicking on button "Ответить"
     * If chat is already started client receives a message that chat is already started
     * To not duplicate code method works with startingChat method below
     */
    @Override
    public OwnerCatDto callVolunteer(Update update) {

        OwnerCatDto ownerCatDto = ownersCatsHandler.returnOwnerCatDtoFromUpdate(update);

        Long ownerChatId = ownerCatDto.getChatId();
        OwnerCat owner = OwnerCatMapper.toEntity(ownerCatDto);
        Optional<ChatCats> chatOptional = chatsCatsRepository.findByOwner(owner);

        if (chatOptional.isPresent()) {
            //  If owner already has a chat send him a message about it
            mainMenuHandler.chatAlreadySetToOwnerMessage(ownerChatId);
            return null;
        } else {
            // If owner hasn't a chat searches for a free volunteer
            Optional<OwnerCat> volunteerOptional = ownersCatsRepository.findVolunteerByVolunteerIsTrueAndNoChatsOpened();

            if (volunteerOptional.isEmpty()) {
                // If no free volunteer exist send to owner a message about it
                mainMenuHandler.noFreeVolunteerAvailableMessage(ownerChatId);
                return null;
            } else {
                // If free volunteer exist
                // 1. Set to Volunteer field volunteer_chat_opened in clients_cats database to true
                OwnerCat volunteer = volunteerOptional.get();
                Long volunteerChatId = volunteer.getChatId();
                volunteer.setVolunteerChatOpened(true);
                ownersCatsRepository.save(volunteer);

                // 2. Set to Owner a Volunteer in clients_cats_database
                ownerCatDto.setVolunteerDto(OwnerCatMapper.toDto(volunteer));
                ownerCatDto.setUpdatedAt(LocalDateTime.now());
                OwnerCat ownerRenewed = OwnerCatMapper.toEntity(ownerCatDto);
                ownersCatsRepository.save(ownerRenewed);

                // 3. Put new chat in chat_cats database with Owner and Volunteer
                ChatCats chat = new ChatCats();
                chat.setIsChatting(false); // Till volunteer haven't pushed button /reply chat is not started
                chat.setOwner(ownerRenewed);
                chat.setVolunteer(volunteer);
                chat.setCreatedAt(LocalDateTime.now());
                chatsCatsRepository.save(chat);

                // 4. Send messages to Volunteer and Owner that inquiry was sent
                mainMenuHandler.inquiryToVolunteerForChat(volunteerChatId, ownerChatId);

                // 5. Get volunteer and create VolunteerDto
                Optional<OwnerCat> volunteerOptionalByChatId = ownersCatsRepository.findByChatId(volunteerChatId);
                if (volunteerOptionalByChatId.isPresent()) {
                    OwnerCat volunteerCat = volunteerOptionalByChatId.get();
                    return OwnerCatMapper.toDto(volunteerCat);
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
        Optional<OwnerCat> volunteerOptional = ownersCatsRepository.findByChatId(volunteerChatId);
        if (volunteerOptional.isPresent()) {
            OwnerCat volunteer = volunteerOptional.get();
            Optional<ChatCats> chatOptional = chatsCatsRepository.findByVolunteer(volunteer);
            if (chatOptional.isPresent()) {
                ChatCats chat = chatOptional.get();
                Long ownerChatId = chat.getOwner().getChatId();
                chat.setIsChatting(true);
                chat.setMessageSentTime(LocalDateTime.now());
                chatsCatsRepository.save(chat);

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

        OwnerCatDto ownerDto = ownersCatsHandler.returnOwnerCatDtoFromUpdate(update);

        if (ownerDto != null) {
            // If "/cats_close" button was pushed by Owner
            // Get a volunteer from owner
            OwnerCatDto volunteerDto = ownerDto.getVolunteerDto();
            Long volunteerChatId = volunteerDto.getChatId();
            // Set to volunteer field volunteer_chat_opened to false
            volunteerDto.setVolunteerChatOpened(false);
            volunteerDto.setUpdatedAt(LocalDateTime.now());
            OwnerCat volunteer = OwnerCatMapper.toEntity(volunteerDto);
            ownersCatsRepository.save(volunteer);
            //Deleting volunteer out of Owner
            ownerDto.setVolunteerDto(null);
            ownerDto.setUpdatedAt(LocalDateTime.now());
            OwnerCat owner = OwnerCatMapper.toEntity(ownerDto);
            ownersCatsRepository.save(owner);
            //Deleting chat in chat_cats database
            Long ownerChatId = ownerDto.getChatId();
            Optional<ChatCats> chatOptional = chatsCatsRepository.findByOwner(owner);
            if (chatOptional.isPresent()) {
                ChatCats chat = chatOptional.get();
                chatsCatsRepository.delete(chat);
                //Send message to Owner and Volunteer that chat was closed by Owner
                mainMenuHandler.ownerClosedChatMessage(ownerChatId, volunteerChatId);
            }
            return volunteerChatId;
        } else {
            // If "/cats_close" button was pushed by Volunteer
            // Get Volunteer from update
            OwnerCatDto volunteerDto = ownersCatsHandler.returnVolunteerCatDtoFromUpdate(update);
            // Set to volunteer field volunteer_chat_opened to false
            Long volunteerChatId = volunteerDto.getChatId();
            volunteerDto.setVolunteerChatOpened(false);
            volunteerDto.setUpdatedAt(LocalDateTime.now());
            OwnerCat volunteer = OwnerCatMapper.toEntity(volunteerDto);
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
                //Deleting chat in chat_cats database
                Optional<ChatCats> chatCatsOptional = chatsCatsRepository.findByVolunteer(volunteer);
                if (chatCatsOptional.isPresent()) {
                    ChatCats chat = chatCatsOptional.get();
                    chatsCatsRepository.delete(chat);
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
        ChatCatsDto chatCatsDtoByOwner = findByOwnerCatChatId(chatId);
        if (chatCatsDtoByOwner != null) {
            Long volunteerChatId = chatCatsDtoByOwner.getVolunteerDto().getChatId();
            mainMenuHandler.chatForwardMessageWithCloseButton(volunteerChatId, update);
            chatCatsDtoByOwner.setMessageSentTime(LocalDateTime.now());
            ChatCats chat = ChatCatsMapper.toEntity(chatCatsDtoByOwner);
            chatsCatsRepository.save(chat);
        } else {
            ChatCatsDto chatCatsDtoByVolunteer = findByVolunteerCatChatId(chatId);
            if (chatCatsDtoByVolunteer != null) {
                Long ownerChatId = chatCatsDtoByVolunteer.getOwnerDto().getChatId();
                mainMenuHandler.chatForwardMessageWithCloseButton(ownerChatId, update);
                chatCatsDtoByVolunteer.setAnswerSentTime(LocalDateTime.now());
                ChatCats chat = ChatCatsMapper.toEntity(chatCatsDtoByVolunteer);
                chatsCatsRepository.save(chat);
            }
        }
    }

    @Override
    public ChatCatsDto findByOwnerCatChatId(Long ownerCatChatId) {
        OwnerCatDto ownerDto = ownersCatsHandler.returnOwnerOrVolunteerCatDtoByChatId(ownerCatChatId);
        OwnerCat owner = OwnerCatMapper.toEntity(ownerDto);
        Optional<ChatCats> chatCats = chatsCatsRepository.findByOwner(owner);
        if (chatCats.isPresent()) {
            ChatCats chat = chatCats.get();
            return ChatCatsMapper.toDto(chat);
        } else {
            return null;
        }
    }

    @Override
    public ChatCatsDto findByVolunteerCatChatId(Long volunteerCatChatId) {
        OwnerCatDto volunteerDto = ownersCatsHandler.returnOwnerOrVolunteerCatDtoByChatId(volunteerCatChatId);
        OwnerCat volunteer = OwnerCatMapper.toEntity(volunteerDto);
        Optional<ChatCats> chatCats = chatsCatsRepository.findByVolunteer(volunteer);
        if (chatCats.isPresent()) {
            ChatCats chat = chatCats.get();
            return ChatCatsMapper.toDto(chat);
        } else {
            return null;
        }
    }
}