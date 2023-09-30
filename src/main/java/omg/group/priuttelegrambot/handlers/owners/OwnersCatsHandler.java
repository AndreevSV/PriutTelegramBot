package omg.group.priuttelegrambot.handlers.owners;

import com.pengrad.telegrambot.model.Update;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.dto.pets.CatDto;

import java.util.List;

public interface OwnersCatsHandler {
    void newOwnerRegister(Update update);
    OwnerCatDto checkForOwnerExist(Update update);
    List<CatDto> checkForOwnerHasCat(OwnerCatDto ownerDto);
    OwnerCatDto returnOwnerCatDtoFromUpdate(Update update);
    OwnerCatDto returnVolunteerCatDtoFromUpdate(Update update);

    OwnerCatDto returnOwnerOrVolunteerCatDtoByChatId(Long chatId);
}
