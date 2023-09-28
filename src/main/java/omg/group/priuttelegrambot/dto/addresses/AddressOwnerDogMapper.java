package omg.group.priuttelegrambot.dto.addresses;

import omg.group.priuttelegrambot.dto.owners.OwnerDogMapper;
import omg.group.priuttelegrambot.entity.addresses.AddressOwnerDog;
import omg.group.priuttelegrambot.exception.BadRequestException;

import java.util.stream.Collectors;

public class AddressOwnerDogMapper {

    public static AddressOwnerDog mapToEntity(AddressOwnerDogDto addressDto) {
        if (addressDto != null) {
            return AddressOwnerDog.builder()
                    .index(addressDto.getIndex())
                    .country(addressDto.getCountry())
                    .region(addressDto.getRegion())
                    .city(addressDto.getCity())
                    .street(addressDto.getStreet())
                    .house(addressDto.getHouse())
                    .letter(addressDto.getLetter())
                    .building(addressDto.getBuilding())
                    .flat(addressDto.getFlat())
                    .clientsDogs(
                            addressDto
                                    .getOwnersDtoList()
                                    .stream()
                                    .map(OwnerDogMapper::toEntity)
                                    .collect(Collectors.toList()))
                    .build();
        } else {
            throw new BadRequestException("В переданном объекте addressOwnerDto нет данных.");
        }
    }

    public static AddressOwnerDogDto mapToDto(AddressOwnerDog address) {
        if (address != null) {
            return AddressOwnerDogDto.builder()
                    .index(address.getIndex())
                    .country(address.getCountry())
                    .region(address.getRegion())
                    .city(address.getCity())
                    .street(address.getStreet())
                    .house(address.getHouse())
                    .letter(address.getLetter())
                    .building(address.getBuilding())
                    .flat(address.getFlat())
                    .ownersDtoList(address
                            .getClientsDogs()
                            .stream()
                            .map(OwnerDogMapper::toDto)
                            .collect(Collectors.toList()))
                    .build();
        } else {
            throw new BadRequestException("В переданном объекте addressOwner нет данных.");
        }
    }

    public static void updateEntityFromDto(AddressOwnerDogDto addressDto, AddressOwnerDog address) {

        address.setIndex(addressDto.getIndex());
        address.setCountry(addressDto.getCountry());
        address.setRegion(addressDto.getRegion());
        address.setCity(addressDto.getCity());
        address.setStreet(addressDto.getStreet());
        address.setHouse(addressDto.getHouse());
        address.setLetter(addressDto.getLetter());
        address.setBuilding(addressDto.getBuilding());
        address.setFlat(addressDto.getFlat());
    }


}
