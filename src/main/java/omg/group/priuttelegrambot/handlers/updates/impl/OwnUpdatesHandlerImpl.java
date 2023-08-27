package omg.group.priuttelegrambot.handlers.updates.impl;

import com.pengrad.telegrambot.model.Update;
import omg.group.priuttelegrambot.handlers.updates.OwnUpdatesHandler;
import org.springframework.stereotype.Service;

@Service
public class OwnUpdatesHandlerImpl implements OwnUpdatesHandler {

    @Override
    public Long extractChatIdFromUpdate(Update update) {
        if (update.callbackQuery() != null) {
            return update.callbackQuery().message().chat().id();
        } else if (update.message() != null) {
            return update.message().chat().id();
        }
        return 0L;
    }

    @Override
    public String extractTextFromUpdate(Update update) {
        if (update.callbackQuery() != null) {
            return update.callbackQuery().data();
        } else if (update.message() != null) {
            return update.message().text();
        }
        return null;
    }

    @Override
    public int extractMessageIdFromUpdate(Update update) {
        if (update.callbackQuery() != null) {
            return update.callbackQuery().message().messageId();
        } else if (update.message() != null) {
            return update.message().messageId();
        }
        return 0;
    }

    @Override
    public long extractUserIdFromUpdate(Update update) {
        if (update.callbackQuery() != null) {
            return update.callbackQuery().message().from().id();
        } else if (update.message() != null) {
            return update.message().from().id();
        }
        return 0;
    }

    @Override
    public String extractTelephoneFromUpdate(Update update) {
        if (update.message().contact() != null) {
            return update.message().contact().phoneNumber();
        }
        return null;
    }

    @Override
    public int extractDateFromUpdate(Update update) {
        if (update.callbackQuery() != null) {
            return update.callbackQuery().message().date();
        } else if (update.message() != null) {
            return update.message().date();
        }
        return 0;
    }
}
