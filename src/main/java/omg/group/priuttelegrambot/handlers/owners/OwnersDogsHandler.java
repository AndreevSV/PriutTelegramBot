package omg.group.priuttelegrambot.handlers.owners;

import com.pengrad.telegrambot.model.Update;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;

import java.util.Optional;

public interface OwnersDogsHandler {
    void callVolunteer(Update update);

    void settingChat(Optional<OwnerDog> ownerDog);

    void executeReplyButtonCommandForVolunteer(Update update);

    OwnerDog returnOwnerDogFromUpdate(Update update);

    OwnerDog returnVolunteerDogFromUpdate(Update update);
}
