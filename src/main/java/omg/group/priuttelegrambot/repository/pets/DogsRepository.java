package omg.group.priuttelegrambot.repository.pets;

import omg.group.priuttelegrambot.entity.pets.Dog;
import omg.group.priuttelegrambot.entity.pets.petsenum.DogsBreed;
import omg.group.priuttelegrambot.entity.pets.petsenum.Sex;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface DogsRepository extends JpaRepository<Dog, Long> {

    @NotNull
    Optional<Dog> findById(@NotNull Long id);

    List<Dog> findByIdContaining(Long id);

    List<Dog> findByNickNameContainingIgnoreCase(String nickname);

    List<Dog> findBySex(Sex sex);

    List<Dog> findByBirthdayBetween(Date startDate, Date endDate);

    List<Dog> findByBreedContaining(DogsBreed breed);

    @Query("SELECT d FROM Dog d WHERE :currentDate BETWEEN d.probationStarts AND d.probationEnds")
    List<Dog> findDogsWhereCurrentDateBetweenProbationStartsAndProbationEnds(LocalDate currentDate);
}
