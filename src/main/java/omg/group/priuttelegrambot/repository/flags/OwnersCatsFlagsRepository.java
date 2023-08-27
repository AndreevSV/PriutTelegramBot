package omg.group.priuttelegrambot.repository.flags;

import omg.group.priuttelegrambot.entity.flags.OwnersCatsFlags;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnersCatsFlagsRepository extends JpaRepository<OwnersCatsFlags, Long> {

    Optional<OwnersCatsFlags> findOwnersCatsFlagsByOwner(OwnerCat owner);

    Optional<OwnersCatsFlags> findOwnersCatsFlagsByVolunteer(OwnerCat owner);

}
