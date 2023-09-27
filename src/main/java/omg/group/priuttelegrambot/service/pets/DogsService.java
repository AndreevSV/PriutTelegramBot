package omg.group.priuttelegrambot.service.pets;

import omg.group.priuttelegrambot.dto.pets.DogDto;
import omg.group.priuttelegrambot.entity.pets.Dog;
import omg.group.priuttelegrambot.entity.pets.petsenum.DogsBreed;
import omg.group.priuttelegrambot.entity.pets.petsenum.Sex;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface DogsService {

    void add(DogDto dogDto);

    void updateById(Long id, DogDto dogDto);

    DogDto findById(Long id);

    List<DogDto> findByIdContaining(Long id);

    List<DogDto> findBySex(Sex sex);

    List<DogDto> findByNickname(String nickname);

    List<DogDto> findByBreed(DogsBreed breed);

    List<DogDto> findByBirthdayBetweenDates(Date startDate, Date endDate);

    List<DogDto> getAll();

    void deleteById(Long id);

    default Dog mapDogFromDogDto(DogDto dogDto) {

        Dog dog = new Dog();

        dog.setAnimalType(dogDto.getAnimalType());
        dog.setSex(dogDto.getSex());
        dog.setNickName(dogDto.getNickName());
        dog.setBirthday(dogDto.getBirthday());
        dog.setBreed(dogDto.getBreed());
        dog.setDisabilities(dogDto.getDisabilities());
        dog.setDescription(dogDto.getDescription());
        dog.setCreatedAt(dogDto.getCreatedAt());
        dog.setUpdatedAt(dogDto.getUpdatedAt());
        dog.setDateOutcome(dogDto.getDateOutcome());
        dog.setPhotoPath(dogDto.getPhotoPath());
        dog.setVolunteer(dogDto.getVolunteer());
        dog.setOwner(dogDto.getOwner());

        return dog;
    }

    default DogDto mapDogDtoFromDog(Dog dog) {

        DogDto dogDto = new DogDto();

        dogDto.setId(dog.getId());
        dogDto.setAnimalType(dog.getAnimalType());
        dogDto.setSex(dog.getSex());
        dogDto.setNickName(dog.getNickName());
        dogDto.setBirthday(dog.getBirthday());
        dogDto.setBreed(dog.getBreed());
        dogDto.setDisabilities(dog.getDisabilities());
        dogDto.setDescription(dog.getDescription());
        dogDto.setPhotoPath(dog.getPhotoPath());
        dogDto.setVolunteer(dog.getVolunteer());
        dogDto.setOwner(dog.getOwner());

        return dogDto;
    }

    @Transactional
    void setFirstProbationPeriod(Long petId, LocalDate date);

    @Transactional
    LocalDate setSecondProbationPeriod(Long petId);

    @Transactional
    void setSecondProbationPassed(Long petId);

    @Transactional
    void setProbationPassed(Long petId);

    List<DogDto> getAllPetsOnProbationToday();
}
