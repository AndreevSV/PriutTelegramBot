package omg.group.priuttelegrambot.handlers.contacts.impl;

import com.pengrad.telegrambot.model.Update;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.dto.owners.OwnerCatMapper;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.handlers.contacts.OwnersCatsContactsHandler;
import omg.group.priuttelegrambot.handlers.owners.OwnersCatsHandler;
import omg.group.priuttelegrambot.handlers.updates.OwnUpdatesHandler;
import omg.group.priuttelegrambot.repository.owners.OwnersCatsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OwnersCatsContactsHandlerImpl implements OwnersCatsContactsHandler {

    private final OwnUpdatesHandler ownUpdatesHandler;
    private final OwnersCatsHandler ownersCatsHandler;
    private final OwnersCatsRepository ownersCatsRepository;

    public OwnersCatsContactsHandlerImpl(OwnUpdatesHandler ownUpdatesHandler,
                                         OwnersCatsHandler ownersCatsHandler,
                                         OwnersCatsRepository ownersCatsRepository) {
        this.ownUpdatesHandler = ownUpdatesHandler;
        this.ownersCatsHandler = ownersCatsHandler;
        this.ownersCatsRepository = ownersCatsRepository;
    }

    @Override
    public void savePhoneNumberFromContact(Update update) {
        OwnerCatDto ownerCatDto = ownersCatsHandler.returnOwnerCatDtoFromUpdate(update);
        String telephone = ownUpdatesHandler.getPhoneNumber(update);

        ownerCatDto.setTelephone(telephone);
        ownerCatDto.setUpdatedAt(LocalDateTime.now());
        OwnerCat owner = OwnerCatMapper.toEntity(ownerCatDto);
        ownersCatsRepository.save(owner);
    }

    @Override
    public boolean isTelephone(Update update) {
        OwnerCatDto ownerCatDto = ownersCatsHandler.returnOwnerCatDtoFromUpdate(update);
        return !ownerCatDto.getTelephone().isEmpty();
    }
}