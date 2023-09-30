package omg.group.priuttelegrambot.service.addresses.impl;

import omg.group.priuttelegrambot.dto.addresses.AddressOwnerCatDto;
import omg.group.priuttelegrambot.dto.addresses.AddressOwnerCatsMapper;
import omg.group.priuttelegrambot.entity.addresses.AddressOwnerCat;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.exception.NotFoundException;
import omg.group.priuttelegrambot.repository.addresses.AddressessOwnersCatsRepository;
import omg.group.priuttelegrambot.repository.owners.OwnersCatsRepository;
import omg.group.priuttelegrambot.service.addresses.AddressesOwnersCatsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AddressesOwnersCatsServiceImpl implements AddressesOwnersCatsService {

    private final AddressessOwnersCatsRepository addressessOwnersCatsRepository;
    private final OwnersCatsRepository ownersCatsRepository;

    public AddressesOwnersCatsServiceImpl(AddressessOwnersCatsRepository addressessOwnersCatsRepository, OwnersCatsRepository ownersCatsRepository) {
        this.addressessOwnersCatsRepository = addressessOwnersCatsRepository;
        this.ownersCatsRepository = ownersCatsRepository;
    }

    @Override
    @Transactional
    public AddressOwnerCatDto getAddressByOwnerId(Long ownerId) {
        Optional<OwnerCat> ownerOptional = ownersCatsRepository.findById(ownerId);
        if (ownerOptional.isPresent()) {
            OwnerCat owner = ownerOptional.get();
            AddressOwnerCat address = owner.getAddress();
            if (address == null) {
                throw new NotFoundException(String.format("Данному клиенту %s %s не установлен адрес", owner.getName(), owner.getSurname()));
            } else {
                return AddressOwnerCatsMapper.mapToDto(address);
            }
        } else {
            throw new NotFoundException(String.format("Владелец с таким id %d не найден ", ownerId));
        }
    }

    @Override
    @Transactional
    public Long setAddressToOwner(Long ownerId, AddressOwnerCatDto addressDto) {
        Optional<OwnerCat> ownerOptional = ownersCatsRepository.findById(ownerId);
        if (ownerOptional.isPresent()) {
            OwnerCat owner = ownerOptional.get();
            Optional<AddressOwnerCat> addressOptional = addressessOwnersCatsRepository.findByCityAndStreetContainingAndHouseAndLetterAndBuildingAndFlat(
                    addressDto.getCity(), addressDto.getStreet(), addressDto.getHouse(),
                    addressDto.getLetter(), addressDto.getBuilding(), addressDto.getFlat()
            );
            if (addressOptional.isEmpty()) {
                AddressOwnerCat address = AddressOwnerCatsMapper.mapToEntity(addressDto);
                address = addressessOwnersCatsRepository.save(address);
                owner.setAddress(address);
                ownersCatsRepository.save(owner);
                return address.getId();
            } else {
                AddressOwnerCat address = addressOptional.get();
                owner.setAddress(address);
                owner.setUpdatedAt(LocalDateTime.now());
                ownersCatsRepository.save(owner);
                return address.getId();
            }
        } else {
            throw new NotFoundException(String.format("Пользователь с таким id %d не найден ", ownerId));
        }
    }

    @Override
    @Transactional
    public void updateAddress(Long addressId, AddressOwnerCatDto addressDto) {
        Optional<AddressOwnerCat> addressOptional = addressessOwnersCatsRepository.findById(addressId);
        if (addressOptional.isPresent()) {
            AddressOwnerCat address = addressOptional.get();
            AddressOwnerCatsMapper.updateEntityFromDto(addressDto, address);
            addressessOwnersCatsRepository.save(address);
        } else {
            throw new NotFoundException(String.format("Адрес с таким id %d не найден ", addressId));
        }
    }

    @Override
    @Transactional
    public void deleteAddress(Long addressId) {
        Optional<AddressOwnerCat> addressOptional = addressessOwnersCatsRepository.findById(addressId);
        if (addressOptional.isPresent()) {
            AddressOwnerCat address = addressOptional.get();
            addressessOwnersCatsRepository.delete(address);
        } else {
            throw new NotFoundException(String.format("Адрес с таким id %d не найден ", addressId));
        }
    }
}
