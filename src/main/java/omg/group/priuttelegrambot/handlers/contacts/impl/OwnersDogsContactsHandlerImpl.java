package omg.group.priuttelegrambot.handlers.contacts.impl;

import com.pengrad.telegrambot.model.Update;
import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;
import omg.group.priuttelegrambot.dto.owners.OwnerDogMapper;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.handlers.contacts.OwnersDogsContactsHandler;
import omg.group.priuttelegrambot.handlers.owners.OwnersDogsHandler;
import omg.group.priuttelegrambot.handlers.updates.OwnUpdatesHandler;
import omg.group.priuttelegrambot.repository.owners.OwnersDogsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OwnersDogsContactsHandlerImpl implements OwnersDogsContactsHandler {

    private final OwnUpdatesHandler ownUpdatesHandler;
    private final OwnersDogsHandler ownersDogsHandler;
    private final OwnersDogsRepository ownersDogsRepository;

    public OwnersDogsContactsHandlerImpl(OwnUpdatesHandler ownUpdatesHandler,
                                         OwnersDogsHandler ownersDogsHandler,
                                         OwnersDogsRepository ownersDogsRepository) {
        this.ownUpdatesHandler = ownUpdatesHandler;
        this.ownersDogsHandler = ownersDogsHandler;
        this.ownersDogsRepository = ownersDogsRepository;
    }

    @Override
    public void savePhoneNumberFromContact(Update update) {
        OwnerDogDto ownerDogDto = ownersDogsHandler.returnOwnerDogDtoFromUpdate(update);
        String telephone = ownUpdatesHandler.getPhoneNumber(update);

        ownerDogDto.setTelephone(telephone);
        ownerDogDto.setUpdatedAt(LocalDateTime.now());
        OwnerDog owner = OwnerDogMapper.toEntity(ownerDogDto);
        ownersDogsRepository.save(owner);
    }

    @Override
    public boolean isTelephone(Update update) {
        OwnerDogDto ownerDogDto = ownersDogsHandler.returnOwnerDogDtoFromUpdate(update);
        return !ownerDogDto.getTelephone().isEmpty();
    }
}
