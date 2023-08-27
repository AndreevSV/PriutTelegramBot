package omg.group.priuttelegrambot.handlers.pets.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.entity.pets.Cat;
import omg.group.priuttelegrambot.handlers.owners.OwnersCatsHandler;
import omg.group.priuttelegrambot.handlers.pets.CatsHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CatsHandlerImpl implements CatsHandler {

    private final TelegramBot telegramBot;
    private final OwnersCatsHandler ownersCatsHandler;

    public CatsHandlerImpl(TelegramBot telegramBot, OwnersCatsHandler ownersCatsHandler) {
        this.telegramBot = telegramBot;
        this.ownersCatsHandler = ownersCatsHandler;
    }

    /**
     * Method checks if Cat(s) on the probation period
     */
    @Override
    public List<Cat>  checkForCatsOnProbation(List<Cat> cats) {

        if (!cats.isEmpty()) {

            List<Cat> catsOnProbation = new ArrayList<>();

            for (Cat cat : cats) {
                if ((cat.getFirstProbation() != null && cat.getFirstProbation().equals(true)) ||
                        ((cat.getSecondProbation() != null && cat.getSecondProbation().equals(true)))) {
                    catsOnProbation.add(cat);
                }
            }
            return catsOnProbation;
        }
        return new ArrayList<>();
    }

    /**
     * Method checks if quantity Cat(s) in the probation period more than 1, then - true
     */
    @Override
    public boolean checkForCatsOnProbationMoreThanOne(List<Cat> cats) {
        return cats.size() > 1;
    }

    /**
     * Method checks if owner exist, owner has a cat(s), cat(s) on a probation period and returns
     * list of cats on a probation
     */
    @Override
    public List<Cat> returnCatsOnProbation(Update update) {

        OwnerCat ownerCat = ownersCatsHandler.checkForOwnerExist(update);

        if (ownerCat != null) {
            List<Cat> cats = ownersCatsHandler.checkForOwnerHasCat(ownerCat);
            if (!cats.isEmpty()) {
                List<Cat> catsInProbation = checkForCatsOnProbation(cats);
                if (!catsInProbation.isEmpty()) {
                    return catsInProbation;
                }
            }
        }
        return new ArrayList<>();
    }

    /**
     * Method returns one animal on probation
     */
    @Override
    public Cat returnOneCatOnProbation(Update update) {

        Long chatId = 0L;
        String text = "";

        if (update.message() != null) {
            chatId = update.message().chat().id();
            text = update.message().text();
        } else if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
        }

        List<Cat> cats = returnCatsOnProbation(update);

        if (!checkForCatsOnProbationMoreThanOne(cats) && !cats.isEmpty()) {

            return cats.get(0);

        } else if (checkForCatsOnProbationMoreThanOne(cats) && update.message().text().isEmpty()) {

            Long id;
            String nickName;

            StringBuilder stringBuilder = new StringBuilder();

            String startMessage = """
                    Вы на испытательном сроке со следующими кошками:
                    """;

            for (Cat cat : cats) {
                id = cat.getId();
                nickName = cat.getNickName();
                String string = String.format("""
                        Номер %d, кличка %s
                        """, id, nickName);
                stringBuilder.append(string);
            }

            SendMessage message = new SendMessage(chatId,
                    startMessage + "\n " + stringBuilder +
                            "\n Отправьте номер или кличку кошки," +
                            "отчет по которой вы хотите отправить:");
            telegramBot.execute(message);

        } else if (checkForCatsOnProbationMoreThanOne(cats) && !update.message().text().isEmpty()) {

            Long idCat = Long.valueOf(text);

            boolean found = false;

            for (Cat cat : cats) {
                if (cat.getId().equals(idCat) || cat.getNickName().equals(text)) {
                    return cat;
                }
            }

            if (!found) {
                SendMessage message = new SendMessage(chatId, """
                        Введенный вами номер животного или кличка не верны. Попробуйте ввести снова ЛИБО номер ЛИБО кличку.
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
