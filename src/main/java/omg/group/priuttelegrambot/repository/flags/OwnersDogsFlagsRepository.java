package omg.group.priuttelegrambot.repository.flags;

import omg.group.priuttelegrambot.entity.flags.OwnersDogsFlags;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnersDogsFlagsRepository extends JpaRepository<OwnersDogsFlags, Long> {

    Optional<OwnersDogsFlags> findByOwner(OwnerDog owner);

    Optional<OwnersDogsFlags> findByOwnerAndIsWaitingForPhotoIsTrueOrIsWaitingForFeelingIsTrueOrIsWaitingForRationIsTrueOrIsWaitingForChangesIsTrue(OwnerDog owner);

    Optional<OwnersDogsFlags> findByOwnerAndIsChattingIsTrue(OwnerDog owner);

    Optional<OwnersDogsFlags> findByOwnerAndIsWaitingForContactsIsTrue(OwnerDog owner);

    Optional<OwnersDogsFlags> findByVolunteer(OwnerDog owner);
}
