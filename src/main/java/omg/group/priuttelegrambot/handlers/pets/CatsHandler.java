package omg.group.priuttelegrambot.handlers.pets;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import jakarta.validation.constraints.NotNull;
import omg.group.priuttelegrambot.entity.pets.Cat;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;

import java.util.List;

public interface CatsHandler {

    InlineKeyboardMarkup formPriutMainMenuButton();

    InlineKeyboardMarkup formInlineKeyboardForInfoMenuButton();

    InlineKeyboardMarkup formInlineKeyboardForTakeMenuButton();

    InlineKeyboardMarkup formInlineKeyboardForSendReportButton();

    void executeButtonOrCommand(Update update, InlineKeyboardMarkup inlineKeyboardMarkup);

    void newOwnerRegister(Update update);

    OwnerCat checkForOwnerExist(Update update);

    List<Cat> checkForOwnerHasCat(OwnerCat ownerCat);

    List<Cat> checkForCatsOnProbation(List<Cat> cats);

    Boolean checkForCatsOnProbationMoreThanOne(List<Cat> cats);

    List<Cat> returnCatsOnProbation(Update update);

    Cat returnOneCatOnProbation(Update update);

    Boolean isReportExist(Update update);

    void receivePhoto(Update update);

    void receiveRation(Update update);

    void receiveFeeling(Update update);

    void receiveChanges(Update update);

    boolean checkForProbationPeriodSetAndValid(@NotNull Update update);

}
