package omg.group.priuttelegrambot.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.dto.animals.CatDto;
import omg.group.priuttelegrambot.entity.animals.Cat;
import omg.group.priuttelegrambot.entity.animals.enimalsenum.CatsBreed;
import omg.group.priuttelegrambot.entity.animals.enimalsenum.Sex;
import omg.group.priuttelegrambot.repository.CatsRepository;
import omg.group.priuttelegrambot.service.CatsService;
import org.springframework.http.HttpStatus;
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
    public HttpStatus add(CatDto catDto) {

        Cat cat = constructCatFromCatDto(catDto);
        cat.setCreatedAt(LocalDateTime.now());

        catsRepository.save(cat);
        return HttpStatus.CREATED;
    }

    @Override
    public HttpStatus updateById(Long id, CatDto catDto) {

        Cat cat = constructCatFromCatDto(catDto);
        cat.setUpdatedAt(LocalDateTime.now());

        if (catsRepository.existsById(id)) {
            catsRepository.save(cat);
            return HttpStatus.OK;
        } else {
            throw new RuntimeException(String.format("Кот/кошка с id %d не найден", id));
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
            throw new NullPointerException(String.format("Кот/кошка с id %d не найден", id));
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
    public HttpStatus deleteById(Long id) {
        if (catsRepository.existsById(id)) {
            catsRepository.deleteById(id);
            return HttpStatus.NO_CONTENT;
        } else {
            throw new NullPointerException(String.format("Кот/кошка с id %d не найден", id));
        }
    }

}
