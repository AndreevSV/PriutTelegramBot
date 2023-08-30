package omg.group.priuttelegrambot.handlers.media.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.response.GetFileResponse;
import omg.group.priuttelegrambot.handlers.media.PhotoHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Random;

@Service
public class PhotoHandlerImpl implements PhotoHandler {
    @Value("${file.download.path}")
    private String downloadPath;

    private final TelegramBot telegramBot;

    public PhotoHandlerImpl(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Override
    public String getFileIdFromUpdate(Update update) {
        if (update.message() != null && update.message().photo() != null) {
            PhotoSize[] photos = update.message().photo();
            PhotoSize photo = photos[photos.length - 1];
            return photo.fileId();
        }
        return null;
    }

    @Override
    public String getFilePathByFileId(String fileId) {

        GetFile request = new GetFile(fileId);
        GetFileResponse getFileResponse = telegramBot.execute(request);

        File file = getFileResponse.file();

        return telegramBot.getFullFilePath(file); // "get downloading link like https://api.telegram.org/file/" + token + "/" + file.filePath();
    }

    @Override
    public String downloadFileByFileId(String fileId) {

        String fullPath = getFilePathByFileId(fileId);
        System.out.println("full path to file = " + fullPath);
        String[] pathParts = fullPath.split("/");
        String fileNameWithExtension = pathParts[pathParts.length - 1];
        System.out.println("file name with extension = " + fileNameWithExtension);

        if (isPhoto(fileNameWithExtension)) {
            Random random = new Random();
            int randomNumber = random.nextInt();
            String[] fileParts = fileNameWithExtension.split("\\.");
            String fileName = fileParts[0];
            System.out.println("file name = " + fileName);
            String extension = fileParts[fileParts.length - 1];
            System.out.println("extension = " + extension);
            String newFileNameWithExtension = fileName + randomNumber + "." + extension;

            System.out.println(newFileNameWithExtension);

            String savePath = downloadPath + newFileNameWithExtension;
            System.out.println(savePath);
            try (InputStream in = new URL(fullPath).openStream()) {
                Files.copy(in, Paths.get(savePath), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return savePath;
        }
        return null;
    }

    @Override
    public boolean isPhoto(String fileNameWithExtension) {
        return fileNameWithExtension.contains(".jpg") ||
                fileNameWithExtension.contains(".jpeg") ||
                fileNameWithExtension.contains(".png") ||
                fileNameWithExtension.contains(".ico") ||
                fileNameWithExtension.contains(".gif") ||
                fileNameWithExtension.contains(".tiff") ||
                fileNameWithExtension.contains(".raw") ||
                fileNameWithExtension.contains(".svg");
    }

    @Override
    public byte[] getFileAsArray(String savePath) {
        Path path = Paths.get(savePath);
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }

    @Override
    public int getHashOfFile(byte[] file) {
        return Arrays.hashCode(file);
    }
}
