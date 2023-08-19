package omg.group.priuttelegrambot.handlers.owners;

import com.pengrad.telegrambot.model.Update;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.entity.pets.Dog;

import java.util.List;

public interface OwnersDogsHandler {
    void newOwnerRegister(Update update);
    OwnerDog checkForOwnerExist(Update update);
    List<Dog> checkForOwnerHasDog(OwnerDog ownerDog);
    OwnerDog returnOwnerFromUpdate(Update update);
    OwnerDog returnVolunteerFromUpdate(Update update);
}
