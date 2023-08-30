package omg.group.priuttelegrambot.handlers.chats;

import com.pengrad.telegrambot.model.Update;
import omg.group.priuttelegrambot.dto.chats.ChatCatsDto;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;

public interface ChatsCatsHandler {
    OwnerCatDto callVolunteer(Update update);
    void executeReplyButtonCommandForVolunteer(Update update);
    Long executeCloseButtonCommand(Update update);
    void forwardMessageReceived(Update update);
    ChatCatsDto findByOwnerCatChatId(Long ownerCatChatId);

    ChatCatsDto findByVolunteerCatChatId(Long volunteerCatChatId);
}
