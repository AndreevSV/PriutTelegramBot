package omg.group.priuttelegrambot.handlers.media.impl;

import com.pengrad.telegrambot.model.Update;
import omg.group.priuttelegrambot.handlers.media.OtherMediaHandler;
import org.springframework.stereotype.Service;

@Service
public class OtherMediaHandlerImpl implements OtherMediaHandler {

    @Override
    public String getVideoFileIdFromUpdate(Update update) {
        if (update.message() != null && update.message().video() != null) {
            return update.message().video().fileId();
        }
        return null;
    }

    @Override
    public String getVoiceFileIdFromUpdate(Update update) {
        if (update.message() != null && update.message().voice() != null) {
            return update.message().voice().fileId();
        }
        return null;
    }

    @Override
    public String getAudioFileIdFromUpdate(Update update) {
        if (update.message() != null && update.message().audio() != null) {
            return update.message().audio().fileId();
        }
        return null;
    }

    @Override
    public String getDocumentFileIdFromUpdate(Update update) {
        if (update.message() != null && update.message().document() != null) {
            return update.message().document().fileId();
        }
        return null;
    }

    @Override
    public String getStickerFileIdFromUpdate(Update update) {
        if (update.message() != null && update.message().sticker() != null) {
            return update.message().sticker().fileId();
        }
        return null;
    }

}
