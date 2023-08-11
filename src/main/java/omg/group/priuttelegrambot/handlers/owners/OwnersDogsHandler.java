package omg.group.priuttelegrambot.handlers.owners;

import com.pengrad.telegrambot.model.Update;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;

public interface OwnersDogsHandler {
    void callVolunteer(Update update);

    void startingChat(OwnerDog owner);

    void executeReplyButtonCommandForVolunteer(Update update);

    OwnerDog returnOwnerFromUpdate(Update update);

    OwnerDog returnVolunteerFromUpdate(Update update);

    void executeCloseButtonCommand(Update update);

    void sendMessageReceived(Update update);

}
