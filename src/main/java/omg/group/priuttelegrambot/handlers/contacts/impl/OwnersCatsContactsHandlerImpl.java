package omg.group.priuttelegrambot.handlers.contacts.impl;

import com.pengrad.telegrambot.model.Update;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.handlers.contacts.OwnersCatsContactsHandler;
import omg.group.priuttelegrambot.handlers.updates.OwnUpdatesHandler;
import omg.group.priuttelegrambot.repository.owners.OwnersCatsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OwnersCatsContactsHandlerImpl implements OwnersCatsContactsHandler {

    private final OwnUpdatesHandler ownUpdatesHandler;
    private final OwnersCatsRepository ownersCatsRepository;

    public OwnersCatsContactsHandlerImpl(OwnUpdatesHandler ownUpdatesHandler,
                                         OwnersCatsRepository ownersCatsRepository) {
        this.ownUpdatesHandler = ownUpdatesHandler;
        this.ownersCatsRepository = ownersCatsRepository;
    }

    @Override
    public void savePhoneNumberFromContact(Update update) {

        Long userId = update.message().contact().userId();
        String firstName = update.message().contact().firstName();
        String lastName = update.message().contact().lastName();
        String phoneNumber = update.message().contact().phoneNumber();

        Long chatId = ownUpdatesHandler.extractChatIdFromUpdate(update);

        Optional<OwnerCat> ownerOptional = ownersCatsRepository.findByChatId(chatId);

        if (ownerOptional.isPresent()) {
            OwnerCat owner = ownerOptional.get();
            owner.setTelephone(phoneNumber);
            owner.setUpdatedAt(LocalDateTime.now());
            ownersCatsRepository.save(owner);
        } else {
            OwnerCat owner = new OwnerCat();
            owner.setName(firstName);
            owner.setSurname(lastName);
            owner.setTelephone(phoneNumber);
            owner.setCreatedAt(LocalDateTime.now());
            owner.setIsVolunteer(false);
            owner.setChatId(chatId);
            owner.setTelegramUserId(userId);
            ownersCatsRepository.save(owner);
        }
    }
}
