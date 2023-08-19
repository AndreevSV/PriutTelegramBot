package omg.group.priuttelegrambot.handlers.owners;

import com.pengrad.telegrambot.model.Update;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.entity.pets.Cat;

import java.util.List;

public interface OwnersCatsHandler {
    void newOwnerRegister(Update update);
    OwnerCat checkForOwnerExist(Update update);
    List<Cat> checkForOwnerHasCat(OwnerCat ownerCat);
    OwnerCat returnOwnerFromUpdate(Update update);
    OwnerCat returnVolunteerFromUpdate(Update update);
}
