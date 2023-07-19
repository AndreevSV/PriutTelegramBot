package omg.group.priuttelegrambot.handlers.pets;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import omg.group.priuttelegrambot.entity.pets.Dog;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface DogsHandler {

    InlineKeyboardMarkup formPriutMainMenuButton();

    InlineKeyboardMarkup formInlineKeyboardForInfoMenuButton();

    InlineKeyboardMarkup formInlineKeyboardForTakeMenuButton();

    InlineKeyboardMarkup formInlineKeyboardForSendReportButton();

    void executeButtonOrCommand(Update update, InlineKeyboardMarkup inlineKeyboardMarkup);

    void newOwnerRegister(@NotNull Update update);

    OwnerDog checkForOwnerExist(Update update);

    List<Dog> checkForOwnerHasDog(OwnerDog ownerDog);

    List<Dog> checkForDogsOnProbation(List<Dog> dogs);

    Boolean checkForDogsOnProbationMoreThanOne(List<Dog> dogs);

    List<Dog> returnDogsOnProbation(Update update);

    Dog returnOneDogOnProbation(Update update);

    Boolean isReportExist(Update update);

    void receivePhoto(Update update);

    void receiveRation(Update update);

    void receiveFeeling(Update update);

    void receiveChanges(Update update);

    boolean checkForProbationPeriodSetAndValid(@NotNull Update update);
}
