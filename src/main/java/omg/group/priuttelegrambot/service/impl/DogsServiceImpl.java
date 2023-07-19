package omg.group.priuttelegrambot.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.dto.pets.DogDto;
import omg.group.priuttelegrambot.entity.pets.Dog;
import omg.group.priuttelegrambot.entity.pets.petsenum.DogsBreed;
import omg.group.priuttelegrambot.entity.pets.petsenum.Sex;
import omg.group.priuttelegrambot.repository.pets.DogsRepository;
import omg.group.priuttelegrambot.service.DogsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
@RequiredArgsConstructor
public class DogsServiceImpl implements DogsService {

    private final DogsRepository dogsRepository;

    @Override
    public void add(DogDto dogDto) {

        Dog dog = constructDogFromDogDto(dogDto);
        dog.setCreatedAt(LocalDateTime.now());

        dogsRepository.save(dog);
    }

    @Override
    public void updateById(Long id, DogDto dogDto) {

        Dog dog = constructDogFromDogDto(dogDto);
        dog.setUpdatedAt(LocalDateTime.now());

        if (dogsRepository.existsById(id)) {
            dogsRepository.save(dog);
        } else {
            System.out.println((String.format("Собака с id %d не найдена", id)));
        }
    }

    @Override
    public List<DogDto> findById(Long id) {

        Optional<Dog> dogOptional = dogsRepository.findById(id);

        if (dogOptional.isPresent()) {
            Dog dog = dogOptional.get();
            DogDto dogDto = constructDogDtoFromCat(dog);
            return Collections.singletonList(dogDto);
        } else {
            System.out.println((String.format("Собака с id %d не найден", id)));
            return null;
        }
    }

    @Override
    public List<DogDto> findBySex(Sex sex) {
        List<Dog> dogsList = dogsRepository.findBySex(sex);
        return dogsList.stream()
                .map(this::constructDogDtoFromCat)
                .collect(Collectors.toList());
    }

    @Override
    public List<DogDto> findByNickname(String nickname) {
        List<Dog> dogsList = dogsRepository.findByNickNameContainingIgnoreCase(nickname);
        return dogsList.stream()
                .map(this::constructDogDtoFromCat)
                .collect(Collectors.toList());
    }

    @Override
    public List<DogDto> findByBreed(DogsBreed breed) {
        List<Dog> dogsList = dogsRepository.findByBreedContaining(breed);
        return dogsList.stream()
                .map(this::constructDogDtoFromCat)
                .collect(Collectors.toList());
    }


    @Override
    public List<DogDto> findByBirthdayBetweenDates(Date startDate, Date endDate) {
        List<Dog> dogsList = dogsRepository.findByBirthdayBetween(startDate, endDate);
        return dogsList.stream()
                .map(this::constructDogDtoFromCat)
                .collect(Collectors.toList());
    }

    @Override
    public List<DogDto> getAll() {
        List<Dog> dogsList = dogsRepository.findAll();
        return dogsList.stream()
                .map(this::constructDogDtoFromCat)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        if (dogsRepository.existsById(id)) {
            dogsRepository.deleteById(id);
        } else {
            System.out.println(String.format("Собака с id %d не найден", id));
        }
    }



}