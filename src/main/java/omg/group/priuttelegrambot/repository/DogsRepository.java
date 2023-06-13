package omg.group.priuttelegrambot.repository;

import omg.group.priuttelegrambot.entity.animals.Dog;
import omg.group.priuttelegrambot.entity.animals.enimalsenum.Sex;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DogsRepository extends JpaRepository<Dog, Long> {

    @NotNull
    Optional<Dog> findById(@NotNull Long id);

    List<Dog> findByNickNameContainingIgnoreCase(String nickname);

    List<Dog> findBySex(Sex sex);

    List<Dog> findByBirthdayBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Dog> findByBreedContainingIgnoreCase(String breed);
}
