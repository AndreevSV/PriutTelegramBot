package omg.group.priuttelegrambot.repository.flags;

import omg.group.priuttelegrambot.entity.flags.OwnersCatsFlags;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnersCatsFlagsRepository extends JpaRepository<OwnersCatsFlags, Long> {

    Optional<OwnersCatsFlags> findByOwner(OwnerCat owner);

    Optional<OwnersCatsFlags> findByOwnerAndIsWaitingForPhotoIsTrueOrIsWaitingForFeelingIsTrueOrIsWaitingForRationIsTrueOrIsWaitingForChangesIsTrue(OwnerCat owner);

    Optional<OwnersCatsFlags> findByOwnerAndIsChattingIsTrue(OwnerCat owner);

    Optional<OwnersCatsFlags> findByOwnerAndIsWaitingForContactsIsTrue(OwnerCat owner);

    Optional<OwnersCatsFlags> findByVolunteer(OwnerCat owner);

}
