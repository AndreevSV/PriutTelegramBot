package omg.group.priuttelegrambot.handlers.pets;

import com.pengrad.telegrambot.model.Update;
import omg.group.priuttelegrambot.dto.pets.DogDto;

import java.util.List;

public interface DogsHandler {
    List<DogDto> checkForDogsOnProbation(List<DogDto> dogsDto);
    boolean checkForDogsOnProbationMoreThanOne(List<DogDto> dogsDto);
    List<DogDto> returnDogsOnProbation(Update update);
    DogDto returnOneDogOnProbation(Update update);
}
