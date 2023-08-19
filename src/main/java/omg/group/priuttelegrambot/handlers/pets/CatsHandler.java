package omg.group.priuttelegrambot.handlers.pets;

import com.pengrad.telegrambot.model.Update;
import omg.group.priuttelegrambot.entity.pets.Cat;

import java.util.List;

public interface CatsHandler {
    List<Cat> checkForCatsOnProbation(List<Cat> cats);
    boolean checkForCatsOnProbationMoreThanOne(List<Cat> cats);
    List<Cat> returnCatsOnProbation(Update update);
    Cat returnOneCatOnProbation(Update update);
}
