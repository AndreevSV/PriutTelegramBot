package omg.group.priuttelegrambot.repository;

import omg.group.priuttelegrambot.entity.animals.Cat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatsRepository extends JpaRepository<Cat, Long> {

}
