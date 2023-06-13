package omg.group.priuttelegrambot.repository;

import omg.group.priuttelegrambot.entity.animals.Cat;
import omg.group.priuttelegrambot.entity.animals.enimalsenum.Sex;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CatsRepository extends JpaRepository<Cat, Long> {

    @NotNull
    Optional<Cat> findById(@NotNull Long id);

    List<Cat> findByNickNameContainingIgnoreCase(String nickname);

    List<Cat> findBySex(Sex sex);

    List<Cat> findByBirthdayBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Cat> findByBreedContainingIgnoreCase(String breed);

}
