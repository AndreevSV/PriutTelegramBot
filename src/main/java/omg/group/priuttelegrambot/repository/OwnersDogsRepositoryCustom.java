package omg.group.priuttelegrambot.repository;

import omg.group.priuttelegrambot.entity.owners.OwnerDog;

import java.util.Optional;

public interface OwnersDogsRepositoryCustom  {

    Optional<OwnerDog> findFirstByVolunteerIsTrue();

}

