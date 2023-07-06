package omg.group.priuttelegrambot.service;

import omg.group.priuttelegrambot.dto.animals.CatDto;
import omg.group.priuttelegrambot.entity.animals.Cat;
import omg.group.priuttelegrambot.entity.animals.enimalsenum.CatsBreed;
import omg.group.priuttelegrambot.entity.animals.enimalsenum.Sex;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.List;

public interface CatsService {

    void add(CatDto catDto);

    void updateById(Long id, CatDto catDto);

    List<CatDto> findById(Long id);

    List<CatDto> findBySex(Sex sex);

    List<CatDto> findByNickname(String nickname);

    List<CatDto> findByBreed(CatsBreed breed);

    List<CatDto> findByBirthdayBetweenDates(Date startDate, Date endDate);

    List<CatDto> getAll();

    void deleteById(Long id);

    default Cat constructCatFromCatDto(CatDto catDto) {

        Cat cat = new Cat();

        cat.setAnimalType(catDto.getAnimalType());
        cat.setSex(catDto.getSex());
        cat.setNickName(catDto.getNickName());
        cat.setBirthday(catDto.getBirthday());
        cat.setBreed(catDto.getBreed());
        cat.setDisabilities(catDto.getDisabilities());
        cat.setDescription(catDto.getDescription());
        cat.setCreatedAt(catDto.getCreatedAt());
        cat.setUpdatedAt(catDto.getUpdatedAt());
        cat.setDateOutcome(catDto.getDateOutcome());
        cat.setPhotoPath(catDto.getPhotoPath());
        cat.setVolunteer(catDto.getVolunteer());
        cat.setOwnerCat(catDto.getOwner());

        return cat;
    }

    default CatDto constructCatDtoFromCat(Cat cat) {

        CatDto catDto = new CatDto();

        catDto.setId(cat.getId());
        catDto.setAnimalType(cat.getAnimalType());
        catDto.setSex(cat.getSex());
        catDto.setNickName(cat.getNickName());
        catDto.setBirthday(cat.getBirthday());
        catDto.setBreed(cat.getBreed());
        catDto.setDisabilities(cat.getDisabilities());
        catDto.setDescription(cat.getDescription());
        catDto.setPhotoPath(cat.getPhotoPath());
        catDto.setVolunteer(cat.getVolunteer());
        catDto.setOwner(cat.getOwnerCat());

        return catDto;
    }


}
