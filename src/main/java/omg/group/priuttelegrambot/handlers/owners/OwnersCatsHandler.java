package omg.group.priuttelegrambot.handlers.owners;

import com.pengrad.telegrambot.model.Update;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.entity.pets.Cat;

import java.util.List;

public interface OwnersCatsHandler {
    void newOwnerRegister(Update update);
    OwnerCatDto checkForOwnerExist(Update update);
    List<Cat> checkForOwnerHasCat(OwnerCat ownerCat);
    OwnerCatDto returnOwnerCatDtoFromUpdate(Update update);
    OwnerCatDto returnVolunteerCatDtoFromUpdate(Update update);
}
