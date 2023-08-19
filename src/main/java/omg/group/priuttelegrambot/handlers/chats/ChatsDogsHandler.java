package omg.group.priuttelegrambot.handlers.chats;

import com.pengrad.telegrambot.model.Update;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;

public interface ChatsDogsHandler {
    void callVolunteer(Update update);
    void startingChat(OwnerDog owner);
    void executeReplyButtonCommandForVolunteer(Update update);
    void executeCloseButtonCommand(Update update);
    void sendMessageReceived(Update update);
}
