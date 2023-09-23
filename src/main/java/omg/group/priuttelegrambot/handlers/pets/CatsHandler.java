package omg.group.priuttelegrambot.handlers.pets;

import com.pengrad.telegrambot.model.Update;
import omg.group.priuttelegrambot.dto.pets.CatDto;

import java.util.List;

public interface CatsHandler {
    List<CatDto> checkForCatsOnProbation(List<CatDto> catsDto);
    boolean checkForCatsOnProbationMoreThanOne(List<CatDto> catsDto);
    List<CatDto> returnCatsOnProbation(Update update);
    CatDto returnOneCatOnProbation(Update update);
}
