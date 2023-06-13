package omg.group.priuttelegrambot.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.dto.animals.CatDto;
import omg.group.priuttelegrambot.entity.animals.Cat;
import omg.group.priuttelegrambot.repository.CatsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
public class CatsService {

    private final CatsRepository catsRepository;

    public HttpStatus save(CatDto catDto) {
        Cat cat = constructCat(catDto);
        catsRepository.save(cat);
        return HttpStatus.CREATED;
    }

    public HttpStatus delete(CatDto catDto) {
        Cat cat = constructCat(catDto);
        catsRepository.delete(cat);
        return HttpStatus.NO_CONTENT;
    }

    public Optional<CatDto> findById(Long id) {

        Optional<Cat> cat = catsRepository.findById(id);

        CatDto catDto = new CatDto();

        catDto.setId(cat.get().getId());
        catDto.setAnimalType(cat.get().getAnimalType());
        catDto.setSex(cat.get().getSex());
        catDto.setNickName(cat.get().getNickName());
        catDto.setBirthday(cat.get().getBirthday());
        catDto.setBreed(cat.get().getBreed());
        catDto.setDisabilities(cat.get().getDisabilities());
        catDto.setDescription(cat.get().getDescription());
        catDto.setPhotoPath(cat.get().getPhotoPath());
        catDto.setVolunteer(cat.get().getVolunteer());
        catDto.setOwner(cat.get().getOwnerCat());

        return Optional.of(catDto);
    }

    public HttpStatus deleteById(Long id) {
        catsRepository.deleteById(id);
        return HttpStatus.OK;
    }

    private Cat constructCat(CatDto catDto) {
        Cat cat = new Cat();
        cat.setAnimalType(catDto.getAnimalType());
        cat.setSex(catDto.getSex());
        cat.setNickName(catDto.getNickName());
        cat.setBirthday(catDto.getBirthday());
        cat.setBreed(catDto.getBreed());
        cat.setDisabilities(catDto.getDisabilities());
        cat.setDescription(catDto.getDescription());
        cat.setCreatedAt(LocalDateTime.now());
        cat.setPhotoPath(catDto.getPhotoPath());

        return cat;
    }

}
