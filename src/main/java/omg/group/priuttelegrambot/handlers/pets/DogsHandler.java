package omg.group.priuttelegrambot.handlers.pets;

import com.pengrad.telegrambot.model.Update;
import omg.group.priuttelegrambot.entity.pets.Dog;

import java.util.List;

public interface DogsHandler {
    List<Dog> checkForDogsOnProbation(List<Dog> dogs);
    boolean checkForDogsOnProbationMoreThanOne(List<Dog> dogs);
    List<Dog> returnDogsOnProbation(Update update);
    Dog returnOneDogOnProbation(Update update);
}
