package omg.group.priuttelegrambot.handlers.updates;

import com.pengrad.telegrambot.model.Update;

public interface OwnUpdatesHandler {
    Long extractChatIdFromUpdate(Update update);

    String extractTextFromUpdate(Update update);

    int extractMessageIdFromUpdate(Update update);

    long extractUserIdFromUpdate(Update update);

    String extractTelephoneFromUpdate(Update update);

    int extractDateFromUpdate(Update update);
}
