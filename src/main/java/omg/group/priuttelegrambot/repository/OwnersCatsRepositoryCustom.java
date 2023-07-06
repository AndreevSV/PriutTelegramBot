package omg.group.priuttelegrambot.repository;

import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnersCatsRepositoryCustom {

    Optional<OwnerCat> findFirstByVolunteerIsTrue();

}
