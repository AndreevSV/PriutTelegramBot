package omg.group.priuttelegrambot.repository;

import omg.group.priuttelegrambot.dto.AnimalDto;
import omg.group.priuttelegrambot.entity.animals.Cat;

public interface AnimalsRepository {

    void save(Cat cat);
}
