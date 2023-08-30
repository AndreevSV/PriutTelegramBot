package omg.group.priuttelegrambot.handlers.updates.impl;

import com.pengrad.telegrambot.model.Update;
import omg.group.priuttelegrambot.handlers.updates.OwnUpdatesHandler;
import org.springframework.stereotype.Service;

@Service
public class OwnUpdatesHandlerImpl implements OwnUpdatesHandler {

    @Override
    public Long getChatId(Update update) {
        if (update.callbackQuery() != null) {
            return update.callbackQuery().message().chat().id();
        } else if (update.message() != null) {
            return update.message().chat().id();
        }
        return 0L;
    }

    @Override
    public String getFirstName(Update update) {
        if (update.message() != null) {
            return update.message().from().firstName();
        } else if (update.callbackQuery() != null) {
            return update.callbackQuery().from().firstName();
        }
        return null;
    }

    @Override
    public String getText(Update update) {
        if (update.callbackQuery() != null) {
            return update.callbackQuery().data();
        } else if (update.message() != null) {
            return update.message().text();
        }
        return null;
    }

    @Override
    public int getMessageId(Update update) {
        if (update.callbackQuery() != null) {
            return update.callbackQuery().message().messageId();
        } else if (update.message() != null) {
            return update.message().messageId();
        }
        return 0;
    }

    @Override
    public long getUserId(Update update) {
        if (update.callbackQuery() != null) {
            return update.callbackQuery().message().from().id();
        } else if (update.message() != null) {
            return update.message().from().id();
        }
        return 0;
    }

    @Override
    public String getPhoneNumber(Update update) {
        if (update.message().contact() != null) {
            return update.message().contact().phoneNumber();
        }
        return null;
    }

    @Override
    public int getDate(Update update) {
        if (update.callbackQuery() != null) {
            return update.callbackQuery().message().date();
        } else if (update.message() != null) {
            return update.message().date();
        }
        return 0;
    }
}
