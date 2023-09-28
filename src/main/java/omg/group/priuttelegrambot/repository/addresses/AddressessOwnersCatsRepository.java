package omg.group.priuttelegrambot.repository.addresses;

import omg.group.priuttelegrambot.entity.addresses.AddressOwnerCat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressessOwnersCatsRepository extends JpaRepository<AddressOwnerCat, Long> {

    Optional<AddressOwnerCat> findByCityAndStreetContainingAndHouseAndLetterAndBuildingAndFlat(String city, String street, Integer house, char letter, Integer building, Integer flat);

}
