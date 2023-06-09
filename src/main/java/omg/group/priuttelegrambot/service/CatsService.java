package omg.group.priuttelegrambot.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.dto.CatDto;
import omg.group.priuttelegrambot.entity.animals.Cat;
import omg.group.priuttelegrambot.repository.AnimalsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Data
@RequiredArgsConstructor
public class CatsService {

    private final AnimalsRepository animalsRepository;

    public HttpStatus save(CatDto catDto) {

        Cat cat = new Cat();
        cat.setAnimalType(catDto.getAnimalType());
        cat.setNickName(catDto.getNickName());
        cat.setBirthday(catDto.getBirthday());
        cat.setBreed(catDto.getBreed());
        cat.setDisabilities(catDto.getDisabilities());
        cat.setDescription(catDto.getDescription());
        cat.setCreatedAt(LocalDateTime.now());
        cat.setPhotoPath(catDto.getPhotoPath());

        animalsRepository.save(cat);
        return HttpStatus.CREATED;
    }
}
