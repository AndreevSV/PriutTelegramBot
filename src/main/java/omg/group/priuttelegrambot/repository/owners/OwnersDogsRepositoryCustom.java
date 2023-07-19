package omg.group.priuttelegrambot.repository.owners;

import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnersDogsRepositoryCustom  {

    Optional<OwnerDog> findVolunteerByVolunteerIsTrueAndChatsOpenedMinimum();
}

