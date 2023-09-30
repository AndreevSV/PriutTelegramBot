package omg.group.priuttelegrambot.handlers.media;

import com.pengrad.telegrambot.model.Update;

public interface PhotoHandler {
    String getFileIdFromUpdate(Update update);

    String getFilePathByFileId(String fileId);

    String downloadFileByFileId(String fileId);

    boolean isPhoto(String fileNameWithExtension);

    byte[] getFileAsArray(String savePath);

    int getHashOfFile(byte[] file);
}
