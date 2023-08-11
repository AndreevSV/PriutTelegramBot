package omg.group.priuttelegrambot.handlers.owners;

import com.pengrad.telegrambot.model.Update;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;

public interface OwnersCatsHandler {
    void callVolunteer(Update update);

    void startingChat(OwnerCat owner);

    void executeReplyButtonCommandForVolunteer(Update update);

    void executeCloseButtonCommand(Update update);

    void sendMessageReceived(Update update);
}
