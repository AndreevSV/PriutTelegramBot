package omg.group.priuttelegrambot.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.dto.animals.DogDto;
import omg.group.priuttelegrambot.entity.animals.Dog;
import omg.group.priuttelegrambot.repository.DogsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
public class DogsService {

    private final DogsRepository dogsRepository;

    public HttpStatus save(DogDto dogDto) {
        Dog dog = constructDog(dogDto);
        dogsRepository.save(dog);
        return HttpStatus.CREATED;
    }

    public HttpStatus delete(DogDto dogDto) {
        Dog dog = constructDog(dogDto);
        dogsRepository.delete(dog);
        return HttpStatus.NO_CONTENT;
    }

    public Optional<Dog> findById(Long id) {
        return dogsRepository.findById(id);
    }

    public HttpStatus deleteById(Long id) {
        dogsRepository.deleteById(id);
        return HttpStatus.OK;
    }

    private Dog constructDog(DogDto dogDto) {
        Dog dog = new Dog();
        dog.setAnimalType(dogDto.getAnimalType());
        dog.setNickName(dogDto.getNickName());
        dog.setBirthday(dogDto.getBirthday());
        dog.setBreed(dogDto.getBreed());
        dog.setDisabilities(dogDto.getDisabilities());
        dog.setDescription(dogDto.getDescription());
        dog.setCreatedAt(LocalDateTime.now());
        dog.setPhotoPath(dogDto.getPhotoPath());

        return dog;
    }

}