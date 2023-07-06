package omg.group.priuttelegrambot.repository;

import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnersDogsRepositoryCustom  {

    Optional<OwnerDog> findFirstByVolunteerIsTrue();

}

