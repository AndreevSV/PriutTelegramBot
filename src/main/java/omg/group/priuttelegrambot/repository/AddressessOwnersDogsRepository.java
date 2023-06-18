package omg.group.priuttelegrambot.repository;

import omg.group.priuttelegrambot.entity.addresses.AddressesOwnersDogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressessOwnersDogsRepository extends JpaRepository<AddressesOwnersDogs, Long> {

}
