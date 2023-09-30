package omg.group.priuttelegrambot.handlers.media;

import com.pengrad.telegrambot.model.Update;

public interface OtherMediaHandler {
    String getVideoFileIdFromUpdate(Update update);

    String getVoiceFileIdFromUpdate(Update update);

    String getAudioFileIdFromUpdate(Update update);

    String getDocumentFileIdFromUpdate(Update update);

    String getStickerFileIdFromUpdate(Update update);
}
