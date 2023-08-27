package omg.group.priuttelegrambot.service.owners.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.entity.pets.Cat;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.repository.pets.CatsRepository;
import omg.group.priuttelegrambot.repository.owners.OwnersCatsRepository;
import omg.group.priuttelegrambot.service.owners.OwnersCatsService;
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
    private final CatsRepository catsRepository;

    @Override
    public void add(OwnerCatDto ownerCatDto) {

        OwnerCat owner = constructOwner(ownerCatDto);
        owner.setCreatedAt(LocalDateTime.now());

        ownersCatsRepository.save(owner);
    }

    @Override
    public void updateById(Long id, OwnerCatDto ownerCatDto) {

        OwnerCat owner = constructOwner(ownerCatDto);
        owner.setUpdatedAt(LocalDateTime.now());

        if (ownersCatsRepository.existsById(id)) {
            ownersCatsRepository.save(owner);
        } else {
            System.out.println((String.format("Клиент с id %d не найден", id)));
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
            System.out.println((String.format("Клиент с id %d не найден", id)));
            return null;
        }
    }

    @Override
    public Boolean findByChatId(Long chatId) {

        Optional<OwnerCat> ownerCatOptional = ownersCatsRepository.findByChatId(chatId);

        return ownerCatOptional.isPresent();
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
    public void deleteById(Long id) {
        if (ownersCatsRepository.existsById(id)) {
            ownersCatsRepository.deleteById(id);
        } else {
            System.out.println((String.format("Клиент с id %d не найден", id)));
        }
    }

    /**
     *
     * @return free volunteer (if volunteer do not have a chat) or "null" if no free volunteer
     */
    @Override
    public OwnerCatDto findCatsVolunteer() {

        Optional<OwnerCat> volunteer = ownersCatsRepository.findVolunteerByVolunteerIsTrueAndNoChatsOpened();

        if (volunteer.isPresent()) {
            OwnerCat owner = volunteer.get();
            return constructOwnerDto(owner);
        } else {
            return null;
        }
    }

    @Override
    public void setVolunteer(Long id, List<Long> catsIds) {

        OwnerCat owner = ownersCatsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Волонтер с таким id " + id + " не найден."));
        owner.setIsVolunteer(true);
        owner.setUpdatedAt(LocalDateTime.now());
        owner.setVolunteerChatOpened(false);

        List<Cat> cats = catsRepository.findAllById(catsIds);
        for (Cat cat : cats) {
            cat.setOwner(owner);
        }

        owner.setCats(cats);
        ownersCatsRepository.save(owner);
    }
}
