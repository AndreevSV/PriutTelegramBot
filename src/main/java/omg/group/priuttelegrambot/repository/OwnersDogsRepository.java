package omg.group.priuttelegrambot.repository;

import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnersDogsRepository extends JpaRepository<OwnerDog, Long> {
}
