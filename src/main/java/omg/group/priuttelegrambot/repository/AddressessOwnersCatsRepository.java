package omg.group.priuttelegrambot.repository;

import omg.group.priuttelegrambot.entity.addresses.AddressesOwnersCats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressessOwnersCatsRepository extends JpaRepository<AddressesOwnersCats, Long> {

}
