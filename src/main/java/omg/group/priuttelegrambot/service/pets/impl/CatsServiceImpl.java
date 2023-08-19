package omg.group.priuttelegrambot.service.pets.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.dto.pets.CatDto;
import omg.group.priuttelegrambot.entity.pets.Cat;
import omg.group.priuttelegrambot.entity.pets.petsenum.CatsBreed;
import omg.group.priuttelegrambot.entity.pets.petsenum.Sex;
import omg.group.priuttelegrambot.repository.pets.CatsRepository;
import omg.group.priuttelegrambot.service.pets.CatsService;
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
public class CatsServiceImpl implements CatsService {

    private final CatsRepository catsRepository;

    @Override
    public void add(CatDto catDto) {

        Cat cat = constructCatFromCatDto(catDto);
        cat.setCreatedAt(LocalDateTime.now());

        catsRepository.save(cat);
    }

    @Override
    public void updateById(Long id, CatDto catDto) {

        Cat cat = constructCatFromCatDto(catDto);
        cat.setUpdatedAt(LocalDateTime.now());

        if (catsRepository.existsById(id)) {
            catsRepository.save(cat);
        } else {
            System.out.println((String.format("Кот/кошка с id %d не найден", id)));
        }
    }

    @Override
    public List<CatDto> findById(Long id) {

        Optional<Cat> сatOptional = catsRepository.findById(id);

        if (сatOptional.isPresent()) {
            Cat cat = сatOptional.get();
            CatDto catDto = constructCatDtoFromCat(cat);
            return Collections.singletonList(catDto);
        } else {
            System.out.println((String.format("Кот/кошка с id %d не найден", id)));
            return null;
        }
    }

    @Override
    public List<CatDto> findBySex(Sex sex) {
        List<Cat> catsList = catsRepository.findBySex(sex);
        return catsList.stream()
                .map(this::constructCatDtoFromCat)
                .collect(Collectors.toList());
    }

    @Override
    public List<CatDto> findByNickname(String nickname) {
        List<Cat> catsList = catsRepository.findByNickNameContainingIgnoreCase(nickname);
        return catsList.stream()
                .map(this::constructCatDtoFromCat)
                .collect(Collectors.toList());
    }

    @Override
    public List<CatDto> findByBreed(CatsBreed breed) {
        List<Cat> catsList = catsRepository.findByBreedContaining(breed);
        return catsList.stream()
                .map(this::constructCatDtoFromCat)
                .collect(Collectors.toList());
    }


    @Override
    public List<CatDto> findByBirthdayBetweenDates(Date startDate, Date endDate) {
        List<Cat> catsList = catsRepository.findByBirthdayBetween(startDate, endDate);
        return catsList.stream()
                .map(this::constructCatDtoFromCat)
                .collect(Collectors.toList());
    }

    @Override
    public List<CatDto> getAll() {
        List<Cat> catsList = catsRepository.findAll();
        return catsList.stream()
                .map(this::constructCatDtoFromCat)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        if (catsRepository.existsById(id)) {
            catsRepository.deleteById(id);
        } else {
            System.out.println((String.format("Кот/кошка с id %d не найден", id)));
        }
    }

}
