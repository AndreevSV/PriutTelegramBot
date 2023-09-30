package omg.group.priuttelegrambot.dto.addresses;

import omg.group.priuttelegrambot.dto.owners.OwnerCatMapper;
import omg.group.priuttelegrambot.entity.addresses.AddressOwnerCat;
import omg.group.priuttelegrambot.exception.BadRequestException;

import java.util.stream.Collectors;

public class AddressOwnerCatsMapper {

    public static AddressOwnerCat mapToEntity(AddressOwnerCatDto addressDto) {
        if (addressDto != null) {
            return AddressOwnerCat.builder()
                    .index(addressDto.getIndex())
                    .country(addressDto.getCountry())
                    .region(addressDto.getRegion())
                    .city(addressDto.getCity())
                    .street(addressDto.getStreet())
                    .house(addressDto.getHouse())
                    .letter(addressDto.getLetter())
                    .building(addressDto.getBuilding())
                    .flat(addressDto.getFlat())
                    .clientsCats(
                            addressDto
                                    .getOwnersDtoList()
                                    .stream()
                                    .map(OwnerCatMapper::toEntity)
                                    .collect(Collectors.toList()))
                    .build();
        } else {
            throw new BadRequestException("В переданном объекте addressOwnerDto нет данных.");
        }
    }

    public static AddressOwnerCatDto mapToDto(AddressOwnerCat address) {
        if (address != null) {
            return AddressOwnerCatDto.builder()
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
                            .getClientsCats()
                            .stream()
                            .map(OwnerCatMapper::toDto)
                            .collect(Collectors.toList()))
                    .build();
        } else {
            throw new BadRequestException("В переданном объекте addressOwner нет данных.");
        }
    }

    public static void updateEntityFromDto(AddressOwnerCatDto addressDto, AddressOwnerCat address) {

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
