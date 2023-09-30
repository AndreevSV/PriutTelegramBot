package omg.group.priuttelegrambot.service.addresses;

import omg.group.priuttelegrambot.dto.addresses.AddressOwnerCatDto;
import org.springframework.transaction.annotation.Transactional;

public interface AddressesOwnersCatsService {
    @Transactional
    AddressOwnerCatDto getAddressByOwnerId(Long ownerId);

    @Transactional
    Long setAddressToOwner(Long ownerId, AddressOwnerCatDto addressDto);

    @Transactional
    void updateAddress(Long addressId, AddressOwnerCatDto addressDto);

    @Transactional
    void deleteAddress(Long addressId);
}
