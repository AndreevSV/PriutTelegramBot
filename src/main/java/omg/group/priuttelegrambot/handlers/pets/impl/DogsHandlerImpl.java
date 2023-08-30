package omg.group.priuttelegrambot.handlers.pets.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;
import omg.group.priuttelegrambot.dto.pets.DogDto;
import omg.group.priuttelegrambot.handlers.owners.OwnersDogsHandler;
import omg.group.priuttelegrambot.handlers.pets.DogsHandler;
import omg.group.priuttelegrambot.handlers.updates.OwnUpdatesHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DogsHandlerImpl implements DogsHandler {

    private final TelegramBot telegramBot;
    private final OwnersDogsHandler ownersDogsHandler;
    private final OwnUpdatesHandler ownUpdatesHandler;

    public DogsHandlerImpl(TelegramBot telegramBot,
                           OwnersDogsHandler ownersDogsHandler,
                           OwnUpdatesHandler ownUpdatesHandler) {
        this.telegramBot = telegramBot;
        this.ownersDogsHandler = ownersDogsHandler;
        this.ownUpdatesHandler = ownUpdatesHandler;
    }

    /**
     * Method checks if Dog(s) on the probation period
     */
    @Override
    public List<DogDto> checkForDogsOnProbation(List<DogDto> dogsDto) {

        if (!dogsDto.isEmpty()) {

            List<DogDto> dogsOnProbationDto = new ArrayList<>();

            for (DogDto dogDto : dogsDto) {
                if ((dogDto.getFirstProbation() != null && dogDto.getFirstProbation().equals(true)) ||
                        (dogDto.getSecondProbation() != null && dogDto.getSecondProbation().equals(true))) {
                    dogsOnProbationDto.add(dogDto);
                }
            }
            return dogsOnProbationDto;
        }
        return null;
    }

    /**
     * Method checks if quantity Dog(s) in the probation period more than 1, then - true
     */
    @Override
    public boolean checkForDogsOnProbationMoreThanOne(List<DogDto> dogsDto) {
        return dogsDto.size() > 1;
    }

    /**
     * Method checks if owner exist, owner has a dog(s), dog(s) on a probation period and returns
     * list of dogs on a probation
     */
    @Override
    public List<DogDto> returnDogsOnProbation(Update update) {

        OwnerDogDto ownerDogDto = ownersDogsHandler.checkForOwnerExist(update);

        if (ownerDogDto != null) {
            List<DogDto> dogsDto = ownersDogsHandler.checkForOwnerHasDog(ownerDogDto);
            if (!dogsDto.isEmpty()) {
                List<DogDto> dogsInProbationDto = checkForDogsOnProbation(dogsDto);
                if (!dogsInProbationDto.isEmpty()) {
                    return dogsInProbationDto;
                }
            }
        }
        return null;
    }

    /**
     * Method returns one animal on probation
     */
    @Override
    public DogDto returnOneDogOnProbation(Update update) {
        Long chatId = ownUpdatesHandler.getChatId(update);
        String text = ownUpdatesHandler.getText(update);

        List<DogDto> dogsDto = returnDogsOnProbation(update);

        if (!checkForDogsOnProbationMoreThanOne(dogsDto) && !dogsDto.isEmpty()) {

            return dogsDto.get(0);

        } else if (checkForDogsOnProbationMoreThanOne(dogsDto) && update.message().text().isEmpty()) {

            Long id;
            String nickName;

            StringBuilder stringBuilder = new StringBuilder();

            String startMessage = """
                    Вы на испытательном сроке со следующими собаками:
                    """;

            for (DogDto dogDto : dogsDto) {
                id = dogDto.getId();
                nickName = dogDto.getNickName();
                String string = String.format("""
                        Номер: %d, кличка: %s
                        """, id, nickName);
                stringBuilder.append(string);
            }

            SendMessage message = new SendMessage(chatId,
                    startMessage + "\n " + stringBuilder +
                            "\n Отправьте номер или кличку собаки," +
                            "отчет по которой вы хотите отправить:");
            telegramBot.execute(message);

        } else if (checkForDogsOnProbationMoreThanOne(dogsDto) && !update.message().text().isEmpty()) {

            Long idDog = Long.valueOf(text);

            boolean found = false;

            for (DogDto dogDto : dogsDto) {
                if (dogDto.getId().equals(idDog) || dogDto.getNickName().equals(text)) {
                    return dogDto;
                }
            }

            if (!found) {
                SendMessage message = new SendMessage(chatId, """
                        Введенный вами номер животного или кличка не верны.
                        Попробуйте ввести снова ЛИБО номер ЛИБО кличку.
                        """);
                telegramBot.execute(message);
            }
        } else {
            SendMessage message = new SendMessage(chatId, """
                    У вас нет животного на испытательном сроке, вы не можете ничего отправить.
                    """);
            telegramBot.execute(message);
        }
        return null;
    }
}
