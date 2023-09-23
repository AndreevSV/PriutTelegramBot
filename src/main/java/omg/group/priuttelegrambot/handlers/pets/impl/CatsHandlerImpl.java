package omg.group.priuttelegrambot.handlers.pets.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.dto.pets.CatDto;
import omg.group.priuttelegrambot.handlers.owners.OwnersCatsHandler;
import omg.group.priuttelegrambot.handlers.pets.CatsHandler;
import omg.group.priuttelegrambot.handlers.updates.OwnUpdatesHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CatsHandlerImpl implements CatsHandler {

    private final TelegramBot telegramBot;
    private final OwnersCatsHandler ownersCatsHandler;
    private final OwnUpdatesHandler ownUpdatesHandler;

    public CatsHandlerImpl(TelegramBot telegramBot,
                           OwnersCatsHandler ownersCatsHandler,
                           OwnUpdatesHandler ownUpdatesHandler) {
        this.telegramBot = telegramBot;
        this.ownersCatsHandler = ownersCatsHandler;
        this.ownUpdatesHandler = ownUpdatesHandler;
    }

    /**
     * Method checks if Cat(s) on the probation period
     */
    @Override
    public List<CatDto> checkForCatsOnProbation(List<CatDto> catsDto) {

        if (!catsDto.isEmpty()) {

            List<CatDto> catsOnProbationDto = new ArrayList<>();

            for (CatDto catDto : catsDto) {
                if ((catDto.getFirstProbation() != null && catDto.getFirstProbation().equals(true)) ||
                        ((catDto.getSecondProbation() != null && catDto.getSecondProbation().equals(true)))) {
                    catsOnProbationDto.add(catDto);
                }
            }
            return catsOnProbationDto;
        }
        return null;
    }

    /**
     * Method checks if quantity Cat(s) in the probation period more than 1, then - true
     */
    @Override
    public boolean checkForCatsOnProbationMoreThanOne(List<CatDto> catsDto) {
        return catsDto.size() > 1;
    }

    /**
     * Method checks if owner exist, owner has a cat(s), cat(s) on a probation period and returns
     * list of cats on a probation
     */
    @Override
    public List<CatDto> returnCatsOnProbation(Update update) {

        OwnerCatDto ownerCatDto = ownersCatsHandler.checkForOwnerExist(update);

        if (ownerCatDto != null) {
            List<CatDto> catsDto = ownersCatsHandler.checkForOwnerHasCat(ownerCatDto);
            if (!catsDto.isEmpty()) {
                List<CatDto> catsInProbationDto = checkForCatsOnProbation(catsDto);
                if (!catsInProbationDto.isEmpty()) {
                    return catsInProbationDto;
                }
            }
        }
        return null;
    }

    /**
     * Method returns one animal on probation
     */
    @Override
    public CatDto returnOneCatOnProbation(Update update) {
        Long chatId = ownUpdatesHandler.getChatId(update);
        String text = ownUpdatesHandler.getText(update);

        List<CatDto> catsDto = returnCatsOnProbation(update);

        if (!checkForCatsOnProbationMoreThanOne(catsDto) && !catsDto.isEmpty()) {

            return catsDto.get(0);

        } else if (checkForCatsOnProbationMoreThanOne(catsDto) && update.message().text().isEmpty()) {

            Long id;
            String nickName;

            StringBuilder stringBuilder = new StringBuilder();

            String startMessage = """
                    Вы на испытательном сроке со следующими кошками:
                    """;

            for (CatDto catDto : catsDto) {
                id = catDto.getId();
                nickName = catDto.getNickName();
                String string = String.format("""
                        Номер: %d, кличка: %s
                        """, id, nickName);
                stringBuilder.append(string);
            }

            SendMessage message = new SendMessage(chatId,
                    startMessage + "\n " + stringBuilder +
                            "\n Отправьте номер или кличку кошки," +
                            "отчет по которой вы хотите отправить:");
            telegramBot.execute(message);

        } else if (checkForCatsOnProbationMoreThanOne(catsDto) && !update.message().text().isEmpty()) {

            Long idCat = Long.valueOf(text);

            boolean found = false;

            for (CatDto catDto : catsDto) {
                if (catDto.getId().equals(idCat) || catDto.getNickName().equals(text)) {
                    return catDto;
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
