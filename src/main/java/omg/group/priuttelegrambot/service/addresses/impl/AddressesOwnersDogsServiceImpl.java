package omg.group.priuttelegrambot.service.addresses.impl;

import omg.group.priuttelegrambot.dto.addresses.AddressOwnerDogDto;
import omg.group.priuttelegrambot.dto.addresses.AddressOwnerDogMapper;
import omg.group.priuttelegrambot.entity.addresses.AddressOwnerDog;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.exception.NotFoundException;
import omg.group.priuttelegrambot.repository.addresses.AddressessOwnersDogsRepository;
import omg.group.priuttelegrambot.repository.owners.OwnersDogsRepository;
import omg.group.priuttelegrambot.service.addresses.AddressesOwnersDogsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AddressesOwnersDogsServiceImpl implements AddressesOwnersDogsService {

    private final OwnersDogsRepository ownersDogsRepository;
    private final AddressessOwnersDogsRepository addressessOwnersDogsRepository;

    public AddressesOwnersDogsServiceImpl(OwnersDogsRepository ownersDogsRepository,
                                          AddressessOwnersDogsRepository addressessOwnersDogsRepository) {
        this.ownersDogsRepository = ownersDogsRepository;
        this.addressessOwnersDogsRepository = addressessOwnersDogsRepository;
    }

    @Override
    @Transactional
    public AddressOwnerDogDto getAddressByOwnerId(Long ownerId) {
        Optional<OwnerDog> ownerOptional = ownersDogsRepository.findById(ownerId);
        if (ownerOptional.isPresent()) {
            OwnerDog owner = ownerOptional.get();
            AddressOwnerDog address = owner.getAddress();
            if (address == null) {
                throw new NotFoundException(String.format("Данному клиенту %s %s не установлен адрес", owner.getName(), owner.getSurname()));
            } else {
                return AddressOwnerDogMapper.mapToDto(address);
            }
        } else {
            throw new NotFoundException(String.format("Владелец с таким id %d не найден ", ownerId));
        }
    }

    @Override
    @Transactional
    public Long setAddressToOwner(Long ownerId, AddressOwnerDogDto addressDto) {
        Optional<OwnerDog> ownerOptional = ownersDogsRepository.findById(ownerId);
        if (ownerOptional.isPresent()) {
            OwnerDog owner = ownerOptional.get();
            Optional<AddressOwnerDog> addressOptional = addressessOwnersDogsRepository.findByCityAndStreetContainingAndHouseAndLetterAndBuildingAndFlat(
                    addressDto.getCity(), addressDto.getStreet(), addressDto.getHouse(),
                    addressDto.getLetter(), addressDto.getBuilding(), addressDto.getFlat()
            );
            if (addressOptional.isEmpty()) {
                AddressOwnerDog address = AddressOwnerDogMapper.mapToEntity(addressDto);
                address = addressessOwnersDogsRepository.save(address);
                owner.setAddress(address);
                ownersDogsRepository.save(owner);
                return address.getId();
            } else {
                AddressOwnerDog address = addressOptional.get();
                owner.setAddress(address);
                owner.setUpdatedAt(LocalDateTime.now());
                ownersDogsRepository.save(owner);
                return address.getId();
            }

        } else {
            throw new NotFoundException(String.format("Пользователь с таким id %d не найден ", ownerId));
        }
    }

    @Override
    @Transactional
    public void updateAddress(Long addressId, AddressOwnerDogDto addressDto) {
        Optional<AddressOwnerDog> addressOptional = addressessOwnersDogsRepository.findById(addressId);
        if (addressOptional.isPresent()) {
            AddressOwnerDog address = addressOptional.get();
            AddressOwnerDogMapper.updateEntityFromDto(addressDto, address);
            addressessOwnersDogsRepository.save(address);
        } else {
            throw new NotFoundException(String.format("Адрес с таким id %d не найден ", addressId));
        }
    }

    @Override
    @Transactional
    public void deleteAddress(Long addressId) {
        Optional<AddressOwnerDog> addressOptional = addressessOwnersDogsRepository.findById(addressId);
        if (addressOptional.isPresent()) {
            AddressOwnerDog address = addressOptional.get();
            addressessOwnersDogsRepository.delete(address);
        } else {
            throw new NotFoundException(String.format("Адрес с таким id %d не найден ", addressId));
        }
    }

}
