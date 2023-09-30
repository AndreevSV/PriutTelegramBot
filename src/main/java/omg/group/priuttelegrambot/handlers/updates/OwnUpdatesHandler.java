package omg.group.priuttelegrambot.handlers.updates;

import com.pengrad.telegrambot.model.Update;

public interface OwnUpdatesHandler {
    Long getChatId(Update update);

    String getFirstName(Update update);

    String getText(Update update);

    int getMessageId(Update update);

    long getUserId(Update update);

    String getPhoneNumber(Update update);

    int getDate(Update update);
}
