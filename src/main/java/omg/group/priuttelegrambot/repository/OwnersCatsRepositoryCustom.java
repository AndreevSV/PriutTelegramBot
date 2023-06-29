package omg.group.priuttelegrambot.repository;

import omg.group.priuttelegrambot.entity.owners.OwnerCat;

import java.util.Optional;

public interface OwnersCatsRepositoryCustom {

    Optional<OwnerCat> findFirstByVolunteerIsTrue();

}
