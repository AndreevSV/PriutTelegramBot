package omg.group.priuttelegrambot.handlers.chats;

import com.pengrad.telegrambot.model.Update;
import omg.group.priuttelegrambot.dto.chats.ChatDogsDto;
import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;

public interface ChatsDogsHandler {
    OwnerDogDto callVolunteer(Update update);
    void executeReplyButtonCommandForVolunteer(Update update);
    Long executeCloseButtonCommand(Update update);
    void forwardMessageReceived(Update update);
    ChatDogsDto findByOwnerDogChatId(Long ownerDogChatId);

    ChatDogsDto findByVolunteerDogChatId(Long volunteerDogChatId);
}
