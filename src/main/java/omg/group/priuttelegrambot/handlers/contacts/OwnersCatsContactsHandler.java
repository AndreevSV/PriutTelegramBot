package omg.group.priuttelegrambot.handlers.contacts;

import com.pengrad.telegrambot.model.Update;

public interface OwnersCatsContactsHandler {
    void savePhoneNumberFromContact(Update update);

    boolean isTelephone(Update update);
}
