package omg.group.priuttelegrambot.repository;

import omg.group.priuttelegrambot.entity.animals.Dog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogsRepository extends JpaRepository<Dog, Long> {
}
