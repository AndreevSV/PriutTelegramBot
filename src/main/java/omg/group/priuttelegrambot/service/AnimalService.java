package omg.group.priuttelegrambot.service;

import omg.group.priuttelegrambot.entity.animals.Cat;
import omg.group.priuttelegrambot.repository.AnimalDao;
import org.springframework.stereotype.Service;

@Service
public class AnimalService {

    private final AnimalDao animalDao;

    public AnimalService(AnimalDao animalDao) {
        this.animalDao = animalDao;
    }

    public void save(Cat cat) {
        animalDao.save(cat);

    }
}
