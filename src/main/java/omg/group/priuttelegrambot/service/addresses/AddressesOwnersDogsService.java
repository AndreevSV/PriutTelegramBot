package omg.group.priuttelegrambot.service.addresses;

import omg.group.priuttelegrambot.dto.addresses.AddressOwnerDogDto;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.transaction.annotation.Transactional;

public interface AddressesOwnersDogsService {
    @Transactional
    AddressOwnerDogDto getAddressByOwnerId(Long ownerId);

    @Transactional
    Long setAddressToOwner(Long ownerId, AddressOwnerDogDto addressDto);

    @Transactional
    void updateAddress(Long addressId, AddressOwnerDogDto addressDto);

    @Transactional
    @Cascade(value = CascadeType.REMOVE)
    void deleteAddress(Long addressId);
}
