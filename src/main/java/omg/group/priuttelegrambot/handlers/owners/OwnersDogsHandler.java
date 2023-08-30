package omg.group.priuttelegrambot.handlers.owners;

import com.pengrad.telegrambot.model.Update;
import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;
import omg.group.priuttelegrambot.dto.pets.DogDto;

import java.util.List;

public interface OwnersDogsHandler {
    void newOwnerRegister(Update update);
    OwnerDogDto checkForOwnerExist(Update update);
    List<DogDto> checkForOwnerHasDog(OwnerDogDto ownerDogDto);
    OwnerDogDto returnOwnerDogDtoFromUpdate(Update update);
    OwnerDogDto returnVolunteerDogDtoFromUpdate(Update update);

    OwnerDogDto returnOwnerOrVolunteerDogDtoByChatId(Long chatId);
}
