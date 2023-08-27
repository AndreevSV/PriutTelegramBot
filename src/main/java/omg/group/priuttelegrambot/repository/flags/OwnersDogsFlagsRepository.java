package omg.group.priuttelegrambot.repository.flags;

import omg.group.priuttelegrambot.entity.flags.OwnersDogsFlags;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnersDogsFlagsRepository extends JpaRepository<OwnersDogsFlags, Long> {

    Optional<OwnersDogsFlags> findOwnersDogsFlagsByOwner(OwnerDog owner);

    Optional<OwnersDogsFlags> findOwnersDogsFlagsByVolunteer(OwnerDog owner);

}
