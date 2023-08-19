package omg.group.priuttelegrambot.handlers.updates.impl;

import com.pengrad.telegrambot.model.Update;
import omg.group.priuttelegrambot.handlers.updates.OwnUpdatesHandler;
import org.springframework.stereotype.Service;

@Service
public class OwnUpdatesHandlerImpl implements OwnUpdatesHandler {

    @Override
    public Long extractChatIdFromUpdate(Update update) {
        Long chatId = 0L;
        if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
        } else if (update.message() != null) {
            chatId = update.message().chat().id();
        }
        return chatId;
    }

    @Override
    public String extractTextFromUpdate(Update update) {
        String text = "";
        if (update.callbackQuery() != null) {
            text = update.callbackQuery().data();
        } else if (update.message() != null) {
            text = update.message().text();
        }
        return text;
    }

    @Override
    public int extractMessageIdFromUpdate(Update update) {
        int messageId = 0;
        if (update.callbackQuery() != null) {
            messageId = update.callbackQuery().message().messageId();
        } else if (update.message() != null) {
            messageId = update.message().messageId();
        }
        return messageId;
    }

    @Override
    public long extractUserIdFromUpdate(Update update) {
        long userId = 0;
        if (update.callbackQuery() != null) {
            userId = update.callbackQuery().message().from().id();
        } else if (update.message() != null) {
            userId = update.message().from().id();
        }
        return userId;
    }

}
