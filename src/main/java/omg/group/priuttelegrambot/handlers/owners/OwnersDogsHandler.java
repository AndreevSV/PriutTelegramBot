package omg.group.priuttelegrambot.handlers.owners;

import com.pengrad.telegrambot.model.Update;
import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.entity.pets.Dog;

import java.util.List;

public interface OwnersDogsHandler {
    void newOwnerRegister(Update update);
    OwnerDogDto checkForOwnerExist(Update update);
    List<Dog> checkForOwnerHasDog(OwnerDog ownerDog);
    OwnerDogDto returnOwnerDogDtoFromUpdate(Update update);
    OwnerDogDto returnVolunteerDogDtoFromUpdate(Update update);
}
