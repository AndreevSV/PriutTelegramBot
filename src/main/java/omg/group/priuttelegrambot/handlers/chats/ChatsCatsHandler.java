package omg.group.priuttelegrambot.handlers.chats;

import com.pengrad.telegrambot.model.Update;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;

public interface ChatsCatsHandler {
    void callVolunteer(Update update);
    void executeReplyButtonCommandForVolunteer(Update update);
    Long executeCloseButtonCommand(Update update);
    void forwardMessageReceived(Update update);
}
