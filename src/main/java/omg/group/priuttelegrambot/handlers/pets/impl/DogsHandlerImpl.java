package omg.group.priuttelegrambot.handlers.pets.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.entity.pets.Dog;
import omg.group.priuttelegrambot.handlers.owners.OwnersDogsHandler;
import omg.group.priuttelegrambot.handlers.pets.DogsHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DogsHandlerImpl implements DogsHandler {

    private final TelegramBot telegramBot;
    private final OwnersDogsHandler ownersDogsHandler;

    public DogsHandlerImpl(TelegramBot telegramBot, OwnersDogsHandler ownersDogsHandler) {
        this.telegramBot = telegramBot;
        this.ownersDogsHandler = ownersDogsHandler;
    }

    /**
     * Method checks if Dog(s) on the probation period
     */
    @Override
    public List<Dog> checkForDogsOnProbation(List<Dog> dogs) {

        if (!dogs.isEmpty()) {

            List<Dog> dogsOnProbation = new ArrayList<>();

            for (Dog dog : dogs) {
                if ((dog.getFirstProbation() != null && dog.getFirstProbation().equals(true)) ||
                        (dog.getSecondProbation() != null && dog.getSecondProbation().equals(true))) {
                    dogsOnProbation.add(dog);
                }
            }
            return dogsOnProbation;
        }
        return new ArrayList<>();
    }

    /**
     * Method checks if quantity Dog(s) in the probation period more than 1, then - true
     */
    @Override
    public boolean checkForDogsOnProbationMoreThanOne(List<Dog> dogs) {
        return dogs.size() > 1;
    }

    /**
     * Method checks if owner exist, owner has a dog(s), dog(s) on a probation period and returns
     * list of dogs on a probation
     */
    @Override
    public List<Dog> returnDogsOnProbation(Update update) {

        OwnerDog ownerDog = ownersDogsHandler.checkForOwnerExist(update);

        if (ownerDog != null) {
            List<Dog> dogs = ownersDogsHandler.checkForOwnerHasDog(ownerDog);
            if (!dogs.isEmpty()) {
                List<Dog> dogsInProbation = checkForDogsOnProbation(dogs);
                if (!dogsInProbation.isEmpty()) {
                    return dogsInProbation;
                }
            }
        }
        return new ArrayList<>();
    }

    /**
     * Method returns one animal on probation
     */
    @Override
    public Dog returnOneDogOnProbation(Update update) {

        Long chatId = 0L;
        String text = "";

        if (update.message() != null) {
            chatId = update.message().chat().id();
            text = update.message().text();
        } else if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
        }

        List<Dog> dogs = returnDogsOnProbation(update);

        if (!checkForDogsOnProbationMoreThanOne(dogs) && !dogs.isEmpty()) {

            return dogs.get(0);

        } else if (checkForDogsOnProbationMoreThanOne(dogs) && update.message().text().isEmpty()) {

            Long id;
            String nickName;

            StringBuilder stringBuilder = new StringBuilder();

            String startMessage = """
                    Вы на испытательном сроке
                    со следующими собаками:
                    """;

            for (Dog dog : dogs) {
                id = dog.getId();
                nickName = dog.getNickName();
                String string = String.format("""
                        Номер %d, кличка %s
                        """, id, nickName);
                stringBuilder.append(string);
            }

            SendMessage message = new SendMessage(chatId,
                    startMessage + "\n " + stringBuilder +
                            "\n Отправьте номер или кличку собаки," +
                            "отчет по которой вы хотите отправить:");
            telegramBot.execute(message);

        } else if (checkForDogsOnProbationMoreThanOne(dogs) && !update.message().text().isEmpty()) {

            Long idDog = Long.valueOf(text);

            boolean found = false;

            for (Dog dog : dogs) {
                if (dog.getId().equals(idDog) || dog.getNickName().equals(text)) {
                    return dog;
                }
            }

            if (!found) {
                SendMessage message = new SendMessage(chatId, """
                        Введенный вами номер животного
                        или кличка не верны. Попробуйте
                        ввести снова ЛИБО номер ЛИБО кличку.
                        """);
                telegramBot.execute(message);
            }
        } else {
            SendMessage message = new SendMessage(chatId, """
                    У вас нет животного на испытательном
                    сроке, вы не можете ничего отправить.
                    """);
            telegramBot.execute(message);
        }
        return null;
    }
}
