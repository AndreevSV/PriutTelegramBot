package omg.group.priuttelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.repository.OwnersCatsRepository;
import omg.group.priuttelegrambot.service.OwnersCatsService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OwnersCatsServiceImpl implements OwnersCatsService {

    private final OwnersCatsRepository ownersCatsRepository;

    @Override
    public HttpStatus add(OwnerCatDto ownerCatDto) {

        OwnerCat owner = constructOwner(ownerCatDto);
        owner.setCreatedAt(LocalDateTime.now());

        ownersCatsRepository.save(owner);
        return HttpStatus.CREATED;
    }

    @Override
    public HttpStatus updateById(Long id, OwnerCatDto ownerCatDto) {

        OwnerCat owner = constructOwner(ownerCatDto);
        owner.setUpdatedAt(LocalDateTime.now());

        if (ownersCatsRepository.existsById(id)) {
            ownersCatsRepository.save(owner);
            return HttpStatus.OK;
        } else {
            throw new RuntimeException(String.format("Клиент с id %d не найден", id));
        }
    }

    @Override
    public List<OwnerCatDto> findById(Long id) {

        Optional<OwnerCat> ownerCatOptional = ownersCatsRepository.findById(id);

        if (ownerCatOptional.isPresent()) {
            OwnerCat owner = ownerCatOptional.get();
            OwnerCatDto ownerDto = constructOwnerDto(owner);
            return Collections.singletonList(ownerDto);
        } else {
            throw new NullPointerException(String.format("Клиент с id %d не найден", id));
        }
    }

    @Override
    public List<OwnerCatDto> findByUsername(String username) {
        List<OwnerCat> ownerList = ownersCatsRepository.findByUserNameContainingIgnoreCase(username);
        return ownerList.stream()
                .map(this::constructOwnerDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OwnerCatDto> findBySurname(String surname) {
        List<OwnerCat> ownerList = ownersCatsRepository.findBySurnameContainingIgnoreCase(surname);
        return ownerList.stream()
                .map(this::constructOwnerDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OwnerCatDto> findByTelephone(String telephone) {
        List<OwnerCat> ownerList = ownersCatsRepository.findByTelephoneContainingIgnoreCase(telephone);
        return ownerList.stream()
                .map(this::constructOwnerDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OwnerCatDto> getAll() {
        List<OwnerCat> ownerList = ownersCatsRepository.findAll();
        return ownerList.stream()
                .map(this::constructOwnerDto)
                .collect(Collectors.toList());
    }

    @Override
    public HttpStatus deleteById(Long id) {
        if (ownersCatsRepository.existsById(id)) {
            ownersCatsRepository.deleteById(id);
            return HttpStatus.NO_CONTENT;
        } else {
            throw new NullPointerException(String.format("Клиент с id %d не найден", id));
        }
    }

}