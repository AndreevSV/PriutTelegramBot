package omg.group.priuttelegrambot.handlers.contacts.impl;

import com.pengrad.telegrambot.model.Update;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.handlers.contacts.OwnersDogsContactsHandler;
import omg.group.priuttelegrambot.handlers.updates.OwnUpdatesHandler;
import omg.group.priuttelegrambot.repository.owners.OwnersDogsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OwnersDogsContactsHandlerImpl implements OwnersDogsContactsHandler {

    private final OwnUpdatesHandler ownUpdatesHandler;
    private final OwnersDogsRepository ownersDogsRepository;

    public OwnersDogsContactsHandlerImpl(OwnUpdatesHandler ownUpdatesHandler, OwnersDogsRepository ownersDogsRepository) {
        this.ownUpdatesHandler = ownUpdatesHandler;
        this.ownersDogsRepository = ownersDogsRepository;
    }

    @Override
    public void savePhoneNumberFromContact(Update update) {

        Long userId = update.message().contact().userId();
        String firstName = update.message().contact().firstName();
        String lastName = update.message().contact().lastName();
        String phoneNumber = update.message().contact().phoneNumber();

        Long chatId = ownUpdatesHandler.extractChatIdFromUpdate(update);

        Optional<OwnerDog> ownerOptional = ownersDogsRepository.findByChatId(chatId);

        if (ownerOptional.isPresent()) {
            OwnerDog owner = ownerOptional.get();
            owner.setTelephone(phoneNumber);
            owner.setUpdatedAt(LocalDateTime.now());
            ownersDogsRepository.save(owner);
        } else {
            OwnerDog owner = new OwnerDog();
            owner.setName(firstName);
            owner.setSurname(lastName);
            owner.setTelephone(phoneNumber);
            owner.setCreatedAt(LocalDateTime.now());
            owner.setIsVolunteer(false);
            owner.setChatId(chatId);
            owner.setTelegramUserId(userId);
            ownersDogsRepository.save(owner);
        }
    }
}
