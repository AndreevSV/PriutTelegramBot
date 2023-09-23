package omg.group.priuttelegrambot.repository.pets;

import omg.group.priuttelegrambot.entity.pets.Cat;
import omg.group.priuttelegrambot.entity.pets.petsenum.CatsBreed;
import omg.group.priuttelegrambot.entity.pets.petsenum.Sex;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CatsRepository extends JpaRepository<Cat, Long> {

    @NotNull
    Optional<Cat> findById(@NotNull Long id);

    List<Cat> findByNickNameContainingIgnoreCase(String nickname);

    List<Cat> findBySex(Sex sex);

    List<Cat> findByBirthdayBetween(Date startDate, Date endDate);

    List<Cat> findByBreedContaining(CatsBreed breed);

}
