package omg.group.priuttelegrambot.repository.addresses;

import omg.group.priuttelegrambot.entity.addresses.AddressOwnerDog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressessOwnersDogsRepository extends JpaRepository<AddressOwnerDog, Long> {

    Optional<AddressOwnerDog> findByCityAndStreetContainingAndHouseAndLetterAndBuildingAndFlat(String city, String street, Integer house, char letter, Integer building, Integer flat);

}
